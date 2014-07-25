package net.samism.java.percival;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Types of possible messages:
 * -command message (respond with response to command from JSON)
 * -ping message (respond with "PONG")
 * -server message (purely for logging purposes)
 * -a message that is none of the above (casual message)?
 */
public abstract class IRCMessage {
	static final Logger log = LoggerFactory.getLogger(IRCMessage.class);
	static Commands cmd = PercivalBot.cmd;

	String msg = null;
	String rawMsg = null;
	String author;

	public IRCMessage(String s) {
		this.rawMsg = s;
		this.author = rawMsg.substring(1, rawMsg.indexOf("!"));
	}

	public IRCMessage() {
	}

	public boolean isFrom(String author) {
		return this.author.equals(author);
	}

	public boolean isFromOwner() {
		return author.equals(PercivalBot.OWNER);
	}

	public String getAuthor() {
		return this.author;
	}

	public String getRawMsg() {
		return msg;
	}

	//override this if response depends on a variable
	public abstract String getResponse();

	public abstract String getMsg();

	public abstract String toString();
}