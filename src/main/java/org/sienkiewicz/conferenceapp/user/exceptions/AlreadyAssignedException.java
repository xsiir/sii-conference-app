package org.sienkiewicz.conferenceapp.user.exceptions;

public class AlreadyAssignedException extends Exception{
	
	private Long lectureId;
	
	public AlreadyAssignedException(Long lectureId) {
		this.lectureId = lectureId;
	}
	
	public String toString() {
		return "You are already assigned to lecture with ID "+ lectureId;
	}

}
