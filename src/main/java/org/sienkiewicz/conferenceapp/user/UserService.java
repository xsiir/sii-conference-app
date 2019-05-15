package org.sienkiewicz.conferenceapp.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.sienkiewicz.conferenceapp.scheduler.Lecture;
import org.sienkiewicz.conferenceapp.scheduler.PlanElement;
import org.sienkiewicz.conferenceapp.scheduler.SchedulerFacade;
import org.sienkiewicz.conferenceapp.user.exceptions.AlreadyAssignedException;
import org.sienkiewicz.conferenceapp.user.exceptions.NoMoreSeatsLeftException;
import org.sienkiewicz.conferenceapp.user.exceptions.NotCompatibleEmailAddressesException;
import org.sienkiewicz.conferenceapp.user.exceptions.NotEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.vavr.control.Either;
import io.vavr.control.Option;

@Service
class UserService {

	private final UserRepository userRepository;
	private final SchedulerFacade schedulerFacade;
	private final LoggedUser loggedUser;

	@Autowired
	UserService(UserRepository userRepository, SchedulerFacade schedulerFacade, LoggedUser loggedUser) {
		super();
		this.userRepository = userRepository;
		this.schedulerFacade = schedulerFacade;
		this.loggedUser = loggedUser;
	}

	Optional<User> saveNewUser(User user) {
		Optional<User> savedUser = Optional.of(userRepository.save(user));
		return savedUser;
	}

	void login(String login) {
		userRepository.findByLogin(login).ifPresent(u -> {
			this.loggedUser.setId(u.getId());
			this.loggedUser.setEmail(u.getEmail());
			this.loggedUser.setLogin(u.getLogin());
			this.loggedUser.setLectures(u.getLectures());
		});
	}

	List<Lecture> getLoggedUserLectures() {
		List<Lecture> lectureList = new ArrayList<>();

		List<Long> lectureIdList = loggedUser.getLectures();
		lectureIdList.forEach(id -> {
			schedulerFacade.getLectureById(id).ifPresent(lecture -> lectureList.add(lecture));
		});

		return lectureList;
	}

	private Either<Exception, Boolean> isAlreadyAssinged(User user, Long lectureId) {
		return user.getLectures()
		.stream()
		.filter(userLectureId -> userLectureId.equals(lectureId))
		.findAny()
		.map(result -> Either.<Exception, Boolean>left(new AlreadyAssignedException(lectureId)))
		.orElse(Either.<Exception, Boolean>right(true));
	}


	boolean checkIfLoginAlreadyExsist(String login) {
		return userRepository.findByLogin(login).isPresent();
	}

	Either<Exception, Boolean> checkEmailsCompatibility(Optional<User> databaseUser, User sentUser) {
		return databaseUser.filter(exsistingUser -> exsistingUser.getEmail().equalsIgnoreCase(sentUser.getEmail()))
				.map(sameEmail -> Either.<Exception, Boolean>right(true))
				.orElse(Either.left(new NotCompatibleEmailAddressesException(sentUser.getLogin())));
	}

	Either<Exception, Boolean> hasAvailableSeats(Long lectureId) {
		return schedulerFacade.getLectureById(lectureId)
				.filter(lecture -> lecture.hasAvailableSeats())
				.map(lecture -> Either.<Exception, Boolean>right(true))
				.orElse(Either.<Exception, Boolean>left(new NoMoreSeatsLeftException(lectureId)));
	}

	public Either<Exception, Boolean> assignUserToLecture(User user, Long lectureId) {
		Either<Exception, Boolean> validationResult = assigningValidate(user, lectureId);
		validationResult.onRight(action -> {
			if(checkIfLoginAlreadyExsist(user.getLogin())) {
				final User userToSave = user;
				userRepository.save(userToSave);
			}

			user.addLecture(lectureId);
			userRepository.save(user);
			schedulerFacade.assignParticipantToLecture(user.getId(), lectureId);
			
			if(loggedUser.getLogin()!=null) {
				loggedUser.setLectures(user.getLectures());
			}
			
		});

		return validationResult;
	}

	private Either<Exception, Boolean>  assigningValidate(User user, Long lectureId){
		Either<Exception, Boolean> validationResult = Either.right(true);
		validationResult = checkIfEmailPatternIsCorrect(user.getEmail());
		if(validationResult.isLeft()) return validationResult;
		if(checkIfLoginAlreadyExsist(user.getLogin())) {
			validationResult = checkEmailsCompatibility(userRepository.findByLogin(user.getLogin()), user);
			if(validationResult.isLeft()) return validationResult;
		}
		validationResult = isAlreadyAssinged(user, lectureId);
		if(validationResult.isLeft()) return validationResult;
		validationResult = hasAvailableSeats(lectureId);
		if(validationResult.isLeft()) return validationResult;
		
		return validationResult;
		

	}

	private Either<Exception, Boolean> checkIfEmailPatternIsCorrect(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pattern = Pattern.compile(emailRegex);
		return pattern.matcher(email).matches() ? Either.<Exception, Boolean>right(true)
				: Either.<Exception, Boolean>left(new NotEmailException(email));
	}
	
	public Either<Exception, Boolean> assignLoggedUser(Long lectureId) {
		return userRepository.findByLogin(this.loggedUser.getLogin())
				.map(user -> assignUserToLecture(user, lectureId))
				.orElse(Either.left(new Exception("SOMETHING GONE WRONG")));
	}

	public Either<Exception, Boolean> assignNotLoggedUser(String login, String mail, Long lectureId) {
		User userToAssign = userRepository.findByLogin(login).orElse(new User(login, mail));
		return assignUserToLecture(userToAssign, lectureId);

	}


}
