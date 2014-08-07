package net.samism.java.percival;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static net.samism.java.StringUtils.StringUtils.nthIndexOf;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/29/2014
 * Time: 4:03 AM
 */
public class PercivalBot extends IRCBot {
	private static final Logger log = LoggerFactory.getLogger(PercivalBot.class);
	private static final String CONFIG_FILE_PATH = "/Users/samism/Dropbox/programming/java/projects/IRC Bot (Percival)" +
			"/src/net/samism/java/percival/config/percy.config";
	public static final String TRIGGER = "p>";

	private final Connection c = new PercivalBot.Connection(this);
	private final Thread connection = new Thread(c);

	public final Factoids facts = new Factoids(this);
	private final String identPass;

	public PercivalBot(String botName, String serverName, String[] channels, int port) throws IOException {
		super(botName, serverName, channels, port);

		identPass = loadIdentPass(); //needed to protect the identify password

		send("NICK " + getBotName());
		send("USER " + getBotName() + " 0 * :" + getBotName());

		connection.start();
		log.info("starting thread");
	}

	class Connection implements Runnable {
		private PercivalBot pc;

		public Connection(PercivalBot pc) {
			this.pc = pc;
		}

		public void run() {
			log.info("thread started");
			try {
				String rawLine;

				while ((rawLine = pc.getBr().readLine()) != null) { //if its null, thread dies
					logConsole(">>>" + rawLine); //log to the console immediately

					IRCMessage msg; //The message is raw by default.

					if (rawLine.contains("PRIVMSG " + getCurrentChannelName())) {
						String trigger = facts.containsTrigger(rawLine);
						String cleanedLine = cleanLine(rawLine);

						if (cleanedLine.startsWith(TRIGGER)) {
							msg = new FunctionalMessage(rawLine, pc, facts);
							pc.sendChannel(msg.getResponse());
						} else if (trigger != null) {
							msg = new FactoidMessage(rawLine, trigger, pc, facts);
							pc.sendChannel(msg.getResponse());
						}
					} else {
						msg = new ServerMessage(rawLine, pc);
						if (msg.getResponse() != null)
							pc.send(msg.getResponse());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally { //ensure network & file I/O is closed
				log.error("Line came out as null, closing all streams, killing this thread..");
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
			return rawLine.substring(nthIndexOf(rawLine, ":", 2) + 1);
		}
	}

	public String getIdentPass() {
		return identPass;
	}

	private String loadIdentPass() {
		String pass = "";

		try {
			pass = Files.readAllLines(Paths.get(CONFIG_FILE_PATH), StandardCharsets.UTF_8).toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		pass = pass.replace("[", "").replace("]", "");
		return pass;
	}
}