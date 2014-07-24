package net.samism.java.percival;

public abstract class IRCMessage {
//	 TODO: Do all of these members have to be abstract and therefore overriden by every child class?


	public static final String OWNER = PercivalBot.OWNER;

	String msg = null;

	public abstract boolean isFrom(String author);

	public abstract boolean isFromOwner();

	public abstract String getResponse();

	public abstract String getAuthor();

	public abstract String getMsg();

	public String getRawMsg(){
		return msg;
	}

	public abstract String toString();
}