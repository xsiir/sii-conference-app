package org.sienkiewicz.conferenceapp.scheduler;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class SchedulerFacade {
	
	private final SchedulerService schedulerService;
	
	SchedulerFacade(SchedulerService schedulerService) {
		super();
		this.schedulerService = schedulerService;
	}
	
	public Optional<Lecture> getLectureById(Long id) {
		return schedulerService.getLectureById(id);
	}

	public boolean assignParticipantToLecture(Long userId, Long lectureId) {
		return schedulerService.isAssingsParticipantToLectureSuccesful(userId, lectureId);
	}

	public boolean unassingPartizipantFromLecture(Long userId, Long lectureId) {
		//TO-DO
		return true;
	}
	
	public List<ThematicPath> getThematicPathByDate(LocalDate date){
		return schedulerService.getThematicPathByDate(date);
	}

}
