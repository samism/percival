package net.samism.percival;

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

	private static Connection conn;

	private static final String INSERT_QUERY =
			"INSERT INTO factoids (id, date_created, author, hook, response) VALUES (?,?,?,?,?)";
	private static final String DELETE_QUERY = "DELETE FROM factoids WHERE hook=?";
	private static final String SELECT_QUERY = "SELECT hook,date_created,author,response FROM factoids WHERE hook=?";
	private static final String SELECT_TRIGGERS_QUERY = "SELECT hook FROM factoids";

	private ArrayList<String> triggers = new ArrayList<>();

	FactoidsJDBC() { //default modifier! more protection!
		try {
			conn = DriverManager.getConnection(
					Application.FACTOIDS_DB_URL,
					Application.FACTOIDS_DB_USERNAME,
					Application.FACTOIDS_DB_PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		loadTriggers();
	}

	public final void insert(Factoid f) {
		try (PreparedStatement statement = conn.prepareStatement(INSERT_QUERY)) {
			statement.setNull(1, Types.INTEGER);
			statement.setDate(2, f.getDateCreated());
			statement.setString(3, f.getAuthor());
			statement.setString(4, f.getTrigger());
			statement.setString(5, f.getResponse());

			statement.executeUpdate();

			log.info("Factoid for '{}' added to table.", f.getTrigger());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		loadTriggers();
	}

	public final Factoid select(String hook) {
		try (PreparedStatement statement = conn.prepareStatement(SELECT_QUERY)) {
			statement.setString(1, hook); //prevent security issues with sql

			try (ResultSet results = statement.executeQuery()) {
				if (results.next()) {
					return new Factoid(results.getString("author"),
							results.getString("hook"),
							results.getString("response"),
							results.getDate("date_created"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new Factoid(); //if something went wrong
		}
		return new Factoid(); //if was not in table (shouldn't get here)
	}

	public final void delete(String hook) {
		try (PreparedStatement statement = conn.prepareStatement(DELETE_QUERY)) {
			statement.setString(1, hook);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		loadTriggers();
		log.info("Factoid for '{}' removed from table.", hook);
	}

	private void loadTriggers() {
		try (Statement s = conn.createStatement(); //no PreparedStatement needed bc the query is hardcoded by me
			 ResultSet results = s.executeQuery(SELECT_TRIGGERS_QUERY)) { //query db for all rows' value for `hook`

			if (triggers.size() > 0)
				triggers.clear(); //out with the old, in with the new

			while (results.next()) {
				triggers.add(results.getString("hook"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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

		return "";
	}

	private String aggregateToRegex() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < triggers.size(); i++) { //convert arraylist to a regex-formatted string
			if (i == triggers.size() - 1)
				sb.append(triggers.get(i));
			else
				sb.append(triggers.get(i)).append("|");
		}

		return sb.toString();
	}

	public ArrayList<String> getTriggers() {
		return this.triggers;
	}
}