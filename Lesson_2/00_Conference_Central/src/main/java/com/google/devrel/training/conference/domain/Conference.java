package com.google.devrel.training.conference.domain;

import static com.google.devrel.training.conference.service.OfyService.ofy;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.devrel.training.conference.form.ConferenceForm;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Conference {

	private static final String DEFAULT_CITY = "Default City";
	private static final List<String> DEFAULT_TOPICS = ImmutableList.of("Detault Topic");
	
	//automatic id assignment
	@Id
	private Long id;
	
	//name of the conference
	@Index
	private String name;
	
	private String description;
	
	@Parent
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	private Key<Profile> profileKey;
	
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	private String organizerUserId;
	
	@Index
	private List<String> topics;
	
	@Index
	private String city;
	
	private Date startDate;
	private Date endDate;
	private int month;
	
	@Index
	private int maxAttendees;
	
	@Index
	private int seatsAvailable;
	
	private Conference() {
	}
	
	public Conference(final long id, final String organizerUserId,
					  final ConferenceForm conferenceForm){
		Preconditions.checkNotNull(conferenceForm.getName(), "The name is null");
		this.id = id;
		this.profileKey = Key.create(Profile.class, organizerUserId);
		this.organizerUserId = organizerUserId;
		updateWithConferenceForm(conferenceForm);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the profileKey
	 */
	public Key<Profile> getProfileKey() {
		return profileKey;
	}
	
	public String getWebsafeKey() {
		return Key.create(profileKey, Conference.class, id).getString();
	}

	/**
	 * @return the organizerUserId
	 */
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public String getOrganizerUserId() {
		return organizerUserId;
	}
	
	public String getOrganizerDisplayName() {
		Profile organizer = ofy().load().key(Key.create(Profile.class, organizerUserId)).now();
		if (organizer == null) {
			return organizerUserId;
		} else {
			return organizer.getDisplayName();
		}
	}

	/**
	 * @return the topics
	 */
	public List<String> getTopics() {
		return topics == null ? null : ImmutableList.copyOf(topics);
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate == null ? null : new Date(startDate.getTime());
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		 return endDate == null ? null : new Date(endDate.getTime());
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @return the maxAttendees
	 */
	public int getMaxAttendees() {
		return maxAttendees;
	}

	/**
	 * @return the seatsAvailable
	 */
	public int getSeatsAvailable() {
		return seatsAvailable;
	}
	
	   /**
     * Updates the Conference with ConferenceForm.
     * This method is used upon object creation as well as updating existing Conferences.
     *
     * @param conferenceForm contains form data sent from the client.
     */
    public void updateWithConferenceForm(ConferenceForm conferenceForm) {
        this.name = conferenceForm.getName();
        this.description = conferenceForm.getDescription();
        List<String> topics = conferenceForm.getTopics();
        this.topics = topics == null || topics.isEmpty() ? DEFAULT_TOPICS : topics;
        this.city = conferenceForm.getCity() == null ? DEFAULT_CITY : conferenceForm.getCity();

        Date startDate = conferenceForm.getStartDate();
        this.startDate = startDate == null ? null : new Date(startDate.getTime());
        Date endDate = conferenceForm.getEndDate();
        this.endDate = endDate == null ? null : new Date(endDate.getTime());
        if (this.startDate != null) {
            // Getting the starting month for a composite query.
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.startDate);
            // Calendar.MONTH is zero based, so adding 1.
            this.month = calendar.get(calendar.MONTH) + 1;
        }
        // Check maxAttendees value against the number of already allocated seats.
        int seatsAllocated = maxAttendees - seatsAvailable;
        if (conferenceForm.getMaxAttendees() < seatsAllocated) {
            throw new IllegalArgumentException(seatsAllocated + " seats are already allocated, "
                    + "but you tried to set maxAttendees to " + conferenceForm.getMaxAttendees());
        }
        // The initial number of seatsAvailable is the same as maxAttendees.
        // However, if there are already some seats allocated, we should subtract that numbers.
        this.maxAttendees = conferenceForm.getMaxAttendees();
        this.seatsAvailable = this.maxAttendees - seatsAllocated;
    }

    public void bookSeats(final int number) {
        if (seatsAvailable < number) {
            throw new IllegalArgumentException("There are no seats available.");
        }
        seatsAvailable = seatsAvailable - number;
    }

    public void giveBackSeats(final int number) {
        if (seatsAvailable + number > maxAttendees) {
            throw new IllegalArgumentException("The number of seats will exceeds the capacity.");
        }
        seatsAvailable = seatsAvailable + number;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Id: " + id + "\n")
                .append("Name: ").append(name).append("\n");
        if (city != null) {
            stringBuilder.append("City: ").append(city).append("\n");
        }
        if (topics != null && topics.size() > 0) {
            stringBuilder.append("Topics:\n");
            for (String topic : topics) {
                stringBuilder.append("\t").append(topic).append("\n");
            }
        }
        if (startDate != null) {
            stringBuilder.append("StartDate: ").append(startDate.toString()).append("\n");
        }
        if (endDate != null) {
            stringBuilder.append("EndDate: ").append(endDate.toString()).append("\n");
        }
        stringBuilder.append("Max Attendees: ").append(maxAttendees).append("\n");
        return stringBuilder.toString();
    }
	
}
