package net.samism.percival;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/23/11
 * Time: 10:05 PM
 * <p/>
 * This class handles responses to messages from the server.
 * That includes: PING/PONG and initial nickserv stuff
 */

public class ServerMessage extends IRCMessage {
	public ServerMessage(String s, PercivalBot pc) {
		super(s, pc);
	}

	@Override
	public String getAuthor() {
		return "Server";
	}

	@Override
	public String getResponse() {
		if (rawMsg.startsWith("PING ")) {
			if (msg.contains(":")) {
				return "PONG " + msg.split(" ")[1];
			} else {
				return "PONG " + msg.split(":")[1];
			}
		} else if (rawMsg.contains("End of /MOTD command.")) {
			return "MODE " + PercivalBot.BOT_NAME + " +B"; // for bots
		} else if (rawMsg.contains("This nickname is registered.")) {
			return "PRIVMSG NickServ :identify " + Application.IDENT_PASS;
		} else if (rawMsg.contains(":You are now identified")) {
			String join = "";

			for (String channel : pc.getChannels()) { //join all channels for this network
				join += "JOIN " + channel;
				if(pc.getChannels().size() > 1)
					join += PercivalBot.NL;
			}

			return join;
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
