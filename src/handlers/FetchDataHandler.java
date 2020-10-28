package handlers;

import dao.DataAccessException;
import dao.Database;
import dao.DatabaseTable;
import model.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sqlite.SQLiteErrorCode;
import services.FetchDataFailureException;
import services.FetchDataFailureReason;
import services.FetchDataResult;
import services.FetchDataService;

/**
 * An object that handles data-fetch requests.
 */
public class FetchDataHandler extends Handler {
	
	public FetchDataHandler() {
		super();
	}
	
	public FetchDataHandler(@NotNull Database database) {
		super(database);
	}
	
	@Override
	public @NotNull String expectedHTTPMethod() {
		return "GET";
	}
	
	@Override
	public boolean requiresAuthToken() {
		return true;
	}
	
	@Override
	public @NotNull ErrorResponse run(@NotNull String path, @Nullable String userName, @NotNull String req) throws DataAccessException {
		if (userName == null) {
			throw new DataAccessException(SQLiteErrorCode.SQLITE_AUTH, "No user ID was provided.");
		}
		
		return new ErrorResponse("Not implemented");
	}
	
	/**
	 * Attempts to fetch data about a user with the given <code>id</code>.
	 * @param id The ID of the user to fetch.
	 * @return The resolved <code>User</code> object, or <code>null</code> if the user is not found.
	 * @throws FetchDataFailureException If the fetch fails.
	 */
	public @Nullable User getUserWithId(@NotNull String id) throws FetchDataFailureException {
		FetchDataService service = new FetchDataService();
		FetchDataRequest request = new FetchDataRequest(DatabaseTable.USER);
		FetchDataResult<User> result = service.fetch(request);
		
		if (result.getFailureReason() == FetchDataFailureReason.NOT_FOUND) {
			return null;
		}
		if (result.getFailureReason() != null) {
			throw new FetchDataFailureException(result.getFailureReason());
		}
		if (result.getData() != null) {
			return result.getData().get(0);
		}
		
		throw new IllegalStateException("There is no case where a fetch result has neither value nor error");
	}
}
