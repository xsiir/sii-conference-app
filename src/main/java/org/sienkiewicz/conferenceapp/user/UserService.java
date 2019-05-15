package org.sienkiewicz.conferenceapp.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.sienkiewicz.conferenceapp.mailsender.EmailSender;
import org.sienkiewicz.conferenceapp.scheduler.Lecture;
import org.sienkiewicz.conferenceapp.scheduler.SchedulerFacade;
import org.sienkiewicz.conferenceapp.user.exceptions.AlreadyAssignedException;
import org.sienkiewicz.conferenceapp.user.exceptions.HasAnotherLectureInSameTimeException;
import org.sienkiewicz.conferenceapp.user.exceptions.NoMoreSeatsLeftException;
import org.sienkiewicz.conferenceapp.user.exceptions.NotCompatibleEmailAddressesException;
import org.sienkiewicz.conferenceapp.user.exceptions.NotEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.vavr.control.Either;

@Service
class UserService {

	private final UserRepository userRepository;
	private final SchedulerFacade schedulerFacade;
	private final LoggedUser loggedUser;
	private final EmailSender emailSender;

	@Autowired
	UserService(UserRepository userRepository, SchedulerFacade schedulerFacade, LoggedUser loggedUser, EmailSender emailSender) {
		super();
		this.userRepository = userRepository;
		this.schedulerFacade = schedulerFacade;
		this.loggedUser = loggedUser;
		this.emailSender = emailSender;
	}

	Optional<User> saveNewUser(User user) {
		Optional<User> savedUser = Optional.of(userRepository.save(user));
		return savedUser;
	}

	/**
	 * If user with given login exsist, set every field of loggedUser as user with given login from database,
	 * otherwise do nothing
	 * @param login login for searching user
	 */
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
			schedulerFacade.findLectureById(id).ifPresent(lecture -> lectureList.add(lecture));
		});

		return lectureList;
	}

	/**
	 * Check if user is already assigned to lecture where he is trying assigne to
	 * @param user sent user
	 * @param lectureId id of lecture where user is trying to assigne to
	 * @return Either.right true if user has not been assigned, Either.left  AlreadyAssignedException if has been.
	 */
	private Either<Exception, Boolean> isAlreadyAssinged(User user, Long lectureId) {
		return user.getLectures()
		.stream()
		.filter(userLectureId -> userLectureId.equals(lectureId))
		.findAny()
		.map(result -> Either.<Exception, Boolean>left(new AlreadyAssignedException(lectureId)))
		.orElse(Either.<Exception, Boolean>right(true));
	}

	/**
	 * Check if given login already exist in database
	 * @param login searched login
	 * @return  true if has been, false if has not
	 */
	boolean checkIfLoginAlreadyExist(String login) {
		return userRepository.findByLogin(login).isPresent();
	}

	/**
	 * Check if login of sent user already exist in database and if it does
	 * check email compatibility between sent detail and database record 
	 * @param databaseUser record from data
	 * @param user sent user
	 * @return Either.right true if emails are correct, Either.left  NotCompatibleEmailAddressesException if not.
	 */
	Either<Exception, Boolean> checkEmailsCompatibility(Optional<User> databaseUser, User sentUser) {
		return databaseUser.filter(exsistingUser -> exsistingUser.getEmail().equalsIgnoreCase(sentUser.getEmail()))
				.map(sameEmail -> Either.<Exception, Boolean>right(true))
				.orElse(Either.left(new NotCompatibleEmailAddressesException(sentUser.getLogin())));
	}

	Either<Exception, Boolean> hasAvailableSeats(Long lectureId) {
		return schedulerFacade.findLectureById(lectureId)
				.filter(lecture -> lecture.hasAvailableSeats())
				.map(lecture -> Either.<Exception, Boolean>right(true))
				.orElse(Either.<Exception, Boolean>left(new NoMoreSeatsLeftException(lectureId)));
	}

	public Either<Exception, Boolean> assignUserToLecture(User user, Long lectureId) {
		Either<Exception, Boolean> validationResult = assigningValidate(user, lectureId);
		validationResult.onRight(action -> {
			if(checkIfLoginAlreadyExist(user.getLogin())) {
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
		
		validationResult.onRight(action -> emailSender.sendEmail(user.getEmail()));

		return validationResult;
	}

	private Either<Exception, Boolean>  assigningValidate(User user, Long lectureId){
		Either<Exception, Boolean> validationResult = Either.right(true);
		validationResult = checkIfEmailPatternIsCorrect(user.getEmail());
		if(validationResult.isLeft()) return validationResult;
		if(checkIfLoginAlreadyExist(user.getLogin())) {
			validationResult = checkEmailsCompatibility(userRepository.findByLogin(user.getLogin()), user);
			if(validationResult.isLeft()) return validationResult;
		}
		validationResult = isAlreadyAssinged(user, lectureId);
		if(validationResult.isLeft()) return validationResult;
		validationResult = checkIfIsAlreadyAssignToOtherLectureInSameTime(user, lectureId);
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
		userRepository.save(userToAssign);
		return assignUserToLecture(userToAssign, lectureId);
	}

	public Either<Exception, Boolean> changeEmailAdress(String email) {
		Either<Exception, Boolean> validation = checkIfEmailPatternIsCorrect(email);
		validation.onRight(action -> {
			userRepository.findByLogin(this.loggedUser.getLogin()).ifPresent(user -> {
				user.setEmail(email);
				loggedUser.setEmail(email);
			});
		});
		
		return validation;
	}

	public void unassign(Long lectureId) {
		Optional<User> user = userRepository.findById(this.loggedUser.getId());
		user.map(usr -> usr.getLectures())
				.map(listOfLecture -> listOfLecture.removeIf(index -> index.equals(lectureId)));
		user.ifPresent(usr -> userRepository.save(usr));
		login(this.loggedUser.getLogin());
		schedulerFacade.unassingPartizipantFromLecture(this.loggedUser.getId(), lectureId);
		
	}
	
	private Either<Exception, Boolean> checkIfIsAlreadyAssignToOtherLectureInSameTime(User user, Long lectureId){
		Optional<User> optionalUser = userRepository.findById(user.getId());
		List<Long> lectures = optionalUser.get().getLectures();
		for(Long userLecture : lectures) {
			if(schedulerFacade.findLectureById(userLecture).filter(lecture -> lecture.checkIfInTheSamTime(
					schedulerFacade.findLectureById(lectureId))).isPresent()) {
				return Either.left(new HasAnotherLectureInSameTimeException());
			}
		}
		return Either.right(true);
	}


}
