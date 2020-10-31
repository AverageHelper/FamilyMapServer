package handlers;

import dao.DataAccessException;
import dao.Database;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import services.FillResult;
import services.FillService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * An object that handles database-fill requests.
 */
public class FillHandler extends Handler<FillResponse> {
	
	public FillHandler() {
		super();
	}
	
	public FillHandler(@NotNull Database database) {
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
	public @NotNull FillResponse run(@NotNull String path, @Nullable String userName, @NotNull String req) throws DataAccessException, HandlingFailureException, IOException {
		// Path: /fill/[username]/{generations}
		// Parse the path for the username and generation count
		List<String> components = new ArrayList<>(Arrays.asList(path.split(Pattern.quote("/"))));
		
		// Strip "/"
		components.remove(0);
		// Strip "fill"
		components.remove(0);
		if (components.size() < 1) {
			throw new HandlingFailureException(HandlingFailureReason.TOO_FEW_PATH_COMPONENTS);
		}
		
		// Get /[username]
		userName = components.get(0);
		
		// Get /{generations}
		Integer generations = null;
		if (components.size() > 1) {
			try {
				generations = Integer.parseInt(components.get(1));
				if (generations < 0) {
					throw new NumberFormatException("`generations` should be non-negative.");
				}
				
			} catch (NumberFormatException e) {
				throw new HandlingFailureException(HandlingFailureReason.MISTYPED_PATH_COMPONENT, e);
			}
		}
		
		return fill(userName, generations);
	}
	
	/**
	 * Populates the server's database with generated data for the specified user name. The required
	 * <code>username</code> parameter must be a user already registered with the server. If there
	 * is any data in the database already associated with the given user name, it is deleted. The
	 * optional <code>generations</code> parameter lets the caller specify the number of
	 * generations of ancestors to be generated, and must be a non-negative integer (the default is
	 * 4, which results in 31 new persons each with associated events).
	 *
	 * @param username The ID of the user with which to associate the new family tree.
	 * @param generations If provided, the number of generations to fill.
	 * @return The result of the fill operation
	 * @throws DataAccessException An exception if there is an error accessing the database.
	 * @throws HandlingFailureException An exception if there was a problem handling the request.
	 * @throws IOException An exception if there was a problem reading from local storage.
	 */
	public @NotNull FillResponse fill(@NotNull String username, @Nullable Integer generations) throws HandlingFailureException, DataAccessException, IOException {
		int generationCount = 4;
		if (generations != null) {
			generationCount = generations;
		}
		assert generationCount >= 0;
		assert !username.isEmpty();
		
		FillService service = new FillService(database);
		FillResult result = service.fill(username, generationCount);
		
		if (result.getFailureReason() != null) {
			throw HandlingFailureException.from(result.getFailureReason());
		}
		if (result.getPersonCount() != null && result.getEventCount() != null) {
			return new FillResponse(
				result.getPersonCount(),
				result.getEventCount()
			);
		}
		
		throw new IllegalStateException("There is no case where a fetch result has neither value nor error");
	}
}
