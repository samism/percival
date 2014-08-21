package net.samism.java.percival;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
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
	private static final String PATH_TO_JSON = "misc/factoids.json";
	private File FACTOIDS_JSON_FILE;

	private final JSONObject facts = loadJSON(PATH_TO_JSON);
	private final Set<String> triggers = facts.keySet();

	public Factoids() {
		try {
			FACTOIDS_JSON_FILE = new File(getClass().getResource(PATH_TO_JSON).toURI());
		} catch (URISyntaxException e) {
			log.error("Relative path to factoid JSON file has a syntax error: " + FACTOIDS_JSON_FILE.toString());
			e.printStackTrace();
			String path = System.getProperty("user.dir") + "out/net/samism/java/percvial/" + PATH_TO_JSON;
			FACTOIDS_JSON_FILE = new File(path);
			log.error("Resorting to absolute path: " + path);
		}
	}

	public final String containsTrigger(String line) {
		line = line.toLowerCase().trim();

		String regex = jsonToRegexString(triggers); //returns "xxx|xxx|xx|"

		Pattern req = Pattern.compile(regex);
		Matcher match = req.matcher(line);

		if (match.find()) {
			//only match if:
			//1. the entire string was the trigger
			if (match.group().length() == line.length()) {
				return match.group();
			}

			//2. If there was a whitespace character after the word (indicating that it was a standalone word)
			//3. If there was some other character after the word that was not a letter or number (punctuation)
			if (match.end() < line.length()) { //prevent iobe
				if ((Character.isWhitespace(line.charAt(match.end())) ||
						!Character.isLetterOrDigit(line.charAt(match.end())))) {
					return match.group();
				}
			}
		}

		return null;
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
		try (InputStream is = getClass().getResourceAsStream(path);
			 InputStreamReader ir = new InputStreamReader(is, "UTF-8");
			 BufferedReader br = new BufferedReader(ir)) {

			StringBuilder sb = new StringBuilder();

			for (String line = br.readLine(); line != null; line = br.readLine()) {
				sb.append(line);
			}

			return sb.toString();
		} catch (IOException e) {
			log.error("Error loading factoid file.");
			return "";
		}
	}

	//convert a Set of strings to a single regex String delimited with |
	private String jsonToRegexString(Set<String> s) {
		String[] array = s.toArray(new String[s.size()]);
		Pattern p = Pattern.compile("([\\-\\[\\]\\/\\{\\}\\(\\)\\*\\+\\?\\.\\\\\\^\\$\\|])");
		Matcher m;

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < array.length; i++) {
			m = p.matcher(array[i]);

			while (m.find()) {
				array[i] = m.replaceAll("\\\\$1"); //escape all regex chars
			}

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
			facts.write(new PrintWriter(FACTOIDS_JSON_FILE, "UTF-8")).close(); //modify the file
		} catch (IOException e) {
			log.error("Problem writing JSON to file.");
			log.error("JSON file attempted at path: " + FACTOIDS_JSON_FILE.toString());
		}
	}

	public final void remove(String t) {
		try {
			facts.remove(t); //remove pair from JSON object
			facts.write(new PrintWriter(FACTOIDS_JSON_FILE, "UTF-8")).close(); //modify the file
		} catch (IOException e) {
			log.error("Problem writing JSON to file.");
			log.error("JSON file attempted at path: " + FACTOIDS_JSON_FILE.toString());
		}
	}

	public Set<String> getTriggers() {
		return this.triggers;
	}
}