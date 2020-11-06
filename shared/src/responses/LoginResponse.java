package responses;

import transport.JSONSerialization;
import transport.MissingKeyException;
import org.jetbrains.annotations.NotNull;

public class LoginResponse extends JSONSerialization {
	private final @NotNull String authToken;
	private final @NotNull String userName;
	private final @NotNull String personID;
	private final boolean success = true;
	
	/**
	 * Creates a new <code>LoginResponse</code> object.
	 *
	 * @param authToken A non-empty auth token string.
	 * @param userName The user name passed in with the request.
	 * @param personID A non-empty string containing the Person ID of the user’s <code>Person</code> object.
	 */
	public LoginResponse(
		@NotNull String authToken,
		@NotNull String userName,
		@NotNull String personID
	) {
		this.authToken = authToken;
		this.userName = userName;
		this.personID = personID;
	}
	
	public @NotNull String getAuthToken() {
		return authToken;
	}
	
	public @NotNull String getUserName() {
		return userName;
	}
	
	public @NotNull String getPersonID() {
		return personID;
	}
	
	public boolean isSuccessful() {
		return success;
	}
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public void assertCorrectDeserialization() throws MissingKeyException {
		if (this.authToken == null) {
			throw new MissingKeyException("authToken");
		}
		if (this.userName == null) {
			throw new MissingKeyException("userName");
		}
		if (this.personID == null) {
			throw new MissingKeyException("personID");
		}
	}
}
