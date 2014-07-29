package net.samism.java.percival;

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
			"/src/net/samism/java/percival/factoids.json";

	private final JSONObject facts;
	private final Set<String> triggers;
	private final PercivalBot pc;

	public Factoids(PercivalBot pc) {
		this.pc = pc;
		this.facts = loadJSON(PATH_TO_JSON);
		this.triggers = facts.keySet();
	}

	public final boolean containsTrigger(String line) {
		//todo: allow more than one trigger for a given factoid
		String regex = jsonToRegexString(triggers); //returns "xxx|xxx|xx|"
		line = line.split("PRIVMSG " + pc.getCurrentChannelName() + " :")[1];

		Pattern req = Pattern.compile(regex);

		return req.matcher(line).find();
	}

	public final String getFactoid(String command) {
		return (String) facts.get(command);
	}

	private JSONObject loadJSON(String file) {
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

	public final void add(String t, String r) {
		facts.put(t, r); //add pair to JSON object

		try {
			facts.write(new PrintWriter(PATH_TO_JSON, "UTF-8")).close(); //modify the file
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final void remove(String t) {
		facts.remove(t);

		try {
			facts.write(new PrintWriter(PATH_TO_JSON, "UTF-8")).close(); //modify the file
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}