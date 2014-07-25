package net.samism.java.percival;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
	private static final File LOG_FILE = new File(getDate("EEE, MMM d, ''yy")); // file
	private static final File LOGS_DIR = new File("logs"); // dir

	private String botName;
	private String serverName;
	private String channelName;
	private int port;

	private BufferedReader br;
	private BufferedWriter bw;
	private Socket irc;

	private FileWriter fw;

	public IRCBot(String botName, String serverName, String channelName, int port) {
		this.setBotName(botName);
		this.setServerName(serverName);
		this.setChannelName("#" + channelName);
		this.setPort(port);

		connect(serverName, port);
	}

	public void connect(String server, int port) {
		try {
			irc = new Socket(server, port);
			bw = new BufferedWriter(new OutputStreamWriter(irc
					.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(irc.getInputStream()));
		} catch (UnknownHostException e) {
			log.error("No such host.");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			log.error("Error connecting to the host.");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void send(String msg) throws IOException {
		bw.write(msg + NL);
		bw.flush();
	}

	public void sendChan(String msg) throws IOException {
		bw.write("PRIVMSG " + channelName + " :" + msg + NL);
		bw.flush();
	}

	public void sendChan(String msg, String channel) throws IOException {
		bw.write("PRIVMSG " + channel + " :" + msg + NL);
		bw.flush();
	}

	public void join(String chan) throws IOException {
		sendChan("/join " + chan);
	}

	public void leaveChannel() throws IOException {
		sendChan("/part " + channelName);
	}

	public void leaveChannel(String partMsg) throws IOException {
		sendChan("/part " + channelName + " " + partMsg);
	}

	public void leaveAllChannels() throws IOException {
		sendChan("/join 0"); // "/join 0" same effect as /partall
	}

	public void log(String line) {
		// logs to console, file, and later gui, by default
		// need to get file logging system working...
		log.info("[" + getDate("H:mm:ss:SSS") + "]: " + line);

		try {
			if (LOGS_DIR.exists()) {
				if (LOG_FILE.exists()) {
					fw = new FileWriter(LOG_FILE);
				} else {
					if (LOG_FILE.createNewFile()) {
						fw = new FileWriter(LOG_FILE);
					} else {
						log.info("couldn't make log file");
					}
				}
			} else {
				if (LOGS_DIR.mkdir()) {
					if (LOG_FILE.exists()) {
						fw = new FileWriter(LOG_FILE);
					} else {
						if (LOG_FILE.createNewFile()) {
							fw = new FileWriter(LOG_FILE);
						} else {
							log.info("couldn't make log file");
						}
					}
				} else {
					log.info("couldn't make logs directory");
				}
			}

			if (!isFileEmpty()) {
			} else {
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isFileEmpty() {
		boolean isEmpty = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader(LOG_FILE.getPath()));
			isEmpty = br.readLine() == null;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return isEmpty;
	}

	private static String getDate(String format) {
		return new SimpleDateFormat(format).format(Calendar.getInstance()
				.getTime());
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setBotName(String botName) {
		this.botName = botName;
	}

	public String getBotName() {
		return botName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public BufferedReader getBr() {
		return br;
	}

	public BufferedWriter getBw() {
		return bw;
	}
}