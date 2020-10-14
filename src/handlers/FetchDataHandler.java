package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.TableName;
import model.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import services.FetchDataFailureException;
import services.FetchDataFailureReason;
import services.FetchDataResult;
import services.FetchDataService;

import java.io.IOException;

/**
 * An object that handles multiple data-fetch requests.
 */
public class FetchDataHandler implements HttpHandler {
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// Construct and return the HTTP response
	}
	
	/**
	 * Attempts to fetch data about a user with the given <code>id</code>.
	 * @param id The ID of the user to fetch.
	 * @return The resolved <code>User</code> object, or <code>null</code> if the user is not found.
	 * @throws FetchDataFailureException If the fetch fails.
	 */
	public @Nullable User getUserWithId(@NotNull String id) throws FetchDataFailureException {
		FetchDataService service = new FetchDataService();
		FetchDataRequest request = new FetchDataRequest(TableName.USER);
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
