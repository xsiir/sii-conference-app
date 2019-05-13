package org.sienkiewicz.conferenceapp.scheduler;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Lecture implements PlanElement{
	
	private final int _MAX_PARTICIPANT = 5;
	
	private Long id;
	private LocalTime startTime;
	private LocalTime endTime;
	private Duration duration;
	private String title;
	private String speaker;
	private List<Long> participants;

	Lecture(Long id, LocalTime time, Duration duration, String title, String speaker) {
		super();
		this.id = id;
		this.startTime = time;
		this.duration = duration;
		this.title = title;
		this.speaker = speaker;
		this.participants = new ArrayList<Long>();
		this.endTime = startTime.plusMinutes(duration.toMinutes());
	}

	public int get_MAX_PARTICIPANT() {
		return _MAX_PARTICIPANT;
	}

	public Long getId() {
		return id;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public Duration getDuration() {
		return duration;
	}

	public String getTitle() {
		return title;
	}

	public String getSpeaker() {
		return speaker;
	}

	List<Long> getParticipants() {
		return participants;
	}
	
	public int getOcuppiedSeats(){
		return participants.size();
	}
	
	boolean addNew(Long id) {
		return participants.add(id);
	}
	
	public LocalTime getEndTime() {
		return endTime;
	}
	
	
}
