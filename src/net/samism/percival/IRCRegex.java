package net.samism.percival;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/19/14
 * Time: 8:38 PM
 * <p>
 * Immutable class intended to be used when trying to match a line of IRC input to a regex.
 * <p>
 * Instead of forgetting to put ^ and $ at the end of the regex, and other potential things appropriate
 * for a line of IRC input, this object can be used.
 */
public final class IRCRegex {
	private static final String LINE_START_REGEX = "^";
	private static final String LINE_END_REGEX = "$";

	private final String regex;

	public IRCRegex(String regex) {
		this.regex = LINE_START_REGEX + regex + LINE_END_REGEX;
	}

	@Override
	public String toString() {
		return this.regex;
	}
}
