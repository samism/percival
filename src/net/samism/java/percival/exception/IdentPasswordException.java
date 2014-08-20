package net.samism.java.percival.exception;

/**
 * Created with IntelliJ IDEA.
 * User: samism
 * Date: 8/20/14
 * Time: 2:06 AM
 */
public class IdentPasswordException extends Exception {
	public IdentPasswordException(){
		super();
	}

	public IdentPasswordException(String message){
		super(message);
	}

	public IdentPasswordException(String message, Throwable cause){
		super(message, cause);
	}

	public IdentPasswordException(Throwable cause){
		super(cause);
	}
}