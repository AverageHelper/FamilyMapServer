package handlers;

import transport.MissingKeyException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FetchSingleEventResponse extends FetchDataResponse {
	private final @NotNull String associatedUsername;
	private final @NotNull String eventID;
	private final @NotNull String personID;
	private final @Nullable Double latitude;
	private final @Nullable Double longitude;
	private final @Nullable String country;
	private final @Nullable String city;
	private final @NotNull String eventType;
	private final int year;
	
	public FetchSingleEventResponse(
		@NotNull String associatedUsername,
		@NotNull String eventID,
		@NotNull String personID,
		@Nullable Double latitude,
		@Nullable Double longitude,
		@Nullable String country,
		@Nullable String city,
		@NotNull String eventType,
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
		if (this.eventType == null) {
			throw new MissingKeyException("eventType");
		}
	}
}
