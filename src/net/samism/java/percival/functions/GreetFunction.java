package net.samism.java.percival.functions;

import net.samism.java.percival.FunctionalMessage;
import net.samism.java.percival.PercivalBot;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/9/14
 * Time: 2:00 AM
 */
public class GreetFunction extends Function {
	public GreetFunction(FunctionalMessage message){
		super(message);
	}

	@Override
	public String perform() {
		return message.getAuthor() + ", how's it going????";
	}

	@Override
	public boolean matches(){
		return line.startsWith("sup bro");
	}

	@Override
	public String getSyntax() {
		return "sup bro";
	}


	@Override
	public String toString(){
		return "Greet";
	}
}
