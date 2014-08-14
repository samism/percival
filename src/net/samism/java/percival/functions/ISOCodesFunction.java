package net.samism.java.percival.functions;

import net.samism.java.StringUtils.StringUtils;
import net.samism.java.percival.FunctionalMessage;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/13/14
 * Time: 9:49 PM
 */
public class ISOCodesFunction extends Function {
	public ISOCodesFunction(FunctionalMessage message){
		super(message);
	}

	@Override
	public String perform() {
		return "http://en.wikipedia.org/wiki/ISO_3166-1#Current_codes";
	}

	@Override
	public boolean matches() {
		return line.matches("iso|country-codes");
	}

	@Override
	public String getSyntax() {
		return "iso|country-codes";
	}

	@Override
	public String toString() {
		return "ISO-Codes";
	}
}
