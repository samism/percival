package net.samism.java.percival;

import net.samism.java.percival.functions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 7/26/14
 * Time: 3:53 AM
 */
public class FunctionalMessage extends IRCMessage {
	private static final Logger log = LoggerFactory.getLogger(FunctionalMessage.class);

	private final Factoids facts;
	private final String function;

	public final List<Function> functionObjects = new ArrayList<>();

	public FunctionalMessage(String s, PercivalBot pc, Factoids facts) {
		super(s, pc);
		this.function = msg.substring(msg.indexOf(PercivalBot.BOT_COMMAND_PREFIX) + 2).trim();
		this.facts = facts;

		functionObjects.add(new URLEncodeFunction(this));
		functionObjects.add(new URLDecodeFunction(this));
		functionObjects.add(new RemoveFactoidFunction(this));
		functionObjects.add(new AddFactoidFunction(this));
		functionObjects.add(new TranslateFunction(this));
		functionObjects.add(new GreetFunction(this));
		functionObjects.add(new SyntaxFunction(this));
	}

	@Override
	public String getResponse() {
		String response = "Sorry, what?";

		switch (function) {
			//for commands that do not need an entire class
			case "exit":
				if (isFromOwner())
					Application.exit();
				response = "Only " + PercivalBot.OWNER + " can halt my execution.";
				break;
			case "hi":
			case "hello":
			case "sup":
				response = "Sup, " + author;
				break;
			case "date":
				response = "The date is: " + PercivalBot.getDate("MMM dd yyyy");
				break;
			case "owner":
				response = PercivalBot.OWNER;
				break;
			case "you":
			case "source":
				response = "My source code is at: https://github.com/samism/percival";
				break;
			case "factoids": {
				Set<String> s = facts.getTriggers();

				response = "Here are my factoids (" + s.size() + ") : ";
				response += s.toString();

				break;
			}
			default: {
				//traverse all subclasses of Function to find one that is compatible.
				//upon finding a match, return the result of it's perform() method
				for (Function f : functionObjects) {
					if (f.matches())
						response = f.perform();
				}
			}
		}

		return response;
	}

	@Override
	public String toString() {
		return "FunctionalMessage: " + msg;
	}

	public List<Function> getFunctionObjects() {
		return this.functionObjects;
	}

	public Factoids getFactoidsObject() {
		return this.facts;
	}
}
