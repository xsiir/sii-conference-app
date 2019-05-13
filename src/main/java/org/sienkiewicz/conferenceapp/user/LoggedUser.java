package org.sienkiewicz.conferenceapp.user;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
class LoggedUser {

	private Long id;
	private String login;
	private String email;
	
	Long getId() {
		return id;
	}
	void setId(Long id) {
		this.id = id;
	}
	String getLogin() {
		return login;
	}
	void setLogin(String login) {
		this.login = login;
	}
	String getEmail() {
		return email;
	}
	void setEmail(String email) {
		this.email = email;
	}
	
	LoggedUser getLoggedUser() {
		return this;
	}
	
	
}
