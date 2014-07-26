package net.samism.java.percival;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 7/26/14
 * Time: 3:53 AM
 */
public class FunctionalMessage extends IRCMessage {
	private static final String trigger = "p.";

	String function, recipient;

	public FunctionalMessage(String s, PercivalBot pc) {
		super(s, pc);
		this.recipient = rawMsg.substring(1).split("!")[0];
		this.function = msg.split(trigger)[1];
	}

	@Override
	public String getResponse() {
		String response = "Sorry, what?";

		switch(function){
			case "hi":
				response = "Sup, " + recipient;
				break;
			case "date":
				response = "The date is: " + PercivalBot.getDate("MMM dd yyyy");
				break;
			case "owner":
				response = PercivalBot.OWNER;
				break;
			//no need for a default case
		}

		return response;
	}

	@Override
	public String toString() {
		return "FunctionalMessage: " + msg;
	}
}
