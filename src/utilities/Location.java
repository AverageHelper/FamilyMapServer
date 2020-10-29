package utilities;

import org.jetbrains.annotations.NotNull;

public class Location {
	private final @NotNull String country;
	private final @NotNull String city;
	private final double latitude;
	private final double longitude;
	
	public Location(
		@NotNull String country,
		@NotNull String city,
		double latitude,
		double longitude
	) {
		this.country = country;
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public @NotNull String getCountry() {
		return country;
	}
	
	public @NotNull String getCity() {
		return city;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
}
