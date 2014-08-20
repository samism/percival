package net.samism.java.percival.functions;

import net.samism.java.percival.FunctionalMessage;
import net.samism.java.percival.IRCRegex;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/13/14
 * Time: 9:49 PM
 */
public class ISOCodesFunction extends Function {
	public ISOCodesFunction(FunctionalMessage message) {
		super(message);
	}

	@Override
	public String perform() {
		return "http://en.wikipedia.org/wiki/ISO_3166-1#Current_codes";
	}

	@Override
	public boolean matches() {
		IRCRegex regex = new IRCRegex("iso|country-codes");

		return line.matches(regex.toString());
	}

	@Override
	public String getSyntax() {
		return "iso|country-codes";
	}

	@Override
	public String toString() {
		return "ISO-Codes";
	}
}
