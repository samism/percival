package net.samism.percival.functions;

import net.samism.java.StringUtils.StringUtils;
import net.samism.percival.FunctionalMessage;
import net.samism.percival.IRCRegex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static final Logger log = LoggerFactory.getLogger(URLDecodeFunction.class);

	String encoding;

	public URLDecodeFunction(FunctionalMessage message) {
		super(message);
	}

	@Override
	public String perform() {
		try {
			return StringUtils.decodeCompletely(line, encoding == null ? "utf-8" : encoding);
		} catch (UnsupportedEncodingException | IllegalArgumentException e) {
			e.printStackTrace();
			return "Could not decode that string with " + encoding;
		}
	}

	@Override
	public boolean matches() {
		IRCRegex regex = new IRCRegex("url-d(ecode)?([^\\r\\n]+)");

		Pattern p = Pattern.compile(regex.toString());
		Matcher m = p.matcher(line);

		boolean match = m.find();

		if (match) {
			String group = m.group(2).trim(); //(encoding) text
			String firstWord = group.split(" ")[0].toLowerCase(); //if the first word of the text is optional encoding

			line = group; //the text to encode

			switch (firstWord) {
				case "us-ascii":
				case "iso-8859-1":
				case "utf-16be":
				case "utf-16le":
				case "utf-16":
				case "utf-8":
					encoding = firstWord;
					line = line.replaceFirst(encoding, "").trim();
					break;
			}
		}

		return match;
	}

	@Override
	public String getSyntax() {
		return "url-e(ncode) (encoding) [text]";
	}

	@Override
	public String toString() {
		return "URL-Decode";
	}
}
