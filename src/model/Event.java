package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A representation of a life event.
 */
public class Event implements ModelData {
	private @NotNull String id;
	private @NotNull String associatedUsername;
	private @NotNull String personId;
	private @Nullable Double latitude;
	private @Nullable Double longitude;
	private @Nullable String country;
	private @Nullable String city;
	private @NotNull EventType eventType;
	private int year;
	
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
	
	public void setId(@NotNull String id) {
		this.id = id;
	}
	
	public @NotNull String getAssociatedUsername() {
		return associatedUsername;
	}
	
	public void setAssociatedUsername(@NotNull String userId) {
		this.associatedUsername = associatedUsername;
	}
	
	public @NotNull String getPersonId() {
		return personId;
	}
	
	public void setPersonId(@NotNull String personId) {
		this.personId = personId;
	}
	
	public @Nullable Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(@Nullable Double latitude) {
		this.latitude = latitude;
	}
	
	public @Nullable Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(@Nullable Double longitude) {
		this.longitude = longitude;
	}
	
	public @Nullable String getCountry() {
		return country;
	}
	
	public void setCountry(@Nullable String country) {
		this.country = country;
	}
	
	public @Nullable String getCity() {
		return city;
	}
	
	public void setCity(@Nullable String city) {
		this.city = city;
	}
	
	public @NotNull EventType getEventType() {
		return eventType;
	}
	
	public void setEventType(@NotNull EventType eventType) {
		this.eventType = eventType;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
}
