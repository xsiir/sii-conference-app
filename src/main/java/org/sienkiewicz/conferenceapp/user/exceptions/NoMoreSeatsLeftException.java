package org.sienkiewicz.conferenceapp.user.exceptions;

public class NoMoreSeatsLeftException extends Exception {
	
	private Long lectureID;

	public NoMoreSeatsLeftException(Long lectureID) {
		super();
		this.lectureID = lectureID;
	}
	
	public String toString() {
		return "Lecture " + lectureID + " has no more seats left";
	}
	

}
