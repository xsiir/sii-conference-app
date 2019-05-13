package org.sienkiewicz.conferenceapp.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchedulerFacade {
	
	private final SchedulerRepository schedulerRepositorium;
	private final SchedulerService schedulerService;
	
	SchedulerFacade(SchedulerRepository schedulerRepositorium, SchedulerService schedulerService) {
		super();
		this.schedulerRepositorium = schedulerRepositorium;
		this.schedulerService = schedulerService;
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
