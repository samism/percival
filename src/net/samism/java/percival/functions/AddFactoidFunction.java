package net.samism.java.percival.functions;

import net.samism.java.StringUtils.StringUtils;
import net.samism.java.percival.Factoid;
import net.samism.java.percival.FunctionalMessage;
import net.samism.java.percival.IRCRegex;
import net.samism.java.percival.util.Date;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/9/14
 * Time: 12:40 PM
 */
public class AddFactoidFunction extends Function {
	private String author;

	public AddFactoidFunction(FunctionalMessage message, String author) {
		super(message);
		this.author = author;
	}

	@Override
	public String perform() {
		String[] args = line.split(" ");

		String t = args[1];
		String r = line.substring(StringUtils.nthIndexOf(line, " ", 2)); //response

		Factoid f = new Factoid(this.author, t, r, Date.today());

		message.getFactoidsObject().insert(f);

		return message.getAuthor() + ", I learned that.";
	}

	@Override
	public boolean matches() {
		IRCRegex regex = new IRCRegex("add [^\\r\\n]+ [^\\r\\n]+");

		Pattern p = Pattern.compile(regex.toString());
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
