package handlers;

import model.Gender;
import org.jetbrains.annotations.NotNull;

/**
 * Data formatted to request the creation of a new user in the database and a new authorization token.
 */
public class RegisterRequest extends JSONSerialization {
	private final @NotNull String username;
	private final @NotNull String password;
	private final @NotNull String email;
	private final @NotNull String firstName;
	private final @NotNull String lastName;
	private final @NotNull Gender gender;
	
	/**
	 * Creates a <code>RegisterRequest</code> object.
	 * @param username The user's unique username.
	 * @param password The user's password.
	 * @param email The user's password.
	 * @param firstName The user's first name.
	 * @param lastName The user's last name.
	 * @param gender The user's gender.
	 */
	public RegisterRequest(
		@NotNull String username,
		@NotNull String password,
		@NotNull String email,
		@NotNull String firstName,
		@NotNull String lastName,
		@NotNull Gender gender
	) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
	}
	
	public @NotNull String getUsername() {
		return username;
	}
	
	public @NotNull String getPassword() {
		return password;
	}
	
	public @NotNull String getEmail() {
		return email;
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
}
