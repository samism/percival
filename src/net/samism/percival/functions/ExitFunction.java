package net.samism.percival.functions;

import net.samism.percival.Application;
import net.samism.percival.FunctionalMessage;
import net.samism.percival.IRCRegex;
import net.samism.percival.PercivalBot;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/19/14
 * Time: 2:04 AM
 */
public class ExitFunction extends Function {
	public ExitFunction(FunctionalMessage message) {
		super(message);
	}

	@Override
	public String perform() {
		if (message.isFromOwner())
			Application.exit();
		return "Only " + PercivalBot.OWNER + " can halt my execution.";
	}

	@Override
	public boolean matches() {
		IRCRegex regex = new IRCRegex("exit");

		return line.matches(regex.toString());
	}

	@Override
	public String getSyntax() {
		return "exit";
	}

	@Override
	public String toString() {
		return "Exit";
	}
}
