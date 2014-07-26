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
	PercivalBot pc;

	String msg, rawMsg, author;

	public IRCMessage(String s, PercivalBot pc) {
		this.pc = pc;
		this.rawMsg = s;

		if (rawMsg.contains("PRIVMSG " + pc.getChannelName())) {
			this.author = rawMsg.substring(1, rawMsg.indexOf("!"));
			this.msg = s.split("PRIVMSG " + pc.getChannelName() + " :")[1];
		} else {
			this.author = "Server";
			this.msg = rawMsg;
		}
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

	public String getMsg() {
		return msg;
	}

	public String getRawMsg() {
		return rawMsg;
	}

	//override this if response depends on a variable
	public abstract String getResponse();

	public abstract String toString();
}