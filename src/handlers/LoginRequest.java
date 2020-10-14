package handlers;

import org.jetbrains.annotations.NotNull;

/**
 * Data formatted to request an authorization token from the server.
 */
public class LoginRequest extends JSONSerialization {
	private final @NotNull String username;
	private final @NotNull String password;
	
	/**
	 * Creates a <code>LoginRequest</code> object.
	 * @param username The user's unique username.
	 * @param password The user's password.
	 */
	public LoginRequest(@NotNull String username, @NotNull String password) {
		this.username = username;
		this.password = password;
	}
	
	/**
	 * @return The username to use to log in.
	 */
	public @NotNull String getUsername() {
		return username;
	}
	
	/**
	 * @return The password to use to log in.
	 */
	public @NotNull String getPassword() {
		return password;
	}
}
