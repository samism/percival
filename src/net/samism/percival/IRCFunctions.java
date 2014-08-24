package net.samism.percival;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/29/2014
 * Time: 4:04 AM
 */
public interface IRCFunctions {
	void connect(PercivalBot bot);

	void send(String msg) throws IOException;

	void sendChannel(String msg, String channel) throws IOException;

	void join(String chan) throws IOException;

	void leaveAllChannels() throws IOException;

	void leaveChannel(String partMsg, String channel) throws IOException;

	void leaveChannel(String partMsg) throws IOException;

	void log(String line);
}
