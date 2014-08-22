package net.samism.java.percival;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static net.samism.java.StringUtils.StringUtils.nthIndexOf;
import static net.samism.java.percival.util.File.loadText;


/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/29/2014
 * Time: 4:03 AM
 */
public final class PercivalBot extends IRCBot {
	private static final Logger log = LoggerFactory.getLogger(PercivalBot.class);

	private final Connection c = new PercivalBot.Connection(this);
	private final Thread connection = new Thread(c);

	//private final FactoidsJSON facts = new FactoidsJSON();
	private final FactoidsJDBC facts = new FactoidsJDBC();

	private boolean shouldDie = false;
	private String identPass = loadText(getClass(), CONFIG_FILE_PATH).split(" ")[0];

	public PercivalBot(String serverName, String[] channels, int port) throws IOException {
		super(BOT_NAME, serverName, channels, port);

		connect(this);
		connection.start();
	}

	class Connection implements Runnable {
		private PercivalBot pc;

		public Connection(PercivalBot pc) {
			this.pc = pc;
		}

		public void run() {
			try {
				send("NICK " + BOT_NAME);
				send("USER " + BOT_NAME + " 0 * :" + BOT_NAME);

				String rawLine;

				while ((rawLine = pc.getBr().readLine()) != null && !shouldDie) { //if its null, thread dies
					logConsole(">>>" + rawLine); //logs to the console immediately

					IRCMessage msg;

					//todo: fix bug where only responds to the first channel.
					//modifying the currentChannel variable might not be threadsafe.
					//Should there be a new instance of PercivalBot for each channel instead of just for each network?
					//Nested threads?
					if (rawLine.contains("PRIVMSG " + getCurrentChannelName())) {
						//if (rawLine.contains("PRIVMSG ")) {
						String cleanedLine = cleanLine(rawLine);
						String trigger = facts.containsTrigger(cleanedLine);

						if (cleanedLine.startsWith(BOT_COMMAND_PREFIX)) {
							msg = new FunctionalMessage(rawLine, pc, facts);
							sendChannel(msg.getResponse());
						} else if (trigger != null) {
							msg = new FactoidMessage(rawLine, trigger, pc, facts);
							sendChannel(msg.getResponse());
						}
					} else {
						msg = new ServerMessage(rawLine, pc);
						if (msg.getResponse() != null)
							pc.send(msg.getResponse());
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

		private String cleanLine(String rawLine) {
			rawLine = rawLine.substring(nthIndexOf(rawLine, ":", 2) + 1);
			rawLine = StringEscapeUtils.escapeJava(rawLine);
			return rawLine;
		}
	}

	public String getIdentPass() {
		return this.identPass;
	}

	public void setShouldDie(boolean shouldIt) {
		this.shouldDie = shouldIt;
	}

	public Thread getConnection() {
		return this.connection;
	}
}