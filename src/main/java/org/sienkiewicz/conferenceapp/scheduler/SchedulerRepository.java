package org.sienkiewicz.conferenceapp.scheduler;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
class SchedulerRepository {
	
	//Map which storage all thematic paths for single day.
	private Map<LocalDate, List<ThematicPath>> scheduler = new HashMap<LocalDate, List<ThematicPath>>();
	
	//List for initialize days schedule.
	private List<PlanElement> planDetails;
	
	
	SchedulerRepository() {
		initializeScheduler();
	}
		
	private void initializeScheduler() {
		initializeFirstDay();
		initializeSecondDay();	
		}
	
	Map<LocalDate, List<ThematicPath>> getSchedule(){
		return this.scheduler;
	}
	
	Optional<Lecture> getLectureById(Long lectureId) {
		Optional<Lecture> lecture = Optional.ofNullable(null);
		Collection<List<ThematicPath>> collectionOfDaySchedules = getSchedule().values();
		for(List<ThematicPath> thematicPaths : collectionOfDaySchedules) {
			for(ThematicPath path : thematicPaths) {
				for(PlanElement planElement : path.getPlanOfTheDay()) {
					if(planElement instanceof Lecture && ((Lecture) planElement).getId() == lectureId) {
						lecture = Optional.of((Lecture) planElement);
						System.out.println(lecture.get().getTitle());
						return lecture;
					}
				}
			}
		}
		
		return lecture;
	}
	
	private void initializeFirstDay() {
		//ADD NEW DAY TO SCHEDULER
		LocalDate firstDay = LocalDate.of(2019, 6, 1);
		scheduler.put(firstDay, new LinkedList<>());
		
		//GENERATE FIRST DAY PATHS
		scheduler.get(firstDay).add(new ThematicPath(0L, "JAVA", firstDay));
		scheduler.get(firstDay).add(new ThematicPath(0L, "FRONT-END", firstDay));
		scheduler.get(firstDay).add(new ThematicPath(0L, "BAZY DANYCH", firstDay));

		
		//GENERATE FIRST PATH PLAN
		planDetails = scheduler.get(firstDay).get(0).getPlanOfTheDay();
		planDetails.add(new Lecture(0L, LocalTime.of(10, 00), Duration.ofHours(1).plus(Duration.ofMinutes(45)), "JVM Internals", "Jakub Kubryński"));
		planDetails.add(new Break(getEndTimeOfPreviousPlanElement(), Duration.ofMinutes(15), "Coffee Break"));
		planDetails.add(new Lecture(1L, getEndTimeOfPreviousPlanElement(), Duration.ofHours(1).plus(Duration.ofMinutes(45)), "Modularity and hexagonal architecture", "Jakub Nabrdalik"));

		scheduler.get(firstDay).get(0).replacePlan(planDetails);
		
		//GENERATE SECOND PATH PLAN
		
		planDetails = scheduler.get(firstDay).get(1).getPlanOfTheDay();
		planDetails.add(new Lecture(2L, LocalTime.of(10, 00), Duration.ofHours(1).plus(Duration.ofMinutes(45)), "REACT na co dzień", "Jan Kowalski"));
		planDetails.add(new Break(getEndTimeOfPreviousPlanElement(), Duration.ofMinutes(15), "Coffee  Break"));
		planDetails.add(new Lecture(3L, getEndTimeOfPreviousPlanElement(), Duration.ofHours(1).plus(Duration.ofMinutes(45)), "ANGULAR - ciekawostki", "Tomasz Nowak"));
		
		scheduler.get(firstDay).get(1).replacePlan(planDetails);

		//GENERATE THIRD PATH PLAN
		planDetails = scheduler.get(firstDay).get(2).getPlanOfTheDay();
		planDetails.add(new Lecture(4L, LocalTime.of(10, 00), Duration.ofHours(1).plus(Duration.ofMinutes(45)), "MongoDB", "Wojciech Kowalczyk"));
		planDetails.add(new Break(getEndTimeOfPreviousPlanElement(), Duration.ofMinutes(15), "Coffee Break"));
		planDetails.add(new Lecture(5L, getEndTimeOfPreviousPlanElement(), Duration.ofHours(1).plus(Duration.ofMinutes(45)), "MySQL", "Zbigniew Lewandowski"));

		scheduler.get(firstDay).get(2).replacePlan(planDetails);
	}

	private void initializeSecondDay() {
		//ADD SECOND DAY TO SCHEDULER
		LocalDate secondDay = LocalDate.of(2019, 6, 2);
		scheduler.put(secondDay, new LinkedList<>());
				
		//GENERATE SECOND DAY PATHS
		scheduler.get(secondDay).add(new ThematicPath(0L, "JAVA", secondDay));
		scheduler.get(secondDay).add(new ThematicPath(0L, "FRONT-END", secondDay));
		scheduler.get(secondDay).add(new ThematicPath(0L, "BAZY DANYCH", secondDay));

				
		//GENERATE FIRST PATH PLAN
		List<PlanElement> planDetails = scheduler.get(secondDay).get(0).getPlanOfTheDay();
		planDetails.add(new Lecture(6L, LocalTime.of(10, 00), Duration.ofHours(1).plus(Duration.ofMinutes(45)), "JVM Internals", "Jakub Kubryński"));
		planDetails.add(new Break(getEndTimeOfPreviousPlanElement(), Duration.ofMinutes(15), "Coffee Break"));
		planDetails.add(new Lecture(7L, getEndTimeOfPreviousPlanElement(), Duration.ofHours(1).plus(Duration.ofMinutes(45)), "Modularity and hexagonal architecture", "Jakub Nabrdalik"));

		scheduler.get(secondDay).get(0).replacePlan(planDetails);
				
		//GENERATE SECOND PATH PLAN
				
		planDetails = scheduler.get(secondDay).get(1).getPlanOfTheDay();
		planDetails.add(new Lecture(8L, LocalTime.of(10, 00), Duration.ofHours(1).plus(Duration.ofMinutes(45)), "REACT na co dzień", "Jan Kowalski"));
		planDetails.add(new Break(getEndTimeOfPreviousPlanElement(), Duration.ofMinutes(15), "Coffee Break"));
		planDetails.add(new Lecture(9L, getEndTimeOfPreviousPlanElement(), Duration.ofHours(1).plus(Duration.ofMinutes(45)), "ANGULAR - ciekawostki", "Tomasz Nowak"));
		
		scheduler.get(secondDay).get(1).replacePlan(planDetails);

		//GENERATE THIRD PATH PLAN
		planDetails = scheduler.get(secondDay).get(2).getPlanOfTheDay();
		planDetails.add(new Lecture(10L, LocalTime.of(10, 00), Duration.ofHours(1).plus(Duration.ofMinutes(45)), "MongoDB", "Wojciech Kowalczyk"));
		planDetails.add(new Break(getEndTimeOfPreviousPlanElement(), Duration.ofMinutes(15), "Coffee Break"));
		planDetails.add(new Lecture(11L, getEndTimeOfPreviousPlanElement(), Duration.ofHours(1).plus(Duration.ofMinutes(45)), "MySQL", "Zbigniew Lewandowski"));

		scheduler.get(secondDay).get(2).replacePlan(planDetails);
	}

	List<ThematicPath> getPathsBydate(LocalDate date) {
		return scheduler.get(date);
	}
	
	private LocalTime getEndTimeOfPreviousPlanElement() {
		return planDetails.get(planDetails.size()-1).getEndTime();
	}

}
