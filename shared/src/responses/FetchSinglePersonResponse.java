package responses;

import model.Gender;
import transport.MissingKeyException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FetchSinglePersonResponse extends FetchDataResponse {
	private final @NotNull String associatedUsername;
	private final @NotNull String personID;
	private final @NotNull String firstName;
	private final @NotNull String lastName;
	private final @NotNull Gender gender;
	private final @Nullable String fatherID;
	private final @Nullable String motherID;
	private final @Nullable String spouseID;
	
	public FetchSinglePersonResponse(
		@NotNull String associatedUsername,
		@NotNull String personID,
		@NotNull String firstName,
		@NotNull String lastName,
		@NotNull Gender gender,
		@Nullable String fatherID,
		@Nullable String motherID,
		@Nullable String spouseID
	) {
		super();
		this.associatedUsername = associatedUsername;
		this.personID = personID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.fatherID = fatherID;
		this.motherID = motherID;
		this.spouseID = spouseID;
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
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public void assertCorrectDeserialization() throws MissingKeyException {
		if (this.associatedUsername == null) {
			throw new MissingKeyException("associatedUsername");
		}
		if (this.personID == null) {
			throw new MissingKeyException("personID");
		}
		if (this.firstName == null) {
			throw new MissingKeyException("firstName");
		}
		if (this.lastName == null) {
			throw new MissingKeyException("lastName");
		}
		if (this.gender == null) {
			throw new MissingKeyException("gender");
		}
	}
}
