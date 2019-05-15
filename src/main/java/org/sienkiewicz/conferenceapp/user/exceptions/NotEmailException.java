package org.sienkiewicz.conferenceapp.user.exceptions;

public class NotEmailException extends Exception {
	
	private String email;

	public NotEmailException(String email) {
		super();
		this.email = email;
	}
	
	public String toString() {
		return this.email + " is not an email!";
	}

}
