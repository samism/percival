package net.samism.java.percival.functions;

import net.samism.java.StringUtils.StringUtils;
import net.samism.java.percival.FunctionalMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/9/14
 * Time: 12:40 PM
 */
public class AddFactoidFunction extends Function {
	public AddFactoidFunction(FunctionalMessage message) {
		super(message);
	}

	@Override
	public String perform() {
		String[] args = line.split(" ");
		String t = args[1];
		String r = line.substring(StringUtils.nthIndexOf(line, " ", 2)); //response

		message.getFactoidsObject().add(t, r);
		return message.getAuthor() + ", I learned that.";
	}

	@Override
	public boolean matches() {
		String regex = "^add [^\\r\\n]+ [^\\r\\n]+";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(line);

		return m.find();
	}

	@Override
	public String getSyntax() {
		return "add [trigger] [response]";
	}

	@Override
	public String toString() {
		return "Add-Factoid";
	}
}
