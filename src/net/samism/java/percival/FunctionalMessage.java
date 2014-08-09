package net.samism.java.percival;

import net.samism.java.StringUtils.StringUtils;
import net.samism.java.percival.functions.Function;
import net.samism.java.percival.functions.GreetFunction;
import net.samism.java.percival.functions.SyntaxFunction;
import net.samism.java.percival.functions.TranslateFunction;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.samism.java.StringUtils.StringUtils.countOccurrences;
import static net.samism.java.StringUtils.StringUtils.nthIndexOf;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 7/26/14
 * Time: 3:53 AM
 */
public class FunctionalMessage extends IRCMessage {
	private static final Logger log = LoggerFactory.getLogger(FunctionalMessage.class);

	private final Factoids facts;
	private final String function;

	private final List<Function> funcs = new ArrayList<>();

	public FunctionalMessage(String s, PercivalBot pc, Factoids facts) {
		super(s, pc);
		this.function = msg.substring(msg.indexOf(PercivalBot.BOT_COMMAND_PREFIX) + 2).trim();
		this.facts = facts;

		funcs.add(new TranslateFunction(this));
		funcs.add(new GreetFunction(this));
		funcs.add(new SyntaxFunction(this));
	}

	@Override
	public String getResponse() {
		String response = "Sorry, what?";

		switch (function) {
			//for commands that do not need an entire class
			case "exit":
				if (isFromOwner())
					Application.exit();
				response = "Only " + PercivalBot.OWNER + " can halt my execution.";
				break;
			case "hi":
			case "hello":
			case "sup":
				response = "Sup, " + author;
				break;
			case "date":
				response = "The date is: " + PercivalBot.getDate("MMM dd yyyy");
				break;
			case "owner":
				response = PercivalBot.OWNER;
				break;
			case "you":
			case "source":
				response = "My source code is at: https://github.com/samism/percival";
				break;
			case "factoids": {
				Set<String> s = facts.getTriggers();

				response = "Here are my factoids (" + s.size() + ") : ";
				response += s.toString();

				break;
			}
			default: { //handle mutli-argument functions here
//				if (function.startsWith("trans")) {
//					//check if form valid
//				String regex = "^trans(late)? ([a-z]{2}->[a-z]{2}).+";
//
//				Pattern p = Pattern.compile(regex);
//				Matcher m = p.matcher(function);
//
//				if (!m.find()) {
//					return author + ", follow this form: " + PercivalBot.BOT_COMMAND_PREFIX +
//							"trans(late) [from-language]->[to-language] [text]";
//				}
//
//					try {
//						String from = function.split(" ")[1].split("->")[0];
//						String to = function.split(" ")[1].split("->")[1];
//						String text = function.substring(nthIndexOf(function, " ", 2) + 1);
//
//						URL url = new URL("http://api.mymemory.translated.net/get?q=" + URLEncoder.encode(text, "UTF-8")
//								+ "&langpair=" + from + "|" + to);
//						JSONTokener tokener = new JSONTokener(url.openStream());
//						JSONObject object = new JSONObject(tokener);
//
//						String translation = (String) object.getJSONObject("responseData").get("translatedText");
//
//						if (translation.contains("IS AN INVALID TARGET LANGUAGE"))
//							return "Invalid target language. Please provide a valid 2 character ISO country code.";
//						if (translation.contains("PLEASE SELECT TWO DISTINCT LANGUAGES"))
//							return "Please select two distinct languages.";
//
//						response = translation;
//					} catch (IOException e) {
//						e.printStackTrace();
//						return "Something went wrong with this translation.";
//					}
//				}

//				if (function.startsWith("url-encode")) {
//					if (!function.contains(" ")) {
//						return author + ", follow this form: " + PercivalBot.BOT_COMMAND_PREFIX + "url-encode [text]";
//					}
//
//					try {
//						response = "In UTF-8: ";
//						response += URLEncoder.encode(function.substring(nthIndexOf(function, " ", 1) + 1), "UTF-8");
//					} catch (UnsupportedEncodingException e) {
//						e.printStackTrace();
//						response += "couldn't encode.";
//					}
//				}
//
//				if (function.startsWith("url-decode")) {
//					if (!function.contains(" ")) {
//						return author + ", follow this form: " + PercivalBot.BOT_COMMAND_PREFIX + "url-decode [text]";
//					}
//
//					try {
//						String s = function.substring(nthIndexOf(function, " ", 1) + 1);
//						s = s.replaceAll("(?i)%0A", "").replaceAll("(?i)%0D", ""); //rid of LF and CR, ignore-case
//
//						response = "In UTF-8: ";
//						response += StringUtils.decodeCompletely(s);
//					} catch (UnsupportedEncodingException e) {
//						e.printStackTrace();
//					} catch (IllegalArgumentException e) {
//						response += "couldn't decode. Incorrectly encoded?";
//						e.printStackTrace();
//					}
//				}
//
//				//adding/remove a factoid. command form: add/remove [trigger] [response]
//				if (function.startsWith("add")) {
//					if (countOccurrences(' ', function) < 2) { //2 or more spaces to be valid
//						return author + ", to add a factoid follow this form: " + PercivalBot.BOT_COMMAND_PREFIX +
//								"add [trigger] [response]";
//					}
//
//					String[] args = function.split(" ");
//					String t = args[1]; //trigger
//					String r = function.substring(nthIndexOf(function, " ", 2)); //response
//
//					facts.add(t, r);
//					response = author + ", I learned that.";
//				}
//
//				if (function.startsWith("remove")) {
//					if (!function.contains(" ")) {
//						return author + ", to remove a factoid follow this form: " + PercivalBot.BOT_COMMAND_PREFIX +
//								"remove [trigger]";
//					}
//
//					List<String> args = new ArrayList<>(Arrays.asList(function.split(" ")));
//					args.remove(0); //get rid of actual command
//					int num = args.size(); //initial number of factoids requested to be removed
//
//					for (String t : args) {
//						facts.remove(t);
//
//						if (!isFromOwner()) //only let me remove more than one factoid
//							break;
//					}
//
//					response = author + ", I unlearned " + ((num > 1 && isFromOwner()) ? "those" : "that") + ".";
//				}

				//traverse all subclasses of Function to find one that is compatible.
				//upon finding a match, return the result of it's perform() method
				for (Function f : funcs) {
					if (f.matches())
						response = f.perform();
				}
			}
		}

		return response;
	}

	@Override
	public String toString() {
		return "FunctionalMessage: " + msg;
	}

	public List<Function> getFunctionClasses(){
		return this.funcs;
	}
}
