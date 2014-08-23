package net.samism.percival.functions;

import net.samism.percival.FunctionalMessage;
import net.samism.percival.IRCRegex;

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

		ArrayList<String> triggers = message.getFactoidsObject().getTriggers();
		for (String t : args) {
			if(triggers.contains(t))
				message.getFactoidsObject().delete(t);

			if (!message.isFromOwner()) //only let me remove more than one factoid
				break;
		}

		return message.getAuthor() + ", I unlearned " + ((num > 1 && message.isFromOwner()) ? "those" : "that") + ".";
	}

	@Override
	public boolean matches() {
		IRCRegex regex = new IRCRegex("remove [^\\r\\n]+");

		Pattern p = Pattern.compile(regex.toString());
		Matcher m = p.matcher(line);

		return m.find();
	}

	@Override
	public String getSyntax() {
		return "remove [trigger 1] [trigger 2] ...";
	}

	@Override
	public String toString() {
		return "Remove-Factoid";
	}
}
