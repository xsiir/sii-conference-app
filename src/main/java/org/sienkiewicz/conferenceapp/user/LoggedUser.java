package org.sienkiewicz.conferenceapp.user;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
class LoggedUser {

	private Long id;
	private String login;
	private String email;
	private List<Long> lectures;
	
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
	
	void setLectures(List<Long> lectures) {
		this.lectures = lectures;
	}
	
	List<Long> getLectures(){
		return this.lectures;
	}
	
	
}
