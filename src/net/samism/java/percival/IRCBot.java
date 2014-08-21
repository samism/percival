package net.samism.java.percival;

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

	public static final String OWNER = "ffs82defxp";
	public static final String NL = "\r\n";
	public static final String CONFIG_FILE_PATH = "misc/percy.config";
	public static final String BOT_NAME = "Percival";
	public static final String BOT_COMMAND_PREFIX = "p.";

	private static final File LOGS_DIR = new File("logs"); // logs directory
	private static final File LOG_FILE = new File("log"); // file

	private static final FileWriter fw = null;

//	static {
//		//todo: figure out FileWriter stuff
//		fw = null;
//	}

	private String botName;
	private String serverName;
	private int port;

	private Socket irc;
	private BufferedWriter bw;
	private BufferedReader br;

	private List<String> channels = new ArrayList<>();
	private volatile String currentChannel;

	public IRCBot(String botName, String serverName, String[] channels, int port) {
		this.botName = botName;
		this.serverName = serverName;
		this.port = port;

		Collections.addAll(this.channels, channels);
		this.currentChannel = this.channels.get(0); //for now. todo: how to determine which channel is the "current"?
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
	public void sendChannel(String msg) throws IOException {
		logConsole("<<<" + msg);
		bw.write("PRIVMSG " + currentChannel + " :" + msg + NL);
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
	public void leaveChannel() throws IOException {
		send("PART " + currentChannel);
	}

	@Override
	public void leaveChannel(String partMsg) throws IOException {
		send("PART " + currentChannel + " " + partMsg);
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

	public void setCurrentChannelName(String channelName) {
		this.currentChannel = channelName;
	}

	public String getCurrentChannelName() {
		return this.currentChannel;
	}

	public String getServerName() {
		return this.serverName;
	}

	public String getBotName() {
		return this.botName;
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

	public List<String> getChannels() {
		return this.channels;
	}
}
