package handlers;

import model.AuthToken;
import model.Gender;
import org.jetbrains.annotations.NotNull;
import services.RegisterFailureException;
import services.RegisterFailureReason;
import services.RegisterResult;
import services.RegisterService;

/**
 * An object that handles multiple user registration requests.
 */
public class RegisterHandler {
	/**
	 * Attempts to create a new user in the database and get an authentication token for the user.
	 * @param username The user's identifying name. This must be different from all other usernames.
	 * @param password The user's password.
	 * @param firstName The user's first name.
	 * @param lastName The user's surname.
	 * @param gender The user's gender.
	 *
	 * @return A new auth token.
	 * @throws RegisterFailureException If the login failed.
	 */
	public @NotNull AuthToken register(
		@NotNull String username,
		@NotNull String password,
		@NotNull String firstName,
		@NotNull String lastName,
		@NotNull Gender gender
	) throws RegisterFailureException {
		RegisterService service = new RegisterService();
		RegisterRequest request = new RegisterRequest(username, password, firstName, lastName, gender);
		RegisterResult result = service.register(request);
		
		if (result.getFailureReason() != null) {
			throw new RegisterFailureException(result.getFailureReason());
		}
		if (result.getToken() != null) {
			return result.getToken();
		}
		
		throw new IllegalStateException("There is no case where a fetch result has neither value nor error");
	}
}
