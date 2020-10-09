package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A representation of a person connected to a user and a life event.
 */
public class Person implements ModelData {
	private @NotNull String id;
	private @NotNull String userId;
	private @NotNull String firstName;
	private @NotNull String lastName;
	private @NotNull Gender gender;
	private @Nullable String fatherId;
	private @Nullable String motherId;
	private @Nullable String spouseId;
	
	/**
	 * Creates a <code>Person</code> object.
	 * @param id The person's unique ID.
	 * @param userId The unique ID of the user who created this person.
	 * @param firstName The person's first name.
	 * @param lastName The person's last name.
	 * @param gender The person's gender.
	 * @param fatherId The ID of the person's father.
	 * @param motherId The ID of the person's mother.
	 * @param spouseId The ID of the person's spouse.
	 */
	public Person(
		@NotNull String id,
		@NotNull String userId,
		@NotNull String firstName,
		@NotNull String lastName,
		@NotNull Gender gender,
		@Nullable String fatherId,
		@Nullable String motherId,
		@Nullable String spouseId
	) {
		this.id = id;
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.fatherId = fatherId;
		this.motherId = motherId;
		this.spouseId = spouseId;
	}
	
	public @NotNull String getId() {
		return id;
	}
	
	public void setId(@NotNull String id) {
		this.id = id;
	}
	
	public @NotNull String getUserId() {
		return userId;
	}
	
	public void setUserId(@NotNull String userId) {
		this.userId = userId;
	}
	
	public @NotNull String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(@NotNull String firstName) {
		this.firstName = firstName;
	}
	
	public @NotNull String getLastName() {
		return lastName;
	}
	
	public void setLastName(@NotNull String lastName) {
		this.lastName = lastName;
	}
	
	public @NotNull Gender getGender() {
		return gender;
	}
	
	public void setGender(@NotNull Gender gender) {
		this.gender = gender;
	}
	
	public @Nullable String getFatherId() {
		return fatherId;
	}
	
	public void setFatherId(@Nullable String fatherId) {
		this.fatherId = fatherId;
	}
	
	public @Nullable String getMotherId() {
		return motherId;
	}
	
	public void setMotherId(@Nullable String motherId) {
		this.motherId = motherId;
	}
	
	public @Nullable String getSpouseId() {
		return spouseId;
	}
	
	public void setSpouseId(@Nullable String spouseId) {
		this.spouseId = spouseId;
	}
}
