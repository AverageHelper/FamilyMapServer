package model;

import handlers.JSONSerialization;
import handlers.MissingKeyException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A representation of a life event.
 */
public class Event extends JSONSerialization implements ModelData {
	private final @NotNull String eventID;
	private final @NotNull String associatedUsername;
	private final @NotNull String personID;
	private final @Nullable Double latitude;
	private final @Nullable Double longitude;
	private final @Nullable String country;
	private final @Nullable String city;
//	private @NotNull EventType eventType;
	private @NotNull String eventType;
	private final int year;
	
	/**
	 * Creates an <code>Event</code> object.
	 * @param eventID The event's unique ID.
	 * @param associatedUsername The unique ID of the user who created this event.
	 * @param personID The unique ID of the person whom this event describes.
	 * @param latitude The event's latitudinal coordinate.
	 * @param longitude The event's longitudinal coordinate.
	 * @param country The country in which the event took place.
	 * @param city The city in which the event took place.
	 * @param eventType The type of life event this record describes.
	 * @param year The year on the Gregorian calendar in which this event took place.
	 */
	public Event(
		@NotNull String eventID,
		@NotNull String associatedUsername,
		@NotNull String personID,
		@Nullable Double latitude,
		@Nullable Double longitude,
		@Nullable String country,
		@Nullable String city,
		@NotNull String eventType,
		int year
	) {
		this.eventID = eventID;
		this.associatedUsername = associatedUsername;
		this.personID = personID;
		this.latitude = latitude;
		this.longitude = longitude;
		this.country = country;
		this.city = city;
		this.eventType = eventType;
		this.year = year;
	}
	
	@Override
	public @NotNull String getId() {
		return eventID;
	}
	
	public @NotNull String getEventID() {
		return eventID;
	}
	
	public @NotNull String getAssociatedUsername() {
		return associatedUsername;
	}
	
	public @NotNull String getPersonID() {
		return personID;
	}
	
	public @Nullable Double getLatitude() {
		return latitude;
	}
	
	public @Nullable Double getLongitude() {
		return longitude;
	}
	
	public @Nullable String getCountry() {
		return country;
	}
	
	public @Nullable String getCity() {
		return city;
	}
	
	public @NotNull String getEventType() {
		return eventType;
	}
	
	public int getYear() {
		return year;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Event event = (Event) o;
		return getYear() == event.getYear() &&
			getEventID().equals(event.getEventID()) &&
			getAssociatedUsername().equals(event.getAssociatedUsername()) &&
			getPersonID().equals(event.getPersonID()) &&
			Objects.equals(getLatitude(), event.getLatitude()) &&
			Objects.equals(getLongitude(), event.getLongitude()) &&
			Objects.equals(getCountry(), event.getCountry()) &&
			Objects.equals(getCity(), event.getCity()) &&
			getEventType().equals(event.getEventType());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getPersonID(), getAssociatedUsername(), getPersonID(), getLatitude(), getLongitude(), getCountry(), getCity(), getEventType(), getYear());
	}
	
	@Override
	public String toString() {
		return "Event{" +
			"id='" + eventID + '\'' +
			", associatedUsername='" + associatedUsername + '\'' +
			", personId='" + personID + '\'' +
			", latitude=" + latitude +
			", longitude=" + longitude +
			", country='" + country + '\'' +
			", city='" + city + '\'' +
			", eventType=" + eventType +
			", year=" + year +
			'}';
	}
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public void assertCorrectDeserialization() throws MissingKeyException {
		if (eventID == null) {
			throw new MissingKeyException("eventID");
		}
		if (associatedUsername == null) {
			throw new MissingKeyException("associatedUsername");
		}
		if (personID == null) {
			throw new MissingKeyException("personID");
		}
		if (eventType == null) {
			throw new MissingKeyException("eventType");
//			eventType = EventType.BIRTH;
		}
	}
}
