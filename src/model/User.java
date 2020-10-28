package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A representation of a unique Family Map user.
 */
public class User implements ModelData {
	private @NotNull String userName;
	private @NotNull String password;
	private @NotNull String email;
	private @NotNull String firstName;
	private @NotNull String lastName;
	private @NotNull Gender gender;
	private @Nullable String personID;
	
	/**
	 * Creates a <code>User</code> object.
	 * @param userName The user's unique username.
	 * @param password The hash of the user's password.
	 * @param email The user's email address.
	 * @param firstName The person's first name.
	 * @param lastName The person's last name.
	 * @param gender The person's gender.
	 * @param personID The user's associated Person entry.
	 */
	public User(
		@NotNull String userName,
		@NotNull String password,
		@NotNull String email,
		@NotNull String firstName,
		@NotNull String lastName,
		@NotNull Gender gender,
		@Nullable String personID
	) {
		this.userName=userName;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.personID=personID;
	}
	
	@Override
	public @NotNull String getId() {
		return userName;
	}
	
	public @NotNull String getUserName() {
		return userName;
	}
	
	public void setUserName(@NotNull String userName) {
		this.userName=userName;
	}
	
	public @NotNull String getPassword() {
		return password;
	}
	
	public void setPassword(@NotNull String password) {
		this.password = password;
	}
	
	public @NotNull String getEmail() {
		return this.email;
	}
	
	public void setEmail(@NotNull String email) {
		this.email = email;
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
	
	public @Nullable String getPersonID() {
		return personID;
	}
	
	public void setPersonID(@Nullable String personID) {
		this.personID=personID;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return getUserName().equals(user.getUserName()) &&
			getPassword().equals(user.getPassword()) &&
			getEmail().equals(user.getEmail()) &&
			getFirstName().equals(user.getFirstName()) &&
			getLastName().equals(user.getLastName()) &&
			getGender() == user.getGender() &&
			Objects.equals(getPersonID(), user.getPersonID());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getUserName(), getPassword(), getEmail(), getFirstName(), getLastName(), getGender(), getPersonID());
	}
	
	@Override
	public String toString() {
		return "User{" +
			"username='" + userName + '\'' +
			", password='" + password + '\'' +
			", email='" + email + '\'' +
			", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", gender=" + gender +
			", personId='" + personID + '\'' +
			'}';
	}
}
