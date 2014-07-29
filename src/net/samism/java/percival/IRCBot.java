package net.samism.java.percival;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static net.samism.java.percival.Application.exit;


/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: Unknown
 * Time: Unknown
 */

public class IRCBot implements IRCFunctions {
	private static final Logger log = LoggerFactory.getLogger(IRCBot.class);

	public static final String OWNER = "ffs82defxp";
	private static final String NL = "\r\n";

	private static final File LOG_FILE = new File(getDate("M.d.yy") + ".log"); // file
	private static final File LOGS_DIR = new File("percy-logs"); // logs directory

	private static final FileWriter fw;

	static {
		//todo: figure out FileWriter stuff
		fw = null;
	}

	private String botName;
	private String serverName;
	private int port;
	private ArrayList<String> channels;

	private String currentChannel;

	private BufferedReader br;
	private BufferedWriter bw;
	private Socket irc;


	public IRCBot(String botName, String serverName, String[] channels, int port) {
		this.setBotName(botName);
		this.setServerName(serverName);
		this.setPort(port);

		Collections.addAll(this.channels, channels);
		this.currentChannel = this.channels.get(0); //for now. todo: how to determine which channel is the "current"?
	}

	@Override
	public void connect() {
		try {
			irc = new Socket(serverName, port);
			bw = new BufferedWriter(new OutputStreamWriter(irc.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(irc.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			exit();
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
		sendChannel("/join " + chan);
	}

	@Override
	public void leaveChannel() throws IOException {
		sendChannel("/part " + currentChannel);
	}

	@Override
	public void leaveChannel(String partMsg) throws IOException {
		sendChannel("/part " + currentChannel + " " + partMsg);
	}

	@Override
	public void leaveAllChannels() throws IOException {
		sendChannel("/join 0"); // "/join 0" same effect as /partall
	}

	@Override
	public void logConsole(String line) {
		String date = getDate("H:mm:ss:SSS");
		log.info("[" + date + "]" + line);
	}

	@Override
	public void log(String line) {
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
		try {
			BufferedReader br = new BufferedReader(new FileReader(LOG_FILE.getPath()));
			isEmpty = br.readLine() == null;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return isEmpty;
	}

	public static String getDate(String format) {
		return new SimpleDateFormat(format).
				format(Calendar.getInstance().getTime());
	}

	public void setCurrentChannelName(String channelName) {
		this.currentChannel = channelName;
	}

	public String getCurrentChannelName() {
		return this.currentChannel;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerName() {
		return this.serverName;
	}

	public void setBotName(String botName) {
		this.botName = botName;
	}

	public String getBotName() {
		return this.botName;
	}

	public void setPort(int port) {
		this.port = port;
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