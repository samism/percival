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
	private static final Logger log = LoggerFactory.getLogger(PercivalBot.class);


	private final ResponseHandler rHandler = new ResponseHandler(this);
	private final Connection c = new PercivalBot.Connection(this);
	private final Thread connection = new Thread(c);

	public PercivalBot(String botName, String serverName, String channelName, int port) throws IOException {
		super(botName, serverName, channelName, port);

		send("NICK " + getBotName());
		send("USER " + getBotName() + " 0 * :" + getBotName());

		connection.start();
	}

	class Connection implements Runnable {
		private final PercivalBot pc;

		private long lastReplied = System.currentTimeMillis();

		public Connection(PercivalBot pc) {
			this.pc = pc;
		}

		public void run() {
				try {
					send("MODE " + getBotName() + " +B"); // for bots
					send("PRIVMSG NickServ :identify 197676");
					send("JOIN " + getChannelName());

					String curLine;

					while ((curLine = pc.getBr().readLine()) != null) {
						Thread.sleep(250); //limit response time to 4 times a second
						IRCMessage msg;

						if (curLine.contains("PRIVMSG " + pc.getChannelName()) && commandIsPresent(curLine)) {
							msg = new CommandMessage(curLine);
							pc.sendChan(msg.getResponse());
							lastReplied = System.currentTimeMillis();
						} else if (curLine.contains("PING ") && !curLine.contains("PRIVMSG")) {
							msg = new PingMessage(curLine);
							pc.send(msg.getResponse());
							lastReplied = System.currentTimeMillis();
						} else if (curLine.contains("PRIVMSG " + pc.getChannelName())) {
							msg = new CasualMessage(curLine);
							//dont respond..yet
						} else {
							msg = new ServerMessage(curLine);
						}

						//logging
						if (StringUtils.getTokenCount(curLine, ":") > 1) {
							pc.logConsole(">>> " + msg.getAuthor() + " | " + msg.getMsg());
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

		public boolean commandIsPresent(String line) {
			Pattern req = Pattern.compile(
					"^(perc(ival|y)(,|:))\\s?(help|exit|time|owner|hi|show that you are mine!)",
					Pattern.CASE_INSENSITIVE);
			return (req.matcher(line).find());
		}
	}

	public Connection getConnection() {
		return this.c;
	}
}