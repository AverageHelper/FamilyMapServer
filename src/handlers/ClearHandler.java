package handlers;

import dao.DataAccessException;
import dao.Database;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import services.ClearService;

/**
 * An object that handles clear-database requests.
 */
public class ClearHandler extends Handler<ClearResponse> {
	
	public ClearHandler() {
		super();
	}
	
	public ClearHandler(@NotNull Database database) {
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
	public @NotNull ClearResponse run(@NotNull String path, @Nullable String userName, @NotNull String req) throws DataAccessException {
		return clear();
	}
	
	/**
	 * Attempts to clear the database.
	 * @return An object that represents the successful clearing of the database.
	 * @throws DataAccessException If the operation fails.
	 */
	public @NotNull ClearResponse clear() throws DataAccessException {
		ClearService service = new ClearService(database);
		service.clear();
		return new ClearResponse();
	}
}
