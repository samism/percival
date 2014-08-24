package net.samism.percival;

import net.samism.java.StringUtils.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/29/2014
 * Time: 4:03 AM
 * <p>
 * Types of possible messages:
 * -command message (respond with response to command from JSON)
 * -ping message (respond with "PONG")
 * -server message (purely for logging purposes)
 * -a message that is none of the above (casual message)?
 */
public abstract class IRCMessage {
	final String msg;
	final String rawMsg;
	final String author;
	final String channel;

	public IRCMessage(String s, PercivalBot pc) {
		this.rawMsg = s;

		this.author = rawMsg.substring(1).split("!")[0];
		this.channel = rawMsg.substring(rawMsg.indexOf("#"), StringUtils.nthIndexOf(rawMsg, ":", 2) - 1);
		this.msg = rawMsg.split("PRIVMSG " + this.channel + " :")[1];
	}

	//for ServerMessage
	public IRCMessage(String s) {
		this.author = "Server";
		this.msg = this.rawMsg = s;
		this.channel = "";
	}

	public boolean isFrom(String author) {
		return this.author.equals(author);
	}

	public boolean isFromOwner() {
		return this.author.equals(PercivalBot.OWNER);
	}

	public String getAuthor() {
		return this.author;
	}

	public String getMsg() {
		return this.msg;
	}

	public String getRawMsg() {
		return this.rawMsg;
	}

	public String getChannel() {
		return this.channel;
	}

	//override this if response depends on a variable
	public abstract String getResponse();

	public abstract String toString();
}