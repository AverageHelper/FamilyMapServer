package handlers;

import org.jetbrains.annotations.NotNull;

public class RegisterResponse extends LoginResponse {
	/**
	 * Creates a new <code>RegisterResponse</code> object.
	 *
	 * @param authToken A non-empty auth token string.
	 * @param userName The user name passed in with the request.
	 * @param personID A non-empty string containing the Person ID of the user’s generated <code>Person</code> object.
	 */
	public RegisterResponse(
		@NotNull String authToken,
		@NotNull String userName,
		@NotNull String personID
	) {
		super(authToken, userName, personID);
	}
}
