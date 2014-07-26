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
	public String toString() {
		return "FactoidMessage: " + msg;
	}
}
