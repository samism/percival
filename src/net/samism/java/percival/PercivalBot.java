package net.samism.java.percival;

import net.samism.java.StringUtils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: Unknown
 * Time: Unknown
 */
public class PercivalBot extends IRCBot {
	private static final Logger log = LoggerFactory.getLogger(PercivalBot.class);

	private final Connection c = new PercivalBot.Connection(this);
	private final Thread connection = new Thread(c);

	public PercivalBot(String botName, String serverName, String channelName, int port) throws IOException {
		super(botName, serverName, channelName, port);

		send("NICK " + getBotName());
		send("USER " + getBotName() + " 0 * :" + getBotName());

		connection.start();
	}

	class Connection implements Runnable {
		private PercivalBot pc;

		public Connection(PercivalBot pc) {
			this.pc = pc;
		}

		public void run() {
			try {
				send("MODE " + getBotName() + " +B"); // for bots
				send("PRIVMSG NickServ :identify 197676");
				send("JOIN " + getChannelName());

				String rawLine;

				while ((rawLine = pc.getBr().readLine()) != null) {
					Thread.sleep(250); //limit response time to 4 times a second
					IRCMessage msg;

					if (rawLine.contains("PRIVMSG " + pc.getChannelName()) &&
							Commands.containsCommand(rawLine)) {
						msg = new CommandMessage(rawLine);
						pc.sendChan(msg.getResponse());
					} else if (rawLine.contains("PING ") && !rawLine.contains("PRIVMSG")) {
						msg = new PingMessage(rawLine);
						pc.send(msg.getResponse());
					} else if (rawLine.contains("PRIVMSG " + pc.getChannelName())) {
						msg = new CasualMessage(rawLine);
						//dont respond..yet
					} else {
						msg = new ServerMessage(rawLine);
					}

					//logging
					if (StringUtils.getTokenCount(rawLine, ":") > 1) {
						logConsole(">>> " + msg.getAuthor() + " | " + msg.getMsg());
					}
				}

				log.error("Line came out as null, closing all streams.");
				pc.getBr().close();
				pc.getBw().close();
				pc.getIRCSocket().close();
				connection.join();
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}

	}
}