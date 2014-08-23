package net.samism.percival.functions;

import net.samism.percival.FunctionalMessage;
import net.samism.percival.IRCRegex;
import net.samism.percival.PercivalBot;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/19/14
 * Time: 2:21 AM
 */
public class OwnerFunction extends Function {
	public OwnerFunction(FunctionalMessage message) {
		super(message);
	}

	@Override
	public String perform() {
		return PercivalBot.OWNER;
	}

	@Override
	public boolean matches() {
		IRCRegex regex = new IRCRegex("owner");
		return line.matches(regex.toString());
	}

	@Override
	public String getSyntax() {
		return "owner";
	}

	@Override
	public String toString() {
		return "Get Owner";
	}
}
