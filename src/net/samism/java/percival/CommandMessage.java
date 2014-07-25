package net.samism.java.percival;

import net.samism.java.StringUtils.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/23/11
 * Time: 10:07 PM
 */

public class CommandMessage extends IRCMessage {

	CommandMessage(String s) {
		this.msg = s;
	}

	@Override
	public String getResponse() {
		return cmd.getResponse(msg);
	}

	@Override
	public boolean isFrom(String author) {
		return msg.substring(1, StringUtils.nthIndexOf(msg, "!", 1))
				.equals(author);
	}

	@Override
	public boolean isFromOwner() {
		return isFrom(PercivalBot.OWNER);
	}

	@Override
	public String getAuthor() {
		return msg.substring(1, msg.indexOf("!"));
	}

	@Override
	public String getMsg() {
		return msg.substring(StringUtils.nthIndexOf(msg, ":", 2) + 1);
	}

	@Override
	public String toString() {
		return "CommandMessage: " + msg;
	}
}
