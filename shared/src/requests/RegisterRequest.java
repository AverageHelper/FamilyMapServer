package requests;

import model.Gender;
import transport.JSONSerialization;
import transport.MissingKeyException;
import org.jetbrains.annotations.NotNull;

/**
 * Data formatted to request the creation of a new user in the database and a new authorization token.
 */
public class RegisterRequest extends JSONSerialization {
	private final @NotNull String userName;
	private final @NotNull String password;
	private final @NotNull String email;
	private final @NotNull String firstName;
	private final @NotNull String lastName;
	private final @NotNull Gender gender;
	
	/**
	 * Creates a <code>RegisterRequest</code> object.
	 * @param userName The user's unique username.
	 * @param password The user's password.
	 * @param email The user's password.
	 * @param firstName The user's first name.
	 * @param lastName The user's last name.
	 * @param gender The user's gender.
	 */
	public RegisterRequest(
		@NotNull String userName,
		@NotNull String password,
		@NotNull String email,
		@NotNull String firstName,
		@NotNull String lastName,
		@NotNull Gender gender
	) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
	}
	
	public @NotNull String getUserName() {
		return userName;
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
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public void assertCorrectDeserialization() throws MissingKeyException {
		if (this.userName == null) {
			throw new MissingKeyException("userName");
		}
		if (this.password == null) {
			throw new MissingKeyException("password");
		}
		if (this.email == null) {
			throw new MissingKeyException("email");
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
