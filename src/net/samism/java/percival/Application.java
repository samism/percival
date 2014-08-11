package net.samism.java.percival;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/29/2014
 * Time: 4:05 AM
 * <p/>
 * Percival is an IRC bot, made from scratch.
 */
public final class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	private static final List<PercivalBot> botInstances = new ArrayList<>();

	public static void main(String[] args) {
		new Application();
	}

	public Application() {
		try {
			botInstances.add(new PercivalBot("irc.foonetic.net", new String[]{"#test", "#lingubender"}, 6667));
//			botInstances.add(new PercivalBot("irc.awfulnet.org", new String[]{"#programming, #fitness"}, 6667));
//			botInstances.add(new PercivalBot("irc.freenode.net", new String[]{"##java"}, 6667));
//			botInstances.add(new PercivalBot("irc.strictfp.com", new String[]{"#rscode"}, 6667));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void exit() {
		for (PercivalBot bot : botInstances) {
			destroyBot(bot);
		}

		System.exit(0);
	}

	public static void destroyBot(PercivalBot bot) {
		try {
			bot.leaveAllChannels();
			bot.getConnection().join();
			botInstances.remove(bot);
		} catch (IOException e) {
			log.error("Couldn't leave all channels for the bot connected to: " + bot.getServerName());
			e.printStackTrace();
		} catch (InterruptedException e) {
			bot.getConnection().interrupt();
			log.error("Interrupting thread");
			e.printStackTrace();
		}
	}

}