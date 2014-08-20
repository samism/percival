package net.samism.java.percival.functions;

import net.samism.java.percival.FunctionalMessage;
import net.samism.java.percival.IRCRegex;
import net.samism.java.percival.PercivalBot;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/9/14
 * Time: 2:23 AM
 */
public class SyntaxFunction extends Function {
	public SyntaxFunction(FunctionalMessage message) {
		super(message);
	}

	@Override
	public String perform() {
		Map<String, String> table = new LinkedHashMap<>();
		for (Function f : message.getFunctionObjects())
			table.put(f.toString(), PercivalBot.BOT_COMMAND_PREFIX + f.getSyntax());

		return table.toString();
	}

	@Override
	public boolean matches() {
		IRCRegex regex = new IRCRegex("\\?");

		Pattern p = Pattern.compile(regex.toString());
		Matcher m = p.matcher(line);

		return m.find();
	}

	@Override
	public String getSyntax() {
		return "?";
	}

	@Override
	public String toString() {
		return "Syntax";
	}
}