package handlers;

import com.sun.net.httpserver.HttpExchange;
import dao.DataAccessException;
import model.AuthToken;
import model.Gender;
import org.jetbrains.annotations.NotNull;
import services.RegisterFailureException;
import services.RegisterFailureReason;
import services.RegisterResult;
import services.RegisterService;

import java.io.IOException;

/**
 * An object that handles user registration requests.
 */
public class RegisterHandler extends Handler {
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// Construct and return the HTTP response
	}
	
	/**
	 * Attempts to create a new user in the database and get an authentication token for the user.
	 * @param username The user's identifying name. This must be different from all other usernames.
	 * @param password The user's password.
	 * @param email The user's email.
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
		@NotNull String email,
		@NotNull String firstName,
		@NotNull String lastName,
		@NotNull Gender gender
	) throws RegisterFailureException {
		RegisterRequest req = new RegisterRequest(
			username,
			password,
			email,
			firstName,
			lastName,
			gender
		);
		
		RegisterService service = new RegisterService(database);
		RegisterResult result;
		try {
			result = service.register(req);
		} catch (DataAccessException e) {
			throw new RegisterFailureException(RegisterFailureReason.DATABASE, e);
		}
		
		if (result.getFailureReason() != null) {
			throw new RegisterFailureException(result.getFailureReason(), null);
		}
		if (result.getToken() != null) {
			return result.getToken();
		}
		
		throw new IllegalStateException("There is no case where a fetch result has neither value nor error");
	}
}
