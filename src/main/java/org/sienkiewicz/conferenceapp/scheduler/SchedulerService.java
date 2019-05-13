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
	
	List<ThematicPath> getThematicPathByDate(LocalDate date) {
		return schedulerRepository.getPathsBydate(date);
	}
	
	boolean isAssingsParticipantToLectureSuccesful(Long userId, Long lectureId) {
		boolean isSuccesful = false;
		if(getLectureById(lectureId).filter(lecture -> lecture.getOcuppiedSeats() < 5).isPresent()) {
			isSuccesful = schedulerRepository.getLectureById(lectureId).filter(u -> u.addNew(userId)).isPresent();
		}
		return isSuccesful;
	}

	public Optional<Lecture> getLectureById(Long id) {
		return schedulerRepository.getLectureById(id);
	}

}
