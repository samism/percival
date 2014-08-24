package net.samism.percival;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/23/11
 * Time: 10:05 PM
 * <p>
 * This class handles responses to messages from the server.
 * That includes: PING/PONG and initial nickserv stuff
 */

public class ServerMessage extends IRCMessage {
	enum Type {
		PING, MOTD, IDENTIFY, IDENTIFIED
	}

	private Type currentType;

	public ServerMessage(String s) {
		super(s);
	}

	public boolean shouldRespondTo(String line) {
		if (rawMsg.startsWith("PING ")) {
			this.currentType = Type.PING;
		} else if (rawMsg.contains("End of /MOTD command.")) {
			this.currentType = Type.MOTD;
		} else if (rawMsg.contains("This nickname is registered.")) {
			this.currentType = Type.IDENTIFY;
		} else if (rawMsg.contains(":You are now identified")) {
			this.currentType = Type.IDENTIFIED;
		}

		return this.currentType != null;
	}

	@Override
	public String getAuthor() {
		return "Server";
	}

	@Override
	public String getResponse() {
		switch (this.currentType) {
			case PING:
				if (msg.contains(":")) {
					return "PONG " + msg.split(" ")[1];
				} else {
					return "PONG " + msg.split(":")[1];
				}
			case MOTD:
				return "MODE " + PercivalBot.BOT_NAME + " +B"; // for bots
			case IDENTIFY:
				return "PRIVMSG NickServ :identify " + Application.IDENT_PASS;
			case IDENTIFIED: {
				String join = "";

				for (String channel : PercivalBot.CHANNELS) { //join all channels for this network
					join += "JOIN " + channel;
					if (PercivalBot.CHANNELS.size() > 1)
						join += PercivalBot.NL;
				}

				return join;
			}
			default:
				return null; //should never get here.
		}
//		if (rawMsg.startsWith("PING ")) {
//			if (msg.contains(":")) {
//				return "PONG " + msg.split(" ")[1];
//			} else {
//				return "PONG " + msg.split(":")[1];
//			}
//		} else if (rawMsg.contains("End of /MOTD command.")) {
//			return "MODE " + PercivalBot.BOT_NAME + " +B"; // for bots
//		} else if (rawMsg.contains("This nickname is registered.")) {
//			return "PRIVMSG NickServ :identify " + Application.IDENT_PASS;
//		} else if (rawMsg.contains(":You are now identified")) {
//			String join = "";
//
//			for (String channel : PercivalBot.CHANNELS) { //join all channels for this network
//				join += "JOIN " + channel;
//				if(PercivalBot.CHANNELS.size() > 1)
//					join += PercivalBot.NL;
//			}
//
//			return join;
//	}

//	return null;
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
