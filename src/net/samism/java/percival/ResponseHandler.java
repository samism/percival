package net.samism.java.percival;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: Unknown
 * Time: Unknown
 */
public class ResponseHandler {

	private final PercivalBot pc;

	ResponseHandler(PercivalBot pc) {
		this.pc = pc;
	}

	public String getResponse(IRCMessage msg) {
		String line = msg.getMsg();
		String text = !(msg instanceof PingMessage) ? line.split(getPrefix(line))[1].trim() : line;

		System.out.println("line = " + line + "\ntext = " + text);

		StringBuilder response = new StringBuilder();

		//time, owner, exit, help, hi
		if (msg instanceof PingMessage) {
			/*
				PingMessage:
			 */
			response.append(line.contains(":")
					? "PONG " + line.split(" ")[1]
					: "PONG " + line.split(":")[1]);
		} else if (msg instanceof CommandMessage) {
			/*
				CommandMessage:
			 */
			if (msg.isFromOwner() && (text.equalsIgnoreCase("show that you are mine!")
					|| (text.equalsIgnoreCase("owner")))) {
				response.append("I was written (in Java) by: " + IRCBot.OWNER);
			} else if (text.equalsIgnoreCase("hi")) {
				response.append("Hey there ").append(msg.getAuthor());
			} else if (text.equalsIgnoreCase("time")) {
				DateFormat dateFormat = new SimpleDateFormat("h:mm a");
				Date date = new Date();
				response.append("It's ").append(dateFormat.format(date)).append(" where I'm at.");
			} else if (text.equalsIgnoreCase("help")) {
				try {
					pc.sendChan("Hi, I'm Percival. You can call me Percy if you like.");
					pc.sendChan("My current commands are {owner,hi,time,help}. Oh, and \"exit\" can be used by the admins and ffs.");
					pc.sendChan("I was written in Java by ffs82defxp.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if ((msg.isFromOwner() || (msg.isFrom("Kris") || msg.isFrom("Tan") || msg.isFrom("Joey"))
					&& text.equalsIgnoreCase("exit"))) {
				pc.logConsole("Exit requested by: " + msg.getAuthor());
				System.exit(1);
			} else {
				response.append("Invalid command");
			}
		}

		pc.logConsole("<<< " + response.toString());
		return response.toString();
	}

	private String getPrefix(String s) {
		s = s.toLowerCase();

		String prefix = null;
		String[] prefixes = new String[]{
				"percy,", "percival,", "percy:", "percival:"
		};

		for (String str : prefixes) {
			if (s.startsWith(str)) {
				prefix = str;
			}
		}

		return prefix;
	}
}