package org.sienkiewicz.conferenceapp.user.exceptions;

public class NotCompatibleEmailAddressesException extends Exception {
	
	private String login;
	
	public NotCompatibleEmailAddressesException(String login) {
		this.login = login;
	}
	
	public String toString() {
		return "User " + login + " already exsist and has other email adress";
	}
}
