package org.sienkiewicz.conferenceapp.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class SchedulerService {

	@Autowired
	private SchedulerRepository schedulerRepository;
	
	List<ThematicPath> getThematicPathByDate(LocalDate date) {
		return schedulerRepository.getPathsBydate(date);
	}
	
	boolean isAssingsParticipantToLectureSuccesful(Long userId, Long lectureId) {
		boolean isSuccesful = schedulerRepository.getLectureById(lectureId).filter(u -> u.addNew(userId)).isPresent();
		return isSuccesful;
	}

}
