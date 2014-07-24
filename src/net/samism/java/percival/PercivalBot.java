package net.samism.java.percival;

import net.samism.java.StringUtils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: Unknown
 * Time: Unknown
 */
public class PercivalBot extends IRCBot {
	private static final Logger log = LoggerFactory.getLogger(IRCBot.class);


	private final ResponseHandler rHandler = new ResponseHandler(this);
	private final Connection c = new PercivalBot.Connection(this);
	private final Thread connection = new Thread(c);

	public PercivalBot(String botName, String serverName, String channelName, int port) throws IOException {
		super(botName, serverName, channelName, port);

		send("NICK " + getBotName());
		send("USER " + getBotName() + " 0 * :" + getBotName());

		c.setShouldConnect(true);
		connection.start();
	}

	class Connection implements Runnable {
		private final PercivalBot pc;
		private boolean shouldConnect;

		private boolean didPrelims = false;

		private long lastReplied = System.currentTimeMillis();

		public Connection(PercivalBot pc) {
			this.pc = pc;
		}

		public void run() {
			if (shouldConnect) {
				try {
					String curLine;

					while ((curLine = pc.getBr().readLine()) != null) {
						IRCMessage msg = null;

						if (!didPrelims) {
							if (curLine.contains("foonetic.net 376 Percival :End of /MOTD command.")
									&& !curLine.contains("PRIVMSG")) {
								send("MODE " + getBotName() + " +B"); // for bots
								send("JOIN " + getChannelName());
								send("PRIVMSG NickServ :identify 197676");
								didPrelims = true;
							}
						}

						if ((curLine.contains("PRIVMSG #lingubender") && commandIsPresent(curLine))) {
							if (System.currentTimeMillis() - lastReplied >= 500) {
								msg = new CommandMessage(curLine);
								log.info(msg.toString());
								pc.sendChan(rHandler.getResponse(msg));
								lastReplied = System.currentTimeMillis();
							}
						} else if (curLine.contains("PRIVMSG #lingubender")) {
							msg = new CasualMessage(curLine);
							//dont respond..yet
						} else if (curLine.contains("PING ") && !curLine.contains("PRIVMSG")) {
							if (System.currentTimeMillis() - lastReplied >= 500) {
								msg = new PingMessage(curLine);
								log.info(msg.toString());
								pc.send(rHandler.getResponse(msg));
								lastReplied = System.currentTimeMillis();
							}
						} else {
							msg = new ServerMessage(curLine);
						}

						//logging
						if (StringUtils.getTokenCount(curLine, ":") > 1) {
							pc.logConsole(">>> " + msg.getAuthor() + "|" + msg.getMsg());
						}
					}
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				pc.getBr().close();
				pc.getBw().close();
				pc.getIRCSocket().close();
				pc.connection.join();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public boolean commandIsPresent(String line) {
			Pattern req = Pattern.compile(
					"(perc(ival,|y,|ival:|y:) ?|\\$)(help|exit|time|owner|hi)",
					Pattern.CASE_INSENSITIVE);
			return (req.matcher(line).find());
		}

		public void setShouldConnect(boolean shouldConnect) {
			this.shouldConnect = shouldConnect;
		}

		public boolean getShouldConnect() {
			return shouldConnect;
		}
	}

	public Connection getConnection() {
		return this.c;
	}
}