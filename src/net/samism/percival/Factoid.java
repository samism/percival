package net.samism.percival;

import java.sql.Date;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/21/14
 * Time: 11:24 PM
 */
public class Factoid {
	private String author;
	private String trigger;
	private String response;
	private Date dateCreated;

	public Factoid(String author, String trigger, String response, Date created) {
		this.author = author;
		this.trigger = trigger;
		this.response = response;
		this.dateCreated = created;
	}

	public Factoid() {
		this.author = this.trigger = this.response = null;
		this.dateCreated = null;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public String getResponse() {
		return response;
	}

	public String getTrigger() {
		return trigger;
	}

	public String getAuthor() {
		return author;
	}
}
