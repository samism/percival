package net.samism.java.percival;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static net.samism.java.percival.Application.botInstances;
import static net.samism.java.percival.Application.exit;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/29/2014
 * Time: 4:03 AM
 */
public class PercivalBot extends IRCBot {
	private static final Logger log = LoggerFactory.getLogger(PercivalBot.class);
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
	}

	class Connection implements Runnable {
		private PercivalBot pc;

		public Connection(PercivalBot pc) {
			this.pc = pc;
		}

		public void run() {
			try {
				String rawLine;

				while ((rawLine = pc.getBr().readLine()) != null) { //if its null, thread dies
					logConsole(">>>" + rawLine); //log to the console immediately

					IRCMessage msg; //The message is raw by default.

					if (rawLine.contains("PRIVMSG " + getCurrentChannelName()) && facts.containsTrigger(rawLine)) {
						msg = new FactoidMessage(rawLine, pc, facts);
						pc.sendChannel(msg.getResponse());
					} else if (rawLine.contains("PRIVMSG " + getCurrentChannelName() + " :" + TRIGGER)) { //eg. p>owner
						msg = new FunctionalMessage(rawLine, pc, facts);
						pc.sendChannel(msg.getResponse());
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

				botInstances.remove(pc); //remove this object from the list
				if (botInstances.isEmpty())
					exit();
			}
		}
	}

	public String getIdentPass() {
		return identPass;
	}

	private String loadIdentPass(){
		String pass = "";

		try {
			pass = new Scanner(new File("config/percy.config")).next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return pass;
	}
}