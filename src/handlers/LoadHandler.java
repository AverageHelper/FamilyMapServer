package handlers;

import dao.*;
import model.Event;
import model.Person;
import model.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An object that handles clear-then-load requests.
 */
public class LoadHandler extends Handler<LoadResponse> {
	public LoadHandler() {
		super();
	}
	
	public LoadHandler(@NotNull Database database) {
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
		
		database.clearTables();
		
		database.runTransaction(conn -> {
			UserDao userDao = new UserDao(conn);
			PersonDao personDao = new PersonDao(conn);
			EventDao eventDao = new EventDao(conn);
			
			for (User user : loadRequest.getUsers()) {
				userDao.insert(user);
			}
			for (Person person : loadRequest.getPersons()) {
				personDao.insert(person);
			}
			for (Event event : loadRequest.getEvents()) {
				eventDao.insert(event);
			}
			
			return true;
		});
		
		return new LoadResponse(
			loadRequest.getUsers().size(),
			loadRequest.getPersons().size(),
			loadRequest.getEvents().size()
		);
	}
}
