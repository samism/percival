package net.samism.java.percival;

import net.samism.java.StringUtils.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/23/11
 * Time: 10:05 PM
 */

public class ServerMessage extends IRCMessage {
	PercivalBot pc;

	public ServerMessage(String s, PercivalBot pc) {
		this.msg = s;
		this.author = "Server";
		this.pc = pc;
	}

	@Override
	public String getAuthor() {
		return "Server";
	}

	@Override
	public String getResponse() {
		if(msg.contains("End of /MOTD command.")){
			return "MODE " + pc.getBotName() + " +B"; // for bots
		} else if(msg.contains("This nickname is registered.")){
			return "PRIVMSG NickServ :identify 197676";
		} else if(msg.contains(":You are now identified")){
			return "JOIN " + pc.getChannelName();
		}

		return null;
	}

	@Override
	public String getMsg() {
		return getRawMsg(); //Server message is a raw server message.
	}

	@Override
	public String toString() {
		return "ServerMessage: " + msg;
	}
}
