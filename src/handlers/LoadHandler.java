package handlers;

import dao.DatabaseTable;
import database.DataAccessException;
import database.Database;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import requests.LoadRequest;
import services.LoadResult;
import services.LoadService;

/**
 * An object that handles clear-then-load requests.
 */
public class LoadHandler extends Handler<LoadResponse> {
	public LoadHandler() {
		super();
	}
	
	public LoadHandler(@NotNull Database<DatabaseTable> database) {
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
	public @NotNull LoadResponse run(@NotNull String path, @Nullable String userName, @NotNull String req) throws DataAccessException, HandlingFailureException {
		LoadRequest loadRequest = parseJSON(req, LoadRequest.class);
		
		LoadService service = new LoadService(database);
		LoadResult result = service.load(
			loadRequest.getUsers(),
			loadRequest.getPersons(),
			loadRequest.getEvents()
		);
		
		return new LoadResponse(
			result.getUserCount(),
			result.getPersonCount(),
			result.getEventCount()
		);
	}
}
