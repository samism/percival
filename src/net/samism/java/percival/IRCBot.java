package net.samism.java.percival;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
			log.info("No such host.");
			e.printStackTrace();
		} catch (IOException e) {
			log.info("Error connecting to the host.");
			e.printStackTrace();
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

			if (fileHasContents()) {
				fw
						.write("\n\n---------------------------------\n\t\t\t\t<------  "
								+ getDate("EEE, MMM d, ''yy")
								+ " at "
								+ getDate("h:mm a") + "  ------>\n");
				fw.write("[" + getDate("H:mm:ss:SSS") + "]: " + line);
			} else {
				fw.write("[" + getDate("H:mm:ss:SSS") + "]: " + line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logConsole(String line) {
		String date = getDate("H:mm:ss:SSS");
		log.info("[" + date + "]" + line);
	}

	public void logToClient(String line) {
	}

	public boolean fileHasContents() {
		String fileContents = "";

		try {
			String ln;
			BufferedReader fileBr = new BufferedReader(new InputStreamReader(
					new DataInputStream(new FileInputStream(LOG_FILE))));

			while ((ln = fileBr.readLine()) != null)
				fileContents += ln;

			fileBr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (fileContents.length() > 0 && !fileContents.isEmpty());
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

	public void setBr(BufferedReader br) {
		this.br = br;
	}

	public BufferedReader getBr() {
		return br;
	}

	public void setBw(BufferedWriter bw) {
		this.bw = bw;
	}

	public BufferedWriter getBw() {
		return bw;
	}

	public Socket getIRCSocket() {
		return irc;
	}

	public void setIRCSocket(Socket IRCSocket) {
		this.irc = IRCSocket;
	}
}