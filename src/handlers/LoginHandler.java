package handlers;

import dao.DataAccessException;
import dao.Database;
import model.AuthToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import services.LoginResult;
import services.LoginService;
import utilities.Pair;

/**
 * An object that handles user login requests.
 */
public class LoginHandler extends Handler<LoginResponse> {
	
	public LoginHandler() {
		super();
	}
	
	public LoginHandler(@NotNull Database database) {
		super(database);
	}
	
	@Override
	public @NotNull String expectedHTTPMethod() {
		return "POST";
	}
	
	@Override
	public boolean requiresAuthToken() {
		return false;
	}
	
	@Override
	public @NotNull LoginResponse run(@NotNull String path, @Nullable String userName, @NotNull String req) throws DataAccessException, HandlingFailureException {
		LoginRequest loginRequest = parseJSON(req, LoginRequest.class);
		
		Pair<AuthToken, String> results = login(
			loginRequest.getUserName(),
			loginRequest.getPassword()
		);
		
		AuthToken authToken = results.getFirst();
		String personID = results.getSecond();
		return new LoginResponse(authToken.getId(), authToken.getAssociatedUsername(), personID);
	}
	
	/**
	 * Attempts to get an authentication token for the user.
	 * @param username The user's identifying name.
	 * @param password The user's password.
	 *
	 * @return A new auth token and the user's associated <code>Person</code> ID.
	 * @throws HandlingFailureException If the login failed.
	 * @throws DataAccessException If there was a problem accessing the database.
	 */
	public @NotNull Pair<@NotNull AuthToken, @NotNull String> login(
		@NotNull String username,
		@NotNull String password
	) throws HandlingFailureException, DataAccessException {
		LoginRequest req = new LoginRequest(username, password);
		LoginService service = new LoginService(database);
		LoginResult result = service.login(req);
		
		if (result.getFailureReason() != null) {
			throw HandlingFailureException.from(result.getFailureReason());
		}
		if (result.getToken() != null && result.getPersonID() != null) {
			return new Pair<>(result.getToken(), result.getPersonID());
		}
		
		throw new IllegalStateException("There is no case where a fetch result has neither value nor error");
	}
}
