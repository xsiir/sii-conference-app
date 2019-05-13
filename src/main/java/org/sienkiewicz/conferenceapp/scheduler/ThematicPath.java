package org.sienkiewicz.conferenceapp.scheduler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ThematicPath {
	
	private Long id;
	private String title;
	private LocalDate date;
	private List<PlanElement> planOfTheDay;
	
	public ThematicPath(Long id, String title, LocalDate date) {
		super();
		this.id = id;
		this.title = title;
		this.date = date;
		this.planOfTheDay = new ArrayList<PlanElement>();
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public LocalDate getDate() {
		return date;
	}

	public List<PlanElement> getPlanOfTheDay() {
		return planOfTheDay;
	}
	
	List<PlanElement> replacePlan(List<PlanElement> plan){
		this.planOfTheDay = plan;
		return getPlanOfTheDay();
	}
	
	

	
}
