package net.samism.java.percival;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
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
	private JSONObject responses;
	private static Set<String> commands;

	public Commands(String file) {
		responses = loadCommands(file);
		commands = responses.keySet();
	}

	public static boolean containsCommand(String line) {
		String regex = "^(perc(ival|y)(,|:))\\s?(" + commandsToRegexString(commands) + ")";
		Pattern req = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

		return (req.matcher(line).find());
	}

	private JSONObject loadCommands(String file) {
		String jsonString = loadFile(file);

		JSONTokener tokener = new JSONTokener(jsonString);
		return new JSONObject(tokener);
	}

	private String loadFile(String path) {
		StringBuilder sb = new StringBuilder();
		List<String> lines = null;

		try {
			lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (lines != null) {
			lines.forEach(sb::append); //cool code right here
		}

		return sb.toString();
	}

	//convert a Set of strings to a single regex String
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

	public String getResponse(String command) {
		return (String) responses.get(command);
	}

	public Set<String> getCommands() {
		return commands;
	}
}
