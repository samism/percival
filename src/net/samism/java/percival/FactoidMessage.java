package net.samism.java.percival;

import org.json.JSONException;
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
	private Factoids facts;
	private String trigger;

	public FactoidMessage(String s, String trigger, PercivalBot pc, Factoids facts) {
		super(s, pc);
		this.facts = facts;
		this.trigger = trigger;

		log.info(trigger);
	}

	@Override
	public String getResponse() {
		try {
			return facts.getFactoid(trigger);
		} catch (JSONException e) {
			log.info("Problem finding the JSON entry for: '" + trigger + "'");
			return "Problem finding the JSON entry for: '" + trigger + "'";
		}
	}

	@Override
	public String toString() {
		return "FactoidMessage: " + msg;
	}
}
