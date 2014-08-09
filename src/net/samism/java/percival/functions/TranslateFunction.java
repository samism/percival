package net.samism.java.percival.functions;

import net.samism.java.percival.FunctionalMessage;
import net.samism.java.percival.PercivalBot;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.samism.java.StringUtils.StringUtils.nthIndexOf;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/9/14
 * Time: 1:40 AM
 */
public class TranslateFunction extends Function {
	public TranslateFunction(FunctionalMessage message){
		super(message);
	}

	@Override
	public boolean matches(){
		String regex = "^trans(late)? ([a-z]{2}->[a-z]{2}).+";

		p = Pattern.compile(regex);
		m = p.matcher(line);

		return m.find();
	}

	@Override
	public String getSyntax() {
		return PercivalBot.BOT_COMMAND_PREFIX + "trans(late) [from-language]->[to-language] [text]";
	}

	@Override
	public String perform() {
		String response;

		try {
			String from = line.split(" ")[1].split("->")[0];
			String to = line.split(" ")[1].split("->")[1];
			String text = line.substring(nthIndexOf(line, " ", 2) + 1);

			URL url = new URL("http://api.mymemory.translated.net/get?q=" + URLEncoder.encode(text, "UTF-8")
					+ "&langpair=" + from + "|" + to);
			JSONTokener tokener = new JSONTokener(url.openStream());
			JSONObject object = new JSONObject(tokener);

			String translation = (String) object.getJSONObject("responseData").get("translatedText");

			if (translation.contains("IS AN INVALID TARGET LANGUAGE"))
				return "Invalid target language. Please provide a valid 2 character ISO country code.";
			if (translation.contains("PLEASE SELECT TWO DISTINCT LANGUAGES"))
				return "Please select two distinct languages.";

			response = translation;
		} catch (IOException e) {
			e.printStackTrace();
			return "Something went wrong with this translation.";
		}

		return response;
	}

	@Override
	public String toString(){
		return "Translate";
	}
}
