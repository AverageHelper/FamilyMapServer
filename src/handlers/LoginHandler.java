package handlers;

import com.sun.net.httpserver.HttpExchange;
import dao.DataAccessException;
import model.AuthToken;
import org.jetbrains.annotations.NotNull;
import services.LoginFailureException;
import services.LoginFailureReason;
import services.LoginResult;
import services.LoginService;

import java.io.IOException;

/**
 * An object that handles user login requests.
 */
public class LoginHandler extends Handler {
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// Construct and return the HTTP response
	}
	
	/**
	 * Attempts to get an authentication token for the user.
	 * @param username The user's identifying name.
	 * @param password The user's password.
	 *
	 * @return A new auth token.
	 * @throws LoginFailureException If the login failed.
	 */
	public @NotNull AuthToken login(
		@NotNull String username,
		@NotNull String password
	) throws Exception {
		LoginRequest req = new LoginRequest(username, password);
		LoginService service = new LoginService(database);
		LoginResult result;
		try {
			result = service.login(req);
		} catch (DataAccessException e) {
			throw new LoginFailureException(LoginFailureReason.DATABASE, e);
		}
		
		if (result.getFailureReason() != null) {
			throw new LoginFailureException(result.getFailureReason(), null);
		}
		if (result.getToken() != null) {
			return result.getToken();
		}
		
		throw new IllegalStateException("There is no case where a fetch result has neither value nor error");
	}
}
