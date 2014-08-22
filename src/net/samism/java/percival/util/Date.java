package net.samism.java.percival.util;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/21/14
 * Time: 11:07 PM
 */
public final class Date {
	public static java.sql.Date today() {
		return new java.sql.Date(new java.util.Date().getTime());
	}
}
