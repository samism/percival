package net.samism.percival;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/29/2014
 * Time: 4:05 AM
 * <p>
 * Percival is an IRC bot, made from scratch.
 */
public final class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	private static final List<PercivalBot> bots = new ArrayList<>();

	public static final String FACTOIDS_DB_URL;
	public static final String FACTOIDS_DB_USERNAME;
	public static final String FACTOIDS_DB_PASSWORD;

	public static final String IDENT_PASS;

	static {
		//load credentials for jdbc & ident

		Properties props = new Properties();

		try {
			props.load(Application.class.getResourceAsStream("misc/percival.properties"));

		} catch (IOException e) {
			e.printStackTrace();
			log.error("Could not load properties file. Closing.");
			Application.exit();
		}

		FACTOIDS_DB_URL = props.getProperty("url");
		FACTOIDS_DB_USERNAME = props.getProperty("user");
		FACTOIDS_DB_PASSWORD = props.getProperty("pass");
		IDENT_PASS = props.getProperty("ident");
	}

	public static void main(String[] args) {
		new Application();
	}

	public Application() {
		try {
			//initialize a bot for each network we're connecting to
			bots.add(new PercivalBot("irc.foonetic.net",
					new String[]{"#test"/*, "#lingubender", "#ministryofsillywalks"*/},
					6667));
//			bots.add(new PercivalBot("irc.awfulnet.org",
//					new String[]{"#programming", "#fitness"},
//					6667));
//			bots.add(new PercivalBot("irc.freenode.net",
// 					new String[]{"##java, #git, #regex, #znc"},
// 					6667));
//			bots.add(new PercivalBot("irc.strictfp.com",
// 					new String[]{"#rscode"},
//					6667));
//
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void exit() {
		bots.forEach(Application::destroyBot);

		System.exit(0);
	}

	public static void destroyBot(PercivalBot bot) {
		try {
			bot.leaveAllChannels();
			bot.setShouldDie(true);
		} catch (IOException e) {
			log.error("Couldn't leave all channels for the bot connected to: " + bot.getServerName());
			e.printStackTrace();
		}
	}
}