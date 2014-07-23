package net.samism.java.percival;

import net.samism.java.percival.PercivalBot;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: Unknown
 * Time: Unknown
 * <p/>
 * Percival is an IRC bot, made from scratch.
 * <p/>
 * TODO:
 * <p/>
 * 1. Fix design, may be redundant
 * 2. Implement the use of CasualMessage
 * 3. Make command list dynamic
 * 4. Actually make a useful command
 * 5. Make file logging after Percival is stable
 * 6. Make IRC client after happy with Percival
 */
public class Application {
	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			new PercivalBot("Percival", "irc.foonetic.net", "test", 6667);
		} else {
			int arg = Integer.parseInt(args[0]);

			switch (arg) {
				case 0:
					new PercivalBot("Percival", "irc.foonetic.net", "lingubender", 6667);
					break;
				case 1:
					new PercivalBot("Percival", "irc.strictfp.com", "rscode", 6667);
					break;
				default:
					System.out.println("wierd..");
					break;
			}
		}
	}
}