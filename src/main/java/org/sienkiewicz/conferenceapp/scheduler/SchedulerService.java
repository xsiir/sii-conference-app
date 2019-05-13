package org.sienkiewicz.conferenceapp.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

	@Autowired
	private SchedulerRepository schedulerRepository;
	
	public List<ThematicPath> getThematicPathByDate(LocalDate date) {
		return schedulerRepository.getPathsBydate(date);
	}

}
