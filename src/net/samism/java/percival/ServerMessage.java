package net.samism.java.percival;

import net.samism.java.utils.StringUtil.StringUtil;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/23/11
 * Time: 10:05 PM
 */

public class ServerMessage extends IRCMessage {

	public ServerMessage(String s) {
		this.msg = s;
	}

	@Override
	public boolean isFrom(String author) {
		System.out.println("called isFrom() on ServerMessage");
		return false;
	}

	@Override
	public boolean isFromOwner() {
		System.out.println("called isFromOwner() on ServerMessage");
		return false;
	}

	@Override
	public String getAuthor() {
		return "Server";
	}

	@Override
	public String getMsg() {
		return msg.contains("PRIVMSG #lingubender")
				? msg.substring(StringUtil.nthIndexOf(msg, ":", 2))
				: getRawMsg();
	}

	@Override
	public String toString() {
		return "ServerMessage: " + msg;
	}
}
