package org.sienkiewicz.conferenceapp.scheduler;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchedulerFacade {
	
	private final SchedulerService schedulerService;
	
	@Autowired
	SchedulerFacade(SchedulerService schedulerService) {
		super();
		this.schedulerService = schedulerService;
	}
	
	/**
	 * Get lecture by given ID
	 * @param id	id of searched lecture
	 * @return	Optional of lecture with given id, if empty returns Optional.empty()
	 */
	public Optional<Lecture> findLectureById(Long id) {
		return schedulerService.getLectureById(id);
	}

	/**
	 * Add to lecture participants new user's id
	 * @param userId 	participant id
	 * @param lectureId		lecture id
	 * @return	return true if operation ends successfully, otherwise returns false
	 */
	public boolean assignParticipantToLecture(Long userId, Long lectureId) {
		return schedulerService.isAssingsParticipantToLectureSuccesful(userId, lectureId);
	}

	/**
	 * Remove user's id from lecture with given id
	 * @param userId	id of user which will be removed
	 * @param lectureId	id of lecture where user will be removed from
	 */
	public void unassingPartizipantFromLecture(Long userId, Long lectureId) {
		schedulerService.unassingPartizipantFromLecture(userId, lectureId);
	}
	
	/**
	 * Get whole plan schedule for specific day
	 * @param date 	date for needed plan schedule
	 * @return	list with thematic paths, can not be null 
	 */
	public List<ThematicPath> getThematicPathByDate(LocalDate date){
		return schedulerService.getThematicPathByDate(date);
	}


}
