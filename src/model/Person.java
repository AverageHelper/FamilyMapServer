package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A representation of a person connected to a user and a life event.
 */
public class Person implements ModelData {
	private @NotNull String id;
	private @NotNull String associatedUsername;
	private @NotNull String firstName;
	private @NotNull String lastName;
	private @NotNull Gender gender;
	private @Nullable String fatherID;
	private @Nullable String motherID;
	private @Nullable String spouseID;
	
	/**
	 * Creates a <code>Person</code> object.
	 * @param id The person's unique ID.
	 * @param associatedUsername The unique ID of the user who created this person.
	 * @param firstName The person's first name.
	 * @param lastName The person's last name.
	 * @param gender The person's gender.
	 * @param fatherID The ID of the person's father.
	 * @param motherID The ID of the person's mother.
	 * @param spouseID The ID of the person's spouse.
	 */
	public Person(
		@NotNull String id,
		@NotNull String associatedUsername,
		@NotNull String firstName,
		@NotNull String lastName,
		@NotNull Gender gender,
		@Nullable String fatherID,
		@Nullable String motherID,
		@Nullable String spouseID
	) {
		this.id = id;
		this.associatedUsername = associatedUsername;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.fatherID=fatherID;
		this.motherID=motherID;
		this.spouseID=spouseID;
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
	
	public void setAssociatedUsername(@NotNull String associatedUsername) {
		this.associatedUsername = associatedUsername;
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
	
	public @Nullable String getFatherID() {
		return fatherID;
	}
	
	public void setFatherID(@Nullable String fatherID) {
		this.fatherID=fatherID;
	}
	
	public @Nullable String getMotherID() {
		return motherID;
	}
	
	public void setMotherID(@Nullable String motherID) {
		this.motherID=motherID;
	}
	
	public @Nullable String getSpouseID() {
		return spouseID;
	}
	
	public void setSpouseID(@Nullable String spouseID) {
		this.spouseID=spouseID;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Person person = (Person) o;
		return getId().equals(person.getId()) &&
			getAssociatedUsername().equals(person.getAssociatedUsername()) &&
			getFirstName().equals(person.getFirstName()) &&
			getLastName().equals(person.getLastName()) &&
			getGender() == person.getGender() &&
			Objects.equals(getFatherID(), person.getFatherID()) &&
			Objects.equals(getMotherID(), person.getMotherID()) &&
			Objects.equals(getSpouseID(), person.getSpouseID());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getId(), getAssociatedUsername(), getFirstName(), getLastName(), getGender(), getFatherID(), getMotherID(), getSpouseID());
	}
	
	@Override
	public String toString() {
		return "Person{" +
			"id='" + id + '\'' +
			", associatedUsername='" + associatedUsername + '\'' +
			", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", gender=" + gender +
			", fatherId='" + fatherID + '\'' +
			", motherId='" + motherID + '\'' +
			", spouseId='" + spouseID + '\'' +
			'}';
	}
}
