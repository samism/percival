package net.samism.java.percival.functions;

import net.samism.java.percival.FunctionalMessage;
import net.samism.java.percival.IRCRegex;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/19/14
 * Time: 2:35 AM
 */
public class FactoidsFunction extends Function {
	public FactoidsFunction(FunctionalMessage message) {
		super(message);
	}

	@Override
	public String perform() {
		Set<String> s = message.getFactoidsObject().getTriggers();

		return "Here are my factoids (" + s.size() + ") : " + s.toString();
	}

	@Override
	public boolean matches() {
		IRCRegex regex = new IRCRegex("fact(oids?)?");

		return line.matches(regex.toString());
	}

	@Override
	public String getSyntax() {
		return "fact(oid(s))";
	}

	@Override
	public String toString() {
		return "List Factoids";
	}
}
