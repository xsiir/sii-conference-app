package org.sienkiewicz.conferenceapp.user;

import java.util.Optional;

import org.sienkiewicz.conferenceapp.scheduler.PlanElement;
import org.sienkiewicz.conferenceapp.scheduler.SchedulerFacade;
import org.sienkiewicz.conferenceapp.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class UserService {

	private final  UserRepository userRepository;
	private final SchedulerFacade schedulerFacade;
	private final LoggedUser loggedUser;

	@Autowired
	UserService(UserRepository userRepository, 
				SchedulerFacade schedulerFacade, 
				LoggedUser loggedUser) {
		super();
		this.userRepository = userRepository;
		this.schedulerFacade = schedulerFacade;
		this.loggedUser = loggedUser;
	}
	
	boolean assing(User user, Long lectureId) {	
		Optional<User> foundedUser = userRepository.findByLogin(user.getLogin());
		if(foundedUser.isPresent()) {
			if(checkEmailsCompatibility(user)) {
				schedulerFacade.assignParticipantToLecture(user.getId(), lectureId);
				assignLectureToParticipant(user, lectureId);
			}else {
				return false;
			}
		}else {
			Optional<User> newUser = saveNewUser(user);
			newUser.filter(u -> (schedulerFacade.assignParticipantToLecture(u.getId(), lectureId)));
		}
		return true;
	}
	
	private boolean assignLectureToParticipant(User user, Long lectureId) {
		boolean isSuccesful = user.getLectures().add(lectureId);
		return isSuccesful;
	}

	Optional<User> saveNewUser(User user){
		Optional<User> savedUser = Optional.ofNullable(userRepository.save(user));
		return savedUser;
	}
	
	boolean checkIfLoginAlreadyExsist(String login){
		Optional<User> user = userRepository.	findByLogin(login);
		boolean ifExsist = user.filter(u -> u.getLogin().equals(login)).isPresent();
		return ifExsist;
		
	}
	
	private boolean checkEmailsCompatibility(User user) {
		boolean ifCompatible = userRepository.findByLogin(user.getLogin())		
							.filter(u -> u.getEmail().equalsIgnoreCase(user.getEmail()))
							.isPresent();
		return ifCompatible;
	}
	
	public void getId(PlanElement elem) {
		System.out.println(elem.getTitle());
	}
	
	public void login(String login) {
		userRepository.findByLogin(login).ifPresent(u-> {
			this.loggedUser.setId(u.getId());
			this.loggedUser.setEmail(u.getEmail());
			this.loggedUser.setLogin(u.getLogin());
		});
	}
	
	
	
}
