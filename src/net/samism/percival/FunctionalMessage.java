package net.samism.percival;

import net.samism.percival.functions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 7/26/14
 * Time: 3:53 AM
 */
public class FunctionalMessage extends IRCMessage {
	private static final Logger log = LoggerFactory.getLogger(FunctionalMessage.class);

	private final FactoidsJDBC facts;

	public final List<Function> functionObjects = new ArrayList<>();

	public FunctionalMessage(String s, PercivalBot pc, FactoidsJDBC facts) {
		super(s, pc);
		this.facts = facts;

		functionObjects.add(new FactoidsFunction(this));
		functionObjects.add(new GithubFunction(this));
		functionObjects.add(new OwnerFunction(this));
		functionObjects.add(new DateFunction(this));
		functionObjects.add(new ExitFunction(this));
		functionObjects.add(new ISOCodesFunction(this));
		functionObjects.add(new URLEncodeFunction(this));
		functionObjects.add(new URLDecodeFunction(this));
		functionObjects.add(new AddFactoidFunction(this, author));
		functionObjects.add(new RemoveFactoidFunction(this));
		functionObjects.add(new TranslateFunction(this));
		functionObjects.add(new GreetFunction(this));
		functionObjects.add(new SyntaxFunction(this));
	}

	@Override
	public String getResponse() {
		String response = "Sorry, what?";

		//traverse all subclasses of Function to find one that is a match.
		//upon finding a match, return the result of it's perform() method
		for (Function f : functionObjects) {
			if (f.matches())
				response = f.perform();
		}

		return response;
	}

	@Override
	public String toString() {
		return "FunctionalMessage: " + msg;
	}

	public List<Function> getFunctionObjects() {
		return this.functionObjects;
	}

	public FactoidsJDBC getFactoidsObject() {
		return this.facts;
	}
}
