package org.sienkiewicz.conferenceapp.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.sienkiewicz.conferenceapp.scheduler.SchedulerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SchedulerFacade {
	
	@Autowired
	private SchedulerRepository repo;
	
	@Autowired
	private SchedulerService service;
	
	public boolean assignParticipantToLecture(Long userId, Long lectureId) {
		return isAddingSuccesful(userId, lectureId);
	}

	private boolean isAddingSuccesful(Long userId, Long lectureId) {
		boolean isSuccesful = repo.getLectureById(lectureId).filter(u -> u.addNew(userId)).isPresent();
		return isSuccesful;
	}
	
	public boolean unassingPartizipantFromLecture(Long userId, Long lectureId) {
		//TO-DO
		return true;
	}
	
	public List<ThematicPath> getThematicPathByDate(LocalDate date){
		return service.getThematicPathByDate(date);
	}

}
