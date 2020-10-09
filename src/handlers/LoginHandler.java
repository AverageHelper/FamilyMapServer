package handlers;

import model.AuthToken;
import org.jetbrains.annotations.NotNull;
import services.LoginFailureException;
import services.LoginResult;
import services.LoginService;

/**
 * An object that handles multiple user login requests.
 */
public class LoginHandler {
	/**
	 * Attempts to get an authentication token for the user.
	 * @param username The user's identifying name.
	 * @param password The user's password.
	 *
	 * @return A new auth token.
	 * @throws LoginFailureException If the login failed.
	 */
	public @NotNull AuthToken login(@NotNull String username, @NotNull String password) throws Exception {
		LoginService service = new LoginService();
		LoginRequest request = new LoginRequest(username, password);
		LoginResult result = service.login(request);
		
		if (result.getFailureReason() != null) {
			throw new LoginFailureException(result.getFailureReason());
		}
		if (result.getToken() != null) {
			return result.getToken();
		}
		
		throw new IllegalStateException("There is no case where a fetch result has neither value nor error");
	}
}
