package com.google.devrel.training.conference.form;

import java.util.Date;

public class SessionForm {
	
	private String name;
	private String speaker;
	private Date startTime;
	private String duration;
	private SessionType typeOfSession;
	
	public SessionForm(){}
	
	public SessionForm(String name, String speaker, Date startTime, 
			       String duration, SessionType typeOfSession){
		this.name = name;
		this.speaker = speaker;
		this.startTime = startTime == null ? null : new Date(startTime.getTime());;
		this.duration = duration;
		this.typeOfSession = typeOfSession;
	}
	
	
    /**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the speaker
	 */
	public String getSpeaker() {
		return speaker;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}

	/**
	 * @return the typeOfSession
	 */
	public SessionType getTypeOfSession() {
		return typeOfSession;
	}


	public static enum SessionType {
    	NOT_SPECIFIED,
    	Lecture,
    	Keynote,
    	Workshop
    }
}
