package net.samism.java.percival.functions;

import net.samism.java.percival.FunctionalMessage;
import net.samism.java.percival.PercivalBot;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 7/27/14
 * Time: 4:11 AM
 */
public abstract class Function {
	FunctionalMessage message;
	String line;

	public Function(FunctionalMessage message) {
		this.message = message;
		this.line = message.getMsg().replaceFirst(PercivalBot.BOT_COMMAND_PREFIX, "").trim();
	}

	/**
	 * Actually does what the Function was created to do, e.g. obtain the date, translate a string, URL encode, ...
	 *
	 * @return A String representing the result of the Function, or if it failed, an appropriate String describing
	 * the reason for the failure.
	 */
	public abstract String perform();

	/**
	 * Determines whether this is the appropriate subclass of Function to handle the request.
	 * The criteria is usually a regex that conforms to this class's implementation of the format requested by
	 * getSyntax().
	 *
	 * @return True if the line matches the given regex, signaling to respond, or false if otherwise.
	 * @see Function#getSyntax();
	 */
	public abstract boolean matches();

	/**
	 * For informing the user what the correct way to structure this Function is, syntacticly.
	 * <p/>
	 * The implementation of this method obviously depends on the nature of the Function.
	 *
	 * @return A String describing the syntax for executing a given Function.
	 */
	public abstract String getSyntax();

	/**
	 * Must implement so that the purpose of the function, as a verb, could be later retrieved.
	 *
	 * @return The name of any given subclass of this class, exlcuding the "Function" suffix.
	 */
	public abstract String toString();
}
