package net.samism.java.percival;

import net.samism.java.percival.util.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/21/14
 * Time: 10:34 PM
 */
public final class FactoidsJDBC {
	private static final Logger log = LoggerFactory.getLogger(FactoidsJDBC.class);

	private static Connection db_conn;

	private final String FACTOIDS_DB_URL = "jdbc:mysql://samism.net:3306/percival";
	private final String FACTOIDS_DB_USERNAME;
	private final String FACTOIDS_DB_PASSWORD;

	private static final String INSERT_QUERY = "INSERT INTO factoids (id, date_created, author, hook, response)" +
			"VALUES (?,?,?,?,?)";
	private static final String DELETE_QUERY = "DELETE FROM factoids WHERE hook='~'";
	private static final String SELECT_QUERY = "SELECT hook,date_created,author,response FROM factoids WHERE hook='~'";
	private static final String SELECT_TRIGGERS_QUERY = "SELECT hook FROM factoids";
	//private static final String SELECT_ALL_QUERY = "SELECT * FROM factoids"; //probably for use with tests...

	private ArrayList<String> triggers = new ArrayList<>();

	FactoidsJDBC() { //default modifier! more protection!
		String config = File.loadText(getClass(), PercivalBot.CONFIG_FILE_PATH); //load sensitive info externally
		FACTOIDS_DB_USERNAME = config.split(" ")[1];
		FACTOIDS_DB_PASSWORD = config.split(" ")[2];

		connect();
		loadTriggers();
	}

	public final void insert(Factoid f) {
		try (PreparedStatement s = db_conn.prepareStatement(INSERT_QUERY)) {
			s.setNull(1, Types.INTEGER);
			s.setDate(2, f.getDateCreated());
			s.setString(3, f.getAuthor());
			s.setString(4, f.getTrigger());
			s.setString(5, f.getResponse());

			s.executeUpdate();

			log.info("Factoid for '" + f.getTrigger() + "' added to table.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		loadTriggers();
	}

	public final Factoid select(String hook) {
		String query = SELECT_QUERY.replace("~", hook);

		try (Statement s = db_conn.createStatement();
			 ResultSet results = s.executeQuery(query)) {

			if (results.next()) {
				return new Factoid(results.getString("author"),
						results.getString("hook"),
						results.getString("response"),
						results.getDate("date_created"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new Factoid(); //if something went wrong
		}
		return new Factoid(); //if was not in table (shouldn't get here)
	}

	public final void delete(String hook) {
		String query = DELETE_QUERY.replace("~", hook);

		try (Statement s = db_conn.createStatement()) {
			s.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		loadTriggers();
		log.info("Factoid for '" + hook + "' removed from table.");
	}

	private void loadTriggers() {
		try (Statement s = db_conn.createStatement();
			 ResultSet results = s.executeQuery(SELECT_TRIGGERS_QUERY)) { //query db for all rows' value for `hook`

			triggers.clear();

			while (results.next()) {
				triggers.add(results.getString("hook"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String aggregateToRegex() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < triggers.size(); i++) { //convert arraylist to a regex string
			if (i == triggers.size() - 1)
				sb.append(triggers.get(i));
			else
				sb.append(triggers.get(i)).append("|");
		}

		return sb.toString();
	}

	public final String containsTrigger(String line) {
		line = line.toLowerCase().trim();

		String regex = aggregateToRegex(); //returns "xxx|xxx|xx|"

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

	private void connect() {
		try {
			db_conn = DriverManager.getConnection(FACTOIDS_DB_URL, FACTOIDS_DB_USERNAME, FACTOIDS_DB_PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getTriggers() {
		return this.triggers;
	}
}