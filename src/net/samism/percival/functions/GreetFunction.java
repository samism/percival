package net.samism.percival.functions;

import net.samism.percival.FunctionalMessage;
import net.samism.percival.IRCRegex;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/9/14
 * Time: 2:00 AM
 */
public class GreetFunction extends Function {
	public GreetFunction(FunctionalMessage message) {
		super(message);
	}

	@Override
	public String perform() {
		return message.getAuthor() + ", how's it going?";
	}

	@Override
	public boolean matches() {
		IRCRegex regex = new IRCRegex("(hi|hey|hello|sup|yo|heya|howdy)[^\\r\\n\\w\\d\\s]?");

		return line.matches(regex.toString());
	}

	@Override
	public String getSyntax() {
		return "hi|hey|hello|sup|yo|heya|howdy";
	}

	@Override
	public String toString() {
		return "Greet";
	}
}
