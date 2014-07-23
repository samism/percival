package net.samism.java.percival;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: Unknown
 * Time: Unknown
 */
public interface IRCFunctions {

	void connect(String server, int port);

	void send(String msg) throws IOException;

	void sendChan(String msg) throws IOException;

	void sendChan(String msg, String channel) throws IOException;

	void join(String chan) throws IOException;

	void leaveAllChannels() throws IOException;

	void leaveChannel(String partMsg) throws IOException;

	void leaveChannel() throws IOException;

	void log(String line);

	void logConsole(String line);

	void logToClient(String line);
}
