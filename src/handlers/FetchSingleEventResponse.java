package handlers;

import model.EventType;
import org.jetbrains.annotations.NotNull;

public class FetchSingleEventResponse extends JSONSerialization {
	private final @NotNull String associatedUsername;
	private final @NotNull String eventID;
	private final @NotNull String personID;
	private final @NotNull Double latitude;
	private final @NotNull Double longitude;
	private final @NotNull String country;
	private final @NotNull String city;
	private final @NotNull EventType eventType;
	private final int year;
	private final boolean success;
	
	public FetchSingleEventResponse(
		@NotNull String associatedUsername,
		@NotNull String eventID,
		@NotNull String personID,
		@NotNull Double latitude,
		@NotNull Double longitude,
		@NotNull String country,
		@NotNull String city,
		@NotNull EventType eventType,
		int year
	) {
		this.associatedUsername = associatedUsername;
		this.eventID = eventID;
		this.personID = personID;
		this.latitude = latitude;
		this.longitude = longitude;
		this.country = country;
		this.city = city;
		this.eventType = eventType;
		this.year = year;
		this.success = true;
	}
	
	public @NotNull String getAssociatedUsername() {
		return associatedUsername;
	}
	
	public @NotNull String getEventID() {
		return eventID;
	}
	
	public @NotNull String getPersonID() {
		return personID;
	}
	
	public @NotNull Double getLatitude() {
		return latitude;
	}
	
	public @NotNull Double getLongitude() {
		return longitude;
	}
	
	public @NotNull String getCountry() {
		return country;
	}
	
	public @NotNull String getCity() {
		return city;
	}
	
	public @NotNull EventType getEventType() {
		return eventType;
	}
	
	public int getYear() {
		return year;
	}
	
	public boolean isSuccessful() {
		return success;
	}
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public void assertCorrectDeserialization() throws MissingKeyException {
		if (this.associatedUsername == null) {
			throw new MissingKeyException("associatedUsername");
		}
		if (this.eventID == null) {
			throw new MissingKeyException("eventID");
		}
		if (this.personID == null) {
			throw new MissingKeyException("personID");
		}
		if (this.latitude == null) {
			throw new MissingKeyException("latitude");
		}
		if (this.longitude == null) {
			throw new MissingKeyException("longitude");
		}
		if (this.country == null) {
			throw new MissingKeyException("country");
		}
		if (this.city == null) {
			throw new MissingKeyException("city");
		}
		if (this.eventType == null) {
			throw new MissingKeyException("eventType");
		}
	}
}
