package org.sienkiewicz.conferenceapp.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String login;
	private String email;

	@ElementCollection(targetClass = Long.class, fetch = FetchType.EAGER)
	private List<Long> lectures;
	
	User(){}
	
	User(String login, String email) {
		super();
		this.id = id;
		this.login = login;
		this.email = email;
		this.lectures = new ArrayList<Long>();
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

	Long getId() {
		return id;
	}
	
	List<Long> getLectures(){
		return lectures;
	}

	public boolean addLecture(long lectureId) {
		return this.lectures.add(lectureId);
		
	}
	
	
	
	
	

}
