package net.samism.java.percival.functions;

import net.samism.java.StringUtils.StringUtils;
import net.samism.java.percival.FunctionalMessage;
import net.samism.java.percival.PercivalBot;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/9/14
 * Time: 1:50 PM
 */
public class URLDecodeFunction extends Function {
	String encoding;

	public URLDecodeFunction(FunctionalMessage message) {
		super(message);
	}

	@Override
	public String perform() {
		try {
			return StringUtils.decodeCompletely(line, encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "Could not decode that string with " + encoding;
		}
	}

	@Override
	public boolean matches() {
		String regex = "^url-d(ecode)?((us|iso|utf)-(ascii|8859|8|16be|16le|16)(-1)?)? ([^\\r\\n\\s]+)";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(line);

		boolean match = m.find();

		if (match) {

			encoding = m.group(2) == null ? "utf-8" : m.group(2); //the encoding
			line = m.group(5);//.substring(m.group(6).lastIndexOf(" ")); //buggy solution, but works because encoded text
			//never has any whitespace
		}

		return match;
	}

	@Override
	public String getSyntax() {
		return PercivalBot.BOT_COMMAND_PREFIX + "url-e(ncode) (encoding) [text]";
	}

	@Override
	public String toString() {
		return "URL-Decode";
	}
}
