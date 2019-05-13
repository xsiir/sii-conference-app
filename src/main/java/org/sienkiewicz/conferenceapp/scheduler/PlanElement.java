package org.sienkiewicz.conferenceapp.scheduler;

import java.time.LocalTime;

public interface PlanElement {
	
	String getTitle();
	LocalTime getEndTime();
	LocalTime getStartTime();
	Long getId();

}
