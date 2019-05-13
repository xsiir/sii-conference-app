package org.sienkiewicz.conferenceapp.scheduler;

import java.time.LocalTime;

public interface PlanElement {
	
	Long getId();
	String getTitle();
	LocalTime getStartTime();
	LocalTime getEndTime();

}
