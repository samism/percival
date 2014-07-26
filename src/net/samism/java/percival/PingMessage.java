package net.samism.java.percival;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/23/11
 * Time: 10:07 PM
 */

public class PingMessage extends IRCMessage {

	public PingMessage(String s, PercivalBot pc) {
//		this.msg = s;
//		this.author = "Server";
		super(s, pc);
	}

	@Override
	public String getResponse() {
		String response = "PONG ";

		if (msg.contains(":")) {
			response += msg.split(" ")[1];
		} else {
			response += msg.split(":")[1];
		}

		return response;
	}

	@Override
	public String getMsg() {
		return getRawMsg();
	}

	@Override
	public String toString() {
		return "PingMessage: " + msg;
	}
}
