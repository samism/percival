package net.samism.java.percival;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 7/29/2014
 * Time: 4:05 AM
 * <p/>
 * Percival is an IRC bot, made from scratch.
 */
public final class Application {
	static final ArrayList<PercivalBot> botInstances;

	static {
		botInstances = new ArrayList<>();

		try {
			botInstances.add(new PercivalBot("Percival", "irc.foonetic.net", new String[]{"test"}, 6667));
			botInstances.add(new PercivalBot("Percival", "irc.awfulnet.org", new String[]{"programming"}, 6667));
			botInstances.add(new PercivalBot("Percival", "irc.freenode.net", new String[]{"##java"}, 6667));
			botInstances.add(new PercivalBot("Percival", "irc.strictfp.com", new String[]{"rscode"}, 6667));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(int[] args) throws IOException {
		botInstances.forEach(PercivalBot::connect); //join all servers
	}

	public final static void exit() {
		System.exit(0);
	}
}