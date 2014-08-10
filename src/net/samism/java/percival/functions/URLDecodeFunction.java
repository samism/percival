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
			return StringUtils.decodeCompletely(line, encoding == null ? "utf-8" : encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "Could not decode that string with " + encoding;
		}
	}

	@Override
	public boolean matches() {
		String regex = "^url-d(ecode)?([^\\r\\n]+)";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(line);

		boolean match = m.find();

		if (match) {
			String s = line.split(" ")[1].toLowerCase(); //if the first word of the text happens to be an encoding

			line = m.group(2).trim(); //the text to encode

			switch (s) {
				case "us-ascii":
				case "iso-8859-1":
				case "utf-16be":
				case "utf-16le":
				case "utf-16":
				case "utf-8":
					encoding = s;
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
