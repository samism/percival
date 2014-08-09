package net.samism.java.percival.functions;

import net.samism.java.percival.FunctionalMessage;
import net.samism.java.percival.PercivalBot;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.samism.java.StringUtils.StringUtils.nthIndexOf;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/9/14
 * Time: 12:51 PM
 */
public class URLEncodeFunction extends Function {
	String encoding;

	public URLEncodeFunction(FunctionalMessage message) {
		super(message);
	}

	@Override
	public String perform() {
		try {
			return URLEncoder.encode(line.substring(nthIndexOf(line, " ", 1) + 1), encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "Could not encode that string with " + encoding;
		}
	}

	@Override
	public boolean matches() {
		String regex = "^url-e(ncode)?((us|iso|utf)-(ascii|8859|8|16be|16le|16)(-1)?)? ([^\\r\\n]+)";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(line);

		boolean match = m.find();

		if (match) {
			encoding = m.group(2) == null ? "utf-8" : m.group(2); //the encoding
			line = m.group(6); //the text to encode
		}

		return match;
	}

	@Override
	public String getSyntax() {
		return PercivalBot.BOT_COMMAND_PREFIX + "url-e(ncode) (encoding) [text]";
	}

	@Override
	public String toString() {
		return "URL-Encode";
	}
}
