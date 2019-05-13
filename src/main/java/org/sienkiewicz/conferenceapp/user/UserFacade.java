package org.sienkiewicz.conferenceapp.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public void assingUserToLecture(Long id) {
		Optional<User> user = userRepository.findByLogin(loggedUser.getLogin());
		userService.assing(user.get(), id);
	}

	public void login(String value) {
		userService.login(value);
		System.out.println("ZALOGOWANO: " + loggedUser.getLogin());
	}
	
	public void saveNewOne() {
		User user = new User("maciek", "moj@email.pl");
		userRepository.save(user);
		System.out.println("ZAPISANO XD");
	}

	public boolean checkIfLogged() {
		boolean isLogged = loggedUser.getEmail() == null ? false : true;
		return isLogged;
	}
	
	public void logout() {
		loggedUser = null;
		System.out.println("WYLOGOWANO " + loggedUser.getLogin());
	}

}