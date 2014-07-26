package net.samism.java.percival;

import net.samism.java.StringUtils.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/23/11
 * Time: 10:07 PM
 */

public class FactoidMessage extends IRCMessage {
	private Factoids facts;

	public FactoidMessage(String s, PercivalBot pc, Factoids facts) {
		super(s, pc);
		this.msg = s.substring(s.indexOf(":", s.indexOf("PRIVMSG #") + 9));
		this.facts = facts;
	}

	@Override
	public String getResponse() {
		String command = msg.substring(StringUtils.nthIndexOf(msg, ":", 2) + 1);

		if (command.contains(":")) {
			command = msg.split(":")[1];
		} else if (command.contains(",")) {
			command = msg.split(",")[1];
		}

		command = command.trim();

		return facts.getFactoid(command);
	}

	@Override
	public boolean isFrom(String author) {
		return msg.substring(1, msg.indexOf("!")).equals(author);
	}

	@Override
	public boolean isFromOwner() {
		return isFrom(PercivalBot.OWNER);
	}

	@Override
	public String getMsg() {
		return msg.substring(StringUtils.nthIndexOf(msg, ":", 2) + 1);
	}

	@Override
	public String toString() {
		return "FactoidMessage: " + msg;
	}
}
