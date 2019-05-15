package org.sienkiewicz.conferenceapp.scheduler;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class SchedulerService {

	@Autowired
	private SchedulerRepository schedulerRepository;
	
	/**
	 * Get thematic paths by given date
	 * @param date LocalDate for searching paths
	 * @return	list with thematic paths, returns null if date does not exist
	 */
	List<ThematicPath> getThematicPathByDate(LocalDate date) {
		return schedulerRepository.getPathsBydate(date);
	}
	
	/**
	 * Check if adding user to lecture ends successfully 
	 * 
	 * @param userId id of user who will be added
	 * @param lectureId  if of lecture where user will be added to
	 * @return true if ends succesfully, otherwise false
	 */
	boolean isAssingsParticipantToLectureSuccesful(Long userId, Long lectureId) {
			return schedulerRepository.getLectureById(lectureId).map(u -> u.addNew(userId)).orElse(false);
	}

	/**
	 * Responsible for searching Lecture by given ID. 
	 * 
	 * @param lectureId id of searched lecture.
	 * @return Optional of Lecture if has been found, or Optional.empty() if has not been found.
	 */
	public Optional<Lecture> getLectureById(Long id) {
		return schedulerRepository.getLectureById(id);
	}

	/**
	 * Unassign user from lecture
	 * @param userId user who will be removed from lecture
	 * @param lectureId lecture which will be changed 
	 */
	public void unassingPartizipantFromLecture(Long userId, Long lectureId) {
		getLectureById(lectureId)
			.map(lecture -> lecture.getParticipants())
			.filter(singleLecture -> 
				singleLecture.removeIf(index -> index.equals(userId)));
	}

}
