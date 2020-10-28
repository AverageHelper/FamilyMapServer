package handlers;

import dao.DataAccessException;
import dao.Database;
import model.AuthToken;
import model.Gender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import services.RegisterResult;
import services.RegisterService;
import utilities.Pair;

/**
 * An object that handles user registration requests.
 */
public class RegisterHandler extends Handler<RegisterResponse> {
	
	public RegisterHandler() {
		super();
	}
	
	public RegisterHandler(@NotNull Database database) {
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
	public @NotNull RegisterResponse run(@NotNull String path, @Nullable String userName, @NotNull String req) throws DataAccessException, HandlingFailureException {
		RegisterRequest request = parseJSON(req, RegisterRequest.class);
		
		Pair<AuthToken, String> results = register(
			request.getUserName(),
			request.getPassword(),
			request.getEmail(),
			request.getFirstName(),
			request.getLastName(),
			request.getGender()
		);
		
		AuthToken authToken = results.getFirst();
		String personID = results.getSecond();
		return new RegisterResponse(authToken.getId(), authToken.getAssociatedUsername(), personID);
	}
	
	/**
	 * Attempts to create a new user in the database and get an authentication token for the user.
	 * @param userName The user's identifying name. This must be different from all other usernames.
	 * @param password The user's password.
	 * @param email The user's email.
	 * @param firstName The user's first name.
	 * @param lastName The user's surname.
	 * @param gender The user's gender.
	 *
	 * @return A new auth token and the ID of the user's generated <code>Person</code>.
	 * @throws HandlingFailureException If the login failed.
	 * @throws DataAccessException If there was a problem accessing the database.
	 */
	public @NotNull Pair<@NotNull AuthToken, @NotNull String> register(
		@NotNull String userName,
		@NotNull String password,
		@NotNull String email,
		@NotNull String firstName,
		@NotNull String lastName,
		@NotNull Gender gender
	) throws HandlingFailureException, DataAccessException {
		RegisterRequest req = new RegisterRequest(
			notEmptyValue("userName", userName),
			notEmptyValue("password", password),
			notEmptyValue("email", email),
			notEmptyValue("firstName", firstName),
			notEmptyValue("lastName", lastName),
			gender
		);
		
		RegisterService service = new RegisterService(database);
		RegisterResult result;
		result = service.register(req);
		
		if (result.getFailureReason() != null) {
			throw HandlingFailureException.from(result.getFailureReason());
		}
		if (result.getToken() != null && result.getPersonID() != null) {
			return new Pair<>(result.getToken(), result.getPersonID());
		}
		
		throw new IllegalStateException("There is no case where a fetch result has neither value nor error");
	}
}
