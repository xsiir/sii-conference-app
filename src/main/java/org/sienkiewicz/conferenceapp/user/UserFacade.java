package org.sienkiewicz.conferenceapp.user;

import java.util.List;

import org.sienkiewicz.conferenceapp.scheduler.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.vavr.control.Either;

@Service
public class UserFacade {
	
	private final UserService userService;
	private LoggedUser loggedUser;
	
	@Autowired
	UserFacade(UserService userService, LoggedUser loggedUser) {
		super();
		this.userService = userService;
		this.loggedUser = loggedUser;
	}

	public void login(String value) {
		userService.login(value);
	}
	
	public boolean isLogged() {
		return loggedUser.getLogin() == null ? false : true;
	}

	public void logout() {
		this.loggedUser.setId(null);
		this.loggedUser.setLogin(null);
		this.loggedUser.setEmail(null);
		this.loggedUser.setLectures(null);
	}
	
	public List<Lecture> getLoggedUserLectures() {
		return userService.getLoggedUserLectures();
	}

	public Either<Exception, Boolean> assignNewUser(String login, String mail, Long lectureId) {
		return userService.assignNotLoggedUser(login, mail, lectureId);		
	}
	
	public Either<Exception, Boolean> assingLoggedUserToLecture(Long lectureId){
		return userService.assignLoggedUser(lectureId);
	}
	
	public void unassing(Long lectureId){
		userService.unassign(lectureId);
	}

	public Either<Exception, Boolean> changeUserEmail(String mail) {
		return userService.changeEmailAdress(mail);
	}
	
	public String getLoggedUserEmail() {
		return loggedUser.getEmail();
	}
	
	public String getLoggedUserLogin() {
		return loggedUser.getLogin();
	}

}