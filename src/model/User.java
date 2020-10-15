package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A representation of a unique Family Map user.
 */
public class User implements ModelData {
	private @NotNull String username;
	private @NotNull String password;
	private @NotNull String email;
	private @NotNull String firstName;
	private @NotNull String lastName;
	private @NotNull Gender gender;
	private @Nullable String personId;
	
	/**
	 * Creates a <code>User</code> object.
	 * @param username The user's unique username.
	 * @param password The hash of the user's password.
	 * @param email The user's email address.
	 * @param firstName The person's first name.
	 * @param lastName The person's last name.
	 * @param gender The person's gender.
	 * @param personId The user's associated Person entry.
	 */
	public User(
		@NotNull String username,
		@NotNull String password,
		@NotNull String email,
		@NotNull String firstName,
		@NotNull String lastName,
		@NotNull Gender gender,
		@Nullable String personId
	) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.personId = personId;
	}
	
	@Override
	public @NotNull String getId() {
		return username;
	}
	
	public @NotNull String getUsername() {
		return username;
	}
	
	public void setUsername(@NotNull String username) {
		this.username = username;
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
	
	public @Nullable String getPersonId() {
		return personId;
	}
	
	public void setPersonId(@Nullable String personId) {
		this.personId = personId;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return getUsername().equals(user.getUsername()) &&
			getPassword().equals(user.getPassword()) &&
			getEmail().equals(user.getEmail()) &&
			getFirstName().equals(user.getFirstName()) &&
			getLastName().equals(user.getLastName()) &&
			getGender() == user.getGender() &&
			Objects.equals(getPersonId(), user.getPersonId());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getUsername(), getPassword(), getEmail(), getFirstName(), getLastName(), getGender(), getPersonId());
	}
}
