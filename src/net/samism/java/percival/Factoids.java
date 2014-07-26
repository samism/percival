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
public class Factoids {
	private final Logger log = LoggerFactory.getLogger(Factoids.class);

	private JSONObject facts;
	private Set<String> triggers;
	private PercivalBot pc;

	public Factoids(PercivalBot pc) {
		this.pc = pc;
		facts = loadJSON("/Users/samism/Dropbox/programming/java/projects/IRC Bot (Percival)" +
				"/src/net/samism/java/percival/facts.json");
		triggers = facts.keySet();
	}

	public boolean containsTrigger(String line) {
		String regex = "^(perc(ival|y)(,|:))\\s?(" + jsonToRegexString(triggers) + ")";
		line = line.split("PRIVMSG " + pc.getChannelName() + " :")[1];

		Pattern req = Pattern.compile(regex);

		return req.matcher(line).find();
	}

	public String getFactoid(String command) {
		return (String) facts.get(command);
	}

	private JSONObject loadJSON(String file) {
		String jsonString = loadFile(file);

		JSONTokener tokener = new JSONTokener(jsonString);
		return (JSONObject) new JSONObject(tokener).get("triggers");
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
			for (String s : lines)
				sb.append(s);
		}

		return sb.toString();
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
}