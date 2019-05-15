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
			return schedulerRepository.getLectureById(lectureId).map(u -> u.addNew(userId)).orElse(false);
	}

	public Optional<Lecture> getLectureById(Long id) {
		return schedulerRepository.getLectureById(id);
	}

	public void unassingPartizipantFromLecture(Long userId, Long lectureId) {
		getLectureById(lectureId)
			.map(lecture -> lecture.getParticipants())
			.filter(singleLecture -> 
				singleLecture.removeIf(index -> index.equals(userId)));
	}

}
