package net.samism.java.percival;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/23/11
 * Time: 10:07 PM
 */

public class PingMessage extends IRCMessage {

	public PingMessage(String s) {
		this.msg = s;
	}

	@Override
	public boolean isFrom(String author) {
		System.out.println("called isFrom() on PingMessage");
		return false;
	}

	@Override
	public boolean isFromOwner() {
		System.out.println("called isFromOwner() on PingMessage");
		return false;
	}

	@Override
	public String getAuthor() {
		System.out.println("called getAuthor() on PingMessage");
		return "";
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		return "PingMessage: " + msg;
	}
}
