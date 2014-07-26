package net.samism.java.percival;

import net.samism.java.StringUtils.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 7/26/14
 * Time: 3:53 AM
 */
public class FunctionalMessage extends IRCMessage {
	private static final String trigger = "p.";

	Factoids facts;
	String function;

	public FunctionalMessage(String s, PercivalBot pc, Factoids facts) {
		super(s, pc);
		this.function = msg.split(trigger)[1];
		this.facts = facts;
	}

	@Override
	public String getResponse() {
		String response = "Sorry, what?";

		switch (function) {
			case "exit":
				PercivalBot.exit();
			case "hi":
				response = "Sup, " + author;
				break;
			case "date":
				response = "The date is: " + PercivalBot.getDate("MMM dd yyyy");
				break;
			case "owner":
				response = PercivalBot.OWNER;
				break;
			default: {
				//handle mutli-argument functions here

				//adding/remove a factoid. command form: add/remove <trigger> <response>
				if (function.startsWith("add") ||
						(function.startsWith("remove") && isFromOwner())) {
					String[] args = function.split(" ");
					String t = args[1];
					String r = function.substring(StringUtils.nthIndexOf(function, " ", 2));

					if(function.startsWith("add")) {
						facts.add(t, r);
						response = author + ", I learned that.";
					} else {
						facts.remove(t);
						response = author + ", I unlearned that.";
					}
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
