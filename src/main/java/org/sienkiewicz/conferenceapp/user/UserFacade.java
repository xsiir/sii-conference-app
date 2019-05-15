package org.sienkiewicz.conferenceapp.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.sienkiewicz.conferenceapp.scheduler.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.vavr.control.Either;

@Service
public class UserFacade {
	
	private final UserService userService;
	private LoggedUser loggedUser;
	
	@Autowired
	private UserRepository userRepository;
	
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
	
	public void testRegister() {
		if(!userRepository.findByLogin("maciek").isPresent()) {
			User user = new User("maciek", "email@moj.pl");
			userRepository.save(user);
		}
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

}