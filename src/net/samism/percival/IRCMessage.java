package net.samism.percival;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/29/2014
 * Time: 4:03 AM
 * <p/>
 * Types of possible messages:
 * -command message (respond with response to command from JSON)
 * -ping message (respond with "PONG")
 * -server message (purely for logging purposes)
 * -a message that is none of the above (casual message)?
 */
public abstract class IRCMessage {
	final PercivalBot pc;

	final String msg, rawMsg, author;

	public IRCMessage(String s, PercivalBot pc) {
		this.pc = pc;
		this.rawMsg = s;

		if (rawMsg.contains("PRIVMSG " + pc.getCurrentChannelName())) {
			this.author = rawMsg.substring(1).split("!")[0];
			this.msg = s.split("PRIVMSG " + pc.getCurrentChannelName() + " :")[1];
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