package net.samism.percival;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/23/11
 * Time: 10:07 PM
 */

public class FactoidMessage extends IRCMessage {
	private static final Logger log = LoggerFactory.getLogger(FactoidMessage.class);
	private FactoidsJDBC facts;
	private String trigger;

	public FactoidMessage(String s, String trigger, PercivalBot pc) {
		super(s, pc);
		this.facts = pc.getFactoidsObject();
		this.trigger = trigger;
	}

	@Override
	public String getResponse() {
		return facts.select(trigger).getResponse();
	}

	@Override
	public String toString() {
		return "FactoidMessage: " + msg;
	}
}
