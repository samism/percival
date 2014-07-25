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
	public static Commands cmd = new Commands();

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
				String rawLine;

				while ((rawLine = pc.getBr().readLine()) != null) {
					IRCMessage msg; //The message is raw by default.

					if (rawLine.contains("PRIVMSG " + getChannelName())
							|| Commands.containsCommand(rawLine)) {
						msg = new CommandMessage(rawLine);
						pc.sendChan(msg.getResponse());
					} else if (rawLine.startsWith("PING ")) {
						msg = new PingMessage(rawLine);
						pc.send(msg.getResponse());
					} else {
						msg = new ServerMessage(rawLine, pc);
						if(msg.getResponse() != null)
							pc.send(msg.getResponse());
					}

					logConsole(">>>" + rawLine);
				}

				log.error("Line came out as null, closing all streams and exiting.");
				pc.getBr().close();
				pc.getBw().close();
				pc.getIRCSocket().close();
				connection.join();
				System.exit(1);
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}