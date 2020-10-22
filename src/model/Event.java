package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A representation of a life event.
 */
public class Event implements ModelData {
	private final @NotNull String id;
	private final @NotNull String associatedUsername;
	private final @NotNull String personId;
	private final @Nullable Double latitude;
	private final @Nullable Double longitude;
	private final @Nullable String country;
	private final @Nullable String city;
	private final @NotNull EventType eventType;
	private final int year;
	
	/**
	 * Creates an <code>Event</code> object.
	 * @param id The event's unique ID.
	 * @param associatedUsername The unique ID of the user who created this event.
	 * @param personId The unique ID of the person whom this event describes.
	 * @param latitude The event's latitudinal coordinate.
	 * @param longitude The event's longitudinal coordinate.
	 * @param country The country in which the event took place.
	 * @param city The city in which the event took place.
	 * @param eventType The type of life event this record describes.
	 * @param year The year on the Gregorian calendar in which this event took place.
	 */
	public Event(
		@NotNull String id,
		@NotNull String associatedUsername,
		@NotNull String personId,
		@Nullable Double latitude,
		@Nullable Double longitude,
		@Nullable String country,
		@Nullable String city,
		@NotNull EventType eventType,
		int year
	) {
		this.id = id;
		this.associatedUsername = associatedUsername;
		this.personId = personId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.country = country;
		this.city = city;
		this.eventType = eventType;
		this.year = year;
	}
	
	@Override
	public @NotNull String getId() {
		return id;
	}
	
	public @NotNull String getAssociatedUsername() {
		return associatedUsername;
	}
	
	public @NotNull String getPersonId() {
		return personId;
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
	
	public @NotNull EventType getEventType() {
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
			getId().equals(event.getId()) &&
			getAssociatedUsername().equals(event.getAssociatedUsername()) &&
			getPersonId().equals(event.getPersonId()) &&
			Objects.equals(getLatitude(), event.getLatitude()) &&
			Objects.equals(getLongitude(), event.getLongitude()) &&
			Objects.equals(getCountry(), event.getCountry()) &&
			Objects.equals(getCity(), event.getCity()) &&
			getEventType() == event.getEventType();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getId(), getAssociatedUsername(), getPersonId(), getLatitude(), getLongitude(), getCountry(), getCity(), getEventType(), getYear());
	}
	
	@Override
	public String toString() {
		return "Event{" +
			"id='" + id + '\'' +
			", associatedUsername='" + associatedUsername + '\'' +
			", personId='" + personId + '\'' +
			", latitude=" + latitude +
			", longitude=" + longitude +
			", country='" + country + '\'' +
			", city='" + city + '\'' +
			", eventType=" + eventType +
			", year=" + year +
			'}';
	}
}
