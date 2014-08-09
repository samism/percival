package net.samism.java.percival.functions;

import net.samism.java.percival.FunctionalMessage;
import net.samism.java.percival.PercivalBot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/9/14
 * Time: 2:23 AM
 */
public class SyntaxFunction extends Function {
	public SyntaxFunction(FunctionalMessage message){
		super(message);
	}

	@Override
	public String getSyntax() {
		return PercivalBot.BOT_COMMAND_PREFIX + "?";
	}

	@Override
	public boolean matches() {
		String regex = "^?$";

		p = Pattern.compile(regex);
		m = p.matcher(line);

		System.out.println(line);
		return m.find();
	}

	@Override
	public String perform() {
		Map<String, String> table = new HashMap<>();
		for(Function f : message.getFunctionClasses())
			table.put(f.toString(), f.getSyntax());

		return table.toString();
	}

	@Override
	public String toString(){
		return "Syntax";
	}
}
