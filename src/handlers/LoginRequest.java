package handlers;

import transport.JSONSerialization;
import transport.MissingKeyException;
import org.jetbrains.annotations.NotNull;

/**
 * Data formatted to request an authorization token from the server.
 */
public class LoginRequest extends JSONSerialization {
	private final @NotNull String userName;
	private final @NotNull String password;
	
	/**
	 * Creates a <code>LoginRequest</code> object.
	 * @param userName The user's unique username.
	 * @param password The user's password.
	 */
	public LoginRequest(@NotNull String userName, @NotNull String password) {
		this.userName = userName;
		this.password = password;
	}
	
	/**
	 * @return The username to use to log in.
	 */
	public @NotNull String getUserName() {
		return userName;
	}
	
	/**
	 * @return The password to use to log in.
	 */
	public @NotNull String getPassword() {
		return password;
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
	}
}
