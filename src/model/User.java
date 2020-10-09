package model;

import org.jetbrains.annotations.NotNull;

/**
 * A representation of a unique Family Map user.
 */
public class User implements ModelData {
	private @NotNull String id;
	private @NotNull String username;
	private @NotNull String passwordHash;
	private @NotNull String firstName;
	private @NotNull String lastName;
	private @NotNull Gender gender;
	
	/**
	 * Creates a <code>User</code> object.
	 * @param id The person's unique ID.
	 * @param username The user's unique username.
	 * @param passwordHash The hash of the user's password.
	 * @param firstName The person's first name.
	 * @param lastName The person's last name.
	 * @param gender The person's gender.
	 */
	public User(
		@NotNull String id,
		@NotNull String username,
		@NotNull String passwordHash,
		@NotNull String firstName,
		@NotNull String lastName,
		@NotNull Gender gender
	) {
		this.id = id;
		this.username = username;
		this.passwordHash = passwordHash;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
	}
	
	public @NotNull String getId() {
		return id;
	}
	
	public void setId(@NotNull String id) {
		this.id = id;
	}
	
	public @NotNull String getUsername() {
		return username;
	}
	
	public void setUsername(@NotNull String username) {
		this.username = username;
	}
	
	public @NotNull String getPasswordHash() {
		return passwordHash;
	}
	
	public void setPasswordHash(@NotNull String passwordHash) {
		this.passwordHash = passwordHash;
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
}
