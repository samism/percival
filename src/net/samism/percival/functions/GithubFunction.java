package net.samism.percival.functions;

import net.samism.percival.FunctionalMessage;
import net.samism.percival.IRCRegex;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/19/14
 * Time: 2:27 AM
 */
public class GithubFunction extends Function {
	public GithubFunction(FunctionalMessage message) {
		super(message);
	}

	@Override
	public String perform() {
		return "My source code is at: https://github.com/samism/percival";
	}

	@Override
	public boolean matches() {
		IRCRegex regex = new IRCRegex("you|source");

		return line.matches(regex.toString());
	}

	@Override
	public String getSyntax() {
		return "you|source";
	}

	@Override
	public String toString() {
		return "Get Github Page";
	}
}
