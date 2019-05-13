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
	}
	

}