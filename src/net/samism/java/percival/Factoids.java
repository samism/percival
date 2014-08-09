package net.samism.java.percival;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
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
 * This file handles everything having to do with triggers.
 * <p/>
 * It loads (later will be able to append) triggers from a JSON file - facts.json.
 */
public final class Factoids {
	private static final Logger log = LoggerFactory.getLogger(Factoids.class);
	private static final String PATH_TO_JSON = "/Users/samism/Dropbox/programming/java/projects/IRC Bot (Percival)" +
			"/src/net/samism/java/percival/misc/factoids.json";

	private final JSONObject facts;
	private final Set<String> triggers;

	public Factoids() {
		this.facts = loadJSON(PATH_TO_JSON);
		this.triggers = facts.keySet();
	}

	public final String containsTrigger(String line) {
		line = line.toLowerCase();

		String regex = jsonToRegexString(triggers); //returns "xxx|xxx|xx|"

		Pattern req = Pattern.compile(regex);
		Matcher match = req.matcher(line);

		return match.find() ? match.group() : null;
	}

	public final String getFactoid(String command) throws JSONException {
		return (String) facts.get(command);
	}

	private JSONObject loadJSON(String file) {
		String jsonString = loadFile(file);

		JSONTokener tokener = new JSONTokener(jsonString);
		return new JSONObject(tokener);
	}

	private String loadFile(String path) {
		try {
			StringBuilder sb = new StringBuilder();
			List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);

			for (String s : lines)
				sb.append(s);

			return sb.toString();
		} catch (IOException e) {
			log.error("Error loading factoid file.");
			return "";
		}
	}

	//convert a Set of strings to a single regex String delimited with |
	private String jsonToRegexString(Set<String> s) {
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

	public final void add(String t, String r) {
		try {
			facts.put(t, r); //add pair to JSON object
			facts.write(new PrintWriter(PATH_TO_JSON, "UTF-8")).close(); //modify the file
		} catch (IOException e) {
			log.error("Problem writing JSON to file");
		}
	}

	public final void remove(String t) {
		try {
			facts.remove(t); //remove pair from JSON object
			facts.write(new PrintWriter(PATH_TO_JSON, "UTF-8")).close(); //modify the file
		} catch (IOException e) {
			log.error("Problem writing JSON to file");
		}
	}

	public Set<String> getTriggers() {
		return this.triggers;
	}
}