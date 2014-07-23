package net.samism.java.percival;

import net.samism.java.StringUtils.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/25/11
 * Time: 6:04 AM
 */

public class CasualMessage extends IRCMessage {

	public CasualMessage(String s){
		this.msg = s;

	}

	@Override
	public boolean isFrom(String author) {
		return msg.substring(1, StringUtils.nthIndexOf(msg, "!", 1))
				.equals(author);
	}

	@Override
	public boolean isFromOwner() {
		return isFrom(OWNER);
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
		return "CasualMessage: " + msg;
	}
}
