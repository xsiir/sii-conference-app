package org.sienkiewicz.conferenceapp.user.exceptions;

public class HasAnotherLectureInSameTimeException extends Exception{
	
	public String toString() {
		return "You are already assigned to another lecture in same time!";
	}

}
