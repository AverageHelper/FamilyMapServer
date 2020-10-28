package handlers;

import model.Gender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FetchSinglePersonRequest extends JSONSerialization {
	private final @NotNull String associatedUsername;
	private final @NotNull String personID;
	private final @NotNull String firstName;
	private final @NotNull String lastName;
	private final @NotNull Gender gender;
	private final @Nullable String fatherID;
	private final @Nullable String motherID;
	private final @Nullable String spouseID;
	private final boolean success;
	
	public FetchSinglePersonRequest(
		@NotNull String associatedUsername,
		@NotNull String personID,
		@NotNull String firstName,
		@NotNull String lastName,
		@NotNull Gender gender,
		@Nullable String fatherID,
		@Nullable String motherID,
		@Nullable String spouseID
	) {
		this.associatedUsername = associatedUsername;
		this.personID = personID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.fatherID = fatherID;
		this.motherID = motherID;
		this.spouseID = spouseID;
		this.success = true;
	}
	
	public @NotNull String getAssociatedUsername() {
		return associatedUsername;
	}
	
	public @NotNull String getPersonID() {
		return personID;
	}
	
	public @NotNull String getFirstName() {
		return firstName;
	}
	
	public @NotNull String getLastName() {
		return lastName;
	}
	
	public @NotNull Gender getGender() {
		return gender;
	}
	
	public @Nullable String getFatherID() {
		return fatherID;
	}
	
	public @Nullable String getMotherID() {
		return motherID;
	}
	
	public @Nullable String getSpouseID() {
		return spouseID;
	}
	
	public boolean isSuccessful() {
		return success;
	}
}
