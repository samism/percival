package net.samism.java.percival.functions;

import net.samism.java.percival.FunctionalMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/19/14
 * Time: 2:14 AM
 */
public class DateFunction extends Function {
	public DateFunction(FunctionalMessage message) {
		super(message);
	}

	@Override
	public String perform() {
		return "The date is: " + getDate("MMM dd yyyy");
	}

	@Override
	public boolean matches() {
		return line.matches("^date$|what day is it|day");
	}

	@Override
	public String getSyntax() {
		return "date|day|what day is it";
	}

	@Override
	public String toString() {
		return "Get Date";
	}

	private String getDate(String format) {
		return new SimpleDateFormat(format).
				format(Calendar.getInstance().getTime());
	}
}