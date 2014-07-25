package net.samism.java.percival;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 7/25/14
 * Time: 12:39 AM
 * <p/>
 * This file handles everything having to do with commands.
 * <p/>
 * It loads (later will be able to append) commands from a JSON file - responses.json.
 */
public class Commands {
	private static final Logger log = LoggerFactory.getLogger(Commands.class);
	private static JSONObject responses = loadCommands("/Users/samism/Dropbox/programming/java/projects/IRC Bot (Percival)" +
			"/src/net/samism/java/percival/responses.json");
	private static Set<String> commands = responses.keySet();

	public Commands() {
	}

	public static boolean containsCommand(String line) {
		String regex = "^(perc(ival|y)(,|:))\\s?(" + commandsToRegexString(commands) + ")";
		Pattern req = Pattern.compile(regex);
		Matcher m = req.matcher(line.substring(line.lastIndexOf(':') + 1));

		return (req.matcher(line).find());
	}

	private static JSONObject loadCommands(String file) {
		String jsonString = loadFile(file);

		JSONTokener tokener = new JSONTokener(jsonString);
		return (JSONObject) new JSONObject(tokener).get("commands");
	}

	private static String loadFile(String path) {
		StringBuilder sb = new StringBuilder();
		List<String> lines = null;

		try {
			lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (lines != null) {
			for (String s : lines)
				sb.append(s);
		}

		return sb.toString();
	}

	//convert a Set of strings to a single regex String delimited with |
	private static String commandsToRegexString(Set<String> s) {
		String[] array = s.toArray(new String[s.size()]);
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < array.length; i++) {
			if (i == (array.length - 1))
				sb.append(array[i]);
			else
				sb.append(array[i]).append("|");
		}

		return sb.toString();
	}

	public String getStaticResponse(String command) {
		return (String) responses.get(command);
	}
}