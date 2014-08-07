package net.samism.java.percival;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static net.samism.java.StringUtils.StringUtils.nthIndexOf;
import static net.samism.java.StringUtils.StringUtils.countOccurrences;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 7/26/14
 * Time: 3:53 AM
 */
public class FunctionalMessage extends IRCMessage {
	private static final Logger log = LoggerFactory.getLogger(FunctionalMessage.class);

	final Factoids facts;
	final String function;

	public FunctionalMessage(String s, PercivalBot pc, Factoids facts) {
		super(s, pc);
		this.function = msg.split(PercivalBot.TRIGGER)[1].trim();
		this.facts = facts;
	}

	@Override
	public String getResponse() {
		String response = "Sorry, what?";

		switch (function) {
			//todo: Make each case match a class rather than a single String
			case "exit":
				Application.exit();
			case "hi":
				response = "Sup, " + author;
				break;
			case "date":
				response = "The date is: " + PercivalBot.getDate("MMM dd yyyy");
				break;
			case "owner":
				response = PercivalBot.OWNER;
				break;
			case "factoids": {
				Set<String> s = facts.getTriggers();

				response = "Here are my factoids: ";
				response += s.toString();

				break;
			}
			default: {
				//handle mutli-argument functions here

				//adding/remove a factoid. command form: add/remove [trigger] [response]
				if (function.startsWith("add")){
					System.out.println(countOccurrences(' ', function));
					if (countOccurrences(' ', function) != 2) { //exactly 2 spaces
						return author + ", to add a factoid follow this form: " + PercivalBot.TRIGGER +
								"add [trigger] [response]";
					}

					String[] args = function.split(" ");
					String t = args[1]; //trigger
					String r = function.substring(nthIndexOf(function, " ", 2)); //response

					facts.add(t, r);
					response = author + ", I learned that.";
				}

				if(function.startsWith("remove") && isFromOwner()){
					if(!function.contains(" ")) {
						return author + ", to remove a factoid follow this form: " + PercivalBot.TRIGGER +
								"remove [trigger]";
					}
					String[] args = function.split(" ");
					String t = args[1]; //trigger

					facts.remove(t);
					response = author + ", I unlearned that.";
				}
			}
		}

		return response;
	}

	@Override
	public String toString() {
		return "FunctionalMessage: " + msg;
	}
}
