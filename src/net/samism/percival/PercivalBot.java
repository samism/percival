package net.samism.percival;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static net.samism.java.StringUtils.StringUtils.nthIndexOf;


/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/29/2014
 * Time: 4:03 AM
 */
public final class PercivalBot extends IRCBot {
	private static final Logger log = LoggerFactory.getLogger(PercivalBot.class);

	private final Thread loop = new Thread(new IRCLoop(this));
	private final FactoidsJDBC facts = new FactoidsJDBC();

	private boolean shouldDie = false;

	public PercivalBot(String serverName, String[] channels, int port) throws IOException {
		super(serverName, channels, port);

		connect(this);
		loop.start();
	}

	class IRCLoop implements Runnable {
		private PercivalBot pc;

		public IRCLoop(PercivalBot pc) {
			this.pc = pc;
		}

		public void run() {
			try {
				send("NICK " + BOT_NAME);
				send("USER " + BOT_NAME + " 0 * :" + BOT_NAME);

				while (true) { //if its null, thread dies
					final String rawLine = pc.getBr().readLine();

					if (rawLine == null || shouldDie)
						break;

					logConsole(">>>" + rawLine); //logs to the console immediately

					IRCMessage msg;

					if (rawLine.contains("PRIVMSG #")) {
						String cleanLine = rawLine.substring(nthIndexOf(rawLine, ":", 2) + 1); //no meta data
						String trigger = facts.containsTrigger(cleanLine);

						if (cleanLine.startsWith(BOT_COMMAND_PREFIX)) {
							msg = new FunctionalMessage(rawLine, pc);
						} else if (!trigger.isEmpty()) {
							msg = new FactoidMessage(rawLine, trigger, pc);
						} else {
							continue;
						}

						sendChannel(msg.getResponse(), msg.getChannel());
					} else {
						msg = new ServerMessage(rawLine);

						if (((ServerMessage) msg).shouldRespondTo(rawLine))
							send(msg.getResponse());
					}
				}
			} catch (IOException e) {
				log.error("Check your internet connection, file permissions");
				e.printStackTrace();
			} finally { //ensure network & file I/O is closed
				log.error("Line came out as null, closing all streams, letting thread die...");
				try {
					pc.getBr().close();
					pc.getBw().close();
					pc.getIRCSocket().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setShouldDie(boolean shouldIt) {
		this.shouldDie = shouldIt;
	}

	public FactoidsJDBC getFactoidsObject() {
		return this.facts;
	}
}