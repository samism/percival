package net.samism.percival;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/29/2014
 * Time: 4:05 AM
 */

public class IRCBot implements IRCFunctions {
	private static final Logger log = LoggerFactory.getLogger(IRCBot.class);

	public static final String BOT_NAME = "Percival";
	public static final String BOT_COMMAND_PREFIX = "p.";
	public static final String OWNER = "ffs82defxp";
	public static final String NL = "\r\n";

	public static final List<String> CHANNELS = new ArrayList<>();

	private static final File LOGS_DIR = new File("logs"); // logs directory
	private static final File LOG_FILE = new File("log"); // file

	private static final FileWriter fw = null;

//	static {
//		//todo: figure out FileWriter stuff
//		fw = null;
//	}

	private String serverName;
	private int port;

	private Socket irc;
	private BufferedWriter bw;
	private BufferedReader br;

	public IRCBot(String serverName, String[] channels, int port) {
		this.serverName = serverName;
		this.port = port;

		Collections.addAll(IRCBot.CHANNELS, channels);
	}

	@Override
	public void connect(PercivalBot pc) {
		try {
			irc = new Socket(serverName, port);
			bw = new BufferedWriter(new OutputStreamWriter(irc.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(irc.getInputStream()));
		} catch (IOException e) {
			Application.destroyBot(pc);
			log.error("Couldn't connect to this IRC network. Destroying bot instance.");
			e.printStackTrace();
		}
	}

	@Override
	public void send(String msg) throws IOException {
		logConsole("<<<" + msg);
		bw.write(msg + NL);
		bw.flush();
	}

	@Override
	public void sendChannel(String msg, String channel) throws IOException {
		logConsole("<<<" + msg);
		bw.write("PRIVMSG " + channel + " :" + msg + NL);
		bw.flush();
	}

	@Override
	public void join(String chan) throws IOException {
		send("JOIN " + chan);
	}

	@Override
	public void leaveChannel(String channel) throws IOException {
		send("PART " + channel);
	}

	@Override
	public void leaveChannel(String partMsg, String channel) throws IOException {
		send("PART " + channel + " " + partMsg);
	}

	@Override
	public void leaveAllChannels() throws IOException {
		send("JOIN 0"); // "join 0" same effect as /partall
	}

	@Override
	public void logConsole(String line) {
		log.info(line);
	}

	@Override
	public final void log(String line) {
		//todo: Get file logging working.
		logConsole(line);

		//todo: write to a file

		if (!isFileEmpty()) {
			//do stuff provided file has been appended to already
		} else {
		}

	}

	private boolean isFileEmpty() {
		boolean isEmpty = false;
		try (BufferedReader br = new BufferedReader(new FileReader(LOG_FILE.getPath()))) {
			isEmpty = br.readLine() == null;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return isEmpty;
	}

	public String getServerName() {
		return this.serverName;
	}

	public BufferedReader getBr() {
		return this.br;
	}

	public BufferedWriter getBw() {
		return this.bw;
	}

	public Socket getIRCSocket() {
		return this.irc;
	}
}
