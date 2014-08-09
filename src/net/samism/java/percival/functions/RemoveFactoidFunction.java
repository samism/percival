package net.samism.java.percival.functions;

import net.samism.java.percival.FunctionalMessage;
import net.samism.java.percival.PercivalBot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/9/14
 * Time: 12:46 PM
 */
public class RemoveFactoidFunction extends Function {
	public RemoveFactoidFunction(FunctionalMessage message) {
		super(message);
	}

	@Override
	public String perform() {
		List<String> args = new ArrayList<>(Arrays.asList(line.split(" ")));
		args.remove(0); //get rid of actual command
		int num = args.size(); //initial number of factoids requested to be removed

		for (String t : args) {
			message.getFactoidsObject().remove(t);

			if (!message.isFromOwner()) //only let me remove more than one factoid
				break;
		}

		return message.getAuthor() + ", I unlearned " + ((num > 1 && message.isFromOwner()) ? "those" : "that") + ".";
	}

	@Override
	public boolean matches() {
		String regex = "^remove [^\\r\\n]+";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(line);

		return m.find();
	}

	@Override
	public String getSyntax() {
		return PercivalBot.BOT_COMMAND_PREFIX + "remove [trigger]";
	}

	@Override
	public String toString() {
		return "Remove-Factoid";
	}
}
