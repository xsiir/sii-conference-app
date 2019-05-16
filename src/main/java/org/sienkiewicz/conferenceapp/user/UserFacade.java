package org.sienkiewicz.conferenceapp.user;

import java.util.List;
import java.util.Optional;

import org.sienkiewicz.conferenceapp.mailsender.EmailSender;
import org.sienkiewicz.conferenceapp.scheduler.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
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

	/**
	 * Set every field for loggedUser as user with given login
	 * @param value
	 */
	public void login(String login) {
		userService.login(login);
	}
	/**
	 * Check if user is logged
	 * @return true is he is, otherwise false
	 */
	public boolean isLogged() {
		return loggedUser.getLogin() == null ? false : true;
	}

	/**
	 * Logout user - it means that every field in LoggedUser will be set to null
	 */
	public void logout() {
		this.loggedUser.setId(null);
		this.loggedUser.setLogin(null);
		this.loggedUser.setEmail(null);
		this.loggedUser.setLectures(null);
	}
	
	/**
	 * Get all lectures of logged user, if user is not logged in then return null
	 * @return  lecture's list of logged user, if user is not logged in then return null
	 */
	public List<Lecture> getLoggedUserLectures() {
		return userService.getLoggedUserLectures();
	}

	/**
	 * Try to assign new user to lecture
	 * @param login login of new user
	 * @param mail mail of new user
	 * @param lectureId lecture where user is trying to assign to
	 * @return Either.right true if operation ends successfully, otherwise Either.left Exception with more details
	 */
	public Either<Exception, Boolean> assignNewUser(String login, String mail, Long lectureId) {
		return userService.assignNotLoggedUser(login, mail, lectureId);		
	}
	
	/**
	 * Try to assign logged user to lecture
	 * @param lectureId lecture where user is trying to assign to
	 * @return Either.right true if operation ends successfully, otherwise Either.left Exception with more details
	 */
	public Either<Exception, Boolean> assingLoggedUserToLecture(Long lectureId){
		return userService.assignLoggedUser(lectureId);
	}
	
	/**
	 * Unassign logged user - remove id of lecture from user lecture's list and remove user's id from participants list in lecture
	 * @param lectureId id of lecture where user will be removed from
	 */
	public void unassing(Long lectureId){
		userService.unassign(lectureId);
	}

	/**
	 * Try to change user email
	 * @param mail new user's email
	 * @return  Either.right true if operation ends successfully, otherwise Either.left NotEmailException
	 */
	public Either<Exception, Boolean> changeUserEmail(String mail) {
		return userService.changeEmailAdress(mail);
	}
	
	/**
	 * Get logged user email
	 * @return Optional of logged user login, if user is not logged in then return Optional.empty
	 */
	public Optional<String> getLoggedUserEmail() {
		return Optional.ofNullable(loggedUser.getEmail());
	}
	
	/**
	 * Get logged user email
	 * @return Optional of logged user email, if user is not logged in then return Optional.empty
	 */
	public Optional<String> getLoggedUserLogin() {
		return Optional.ofNullable(loggedUser.getLogin());
	}
	
}