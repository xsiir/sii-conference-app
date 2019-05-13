package org.sienkiewicz.conferenceapp.scheduler;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

class Break implements PlanElement {
	
	private final LocalTime startTime;
	private final LocalTime endTime;
	private final Duration duration;
	private final String title;
	
	Break(LocalTime startTime, Duration duration, String name) {
		super();
		this.startTime = startTime;
		this.duration = duration;
		this.title = name;
		this.endTime = startTime.plusMinutes(duration.toMinutes());
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public Duration	 getDuration() {
		return duration;
	}

	
	public String getTitle() {
		return title;
	}
	
	public LocalTime getEndTime() {
		return endTime;
	}
	
	public Long getId() {
		return 0L;
	}
	
	
	
}
