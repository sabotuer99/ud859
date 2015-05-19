package com.google.devrel.training.conference.domain;

import java.util.Date;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.devrel.training.conference.form.SessionForm.SessionType;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
@Cache
public class Session {
	@Id
	private Long id;
	
	@Parent
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	private Key<Conference> conferenceKey;
	
	@Index
	private String name;
	
	@Index
	private String speaker;
	private Date startTime;
	private String duration;
	
	@Index
	private SessionType typeOfSession;
	
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	private String createdByUserId;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the conferenceKey
	 */
	public Key<Conference> getConferenceKey() {
		return conferenceKey;
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

	/**
	 * @return the createdByUserId
	 */
	public String getCreatedByUserId() {
		return createdByUserId;
	}

	public Session(){}
	
	public Session(final String conferenceId, long sessionId, String name, String speaker, Date startTime, 
			       String duration, SessionType typeOfSession,
			       String userId){
		this.conferenceKey = Key.create(Conference.class, conferenceId);
		this.id = sessionId;
		this.name = name;
		this.speaker = speaker;
		this.startTime = startTime;
		this.duration = duration;
		this.typeOfSession = typeOfSession;
		createdByUserId = userId;
	}
	
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Id: " + id + "\n")
                .append("Name: ").append(name).append("\n");
        if (speaker != null) {
            stringBuilder.append("Speaker: ").append(speaker).append("\n");
        }
        if (startTime != null) {
            stringBuilder.append("StartTime: ").append(startTime.toString()).append("\n");
        }
        if (duration != null) {
            stringBuilder.append("Duration: ").append(duration).append("\n");
        }
        if (typeOfSession != null) {
            stringBuilder.append("Type of Session: ").append(typeOfSession).append("\n");
        }
        return stringBuilder.toString();
    }
}
