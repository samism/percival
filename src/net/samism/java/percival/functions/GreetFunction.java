package net.samism.java.percival.functions;

import net.samism.java.percival.FunctionalMessage;

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
		return line.matches("hi|hey|hello|sup|yo|heya|howdy");
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
