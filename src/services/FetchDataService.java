package services;

import dao.*;
import handlers.FetchDataRequest;
import model.AuthToken;
import model.Event;
import model.ModelData;
import model.Person;
import org.jetbrains.annotations.NotNull;
import server.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An object that serves a single data-fetch request.
 */
public class FetchDataService {
	private final @NotNull Database db;
	
	public FetchDataService(@NotNull Database database) {
		this.db = database;
	}
	
	/**
	 * Attempts to fetch data of a given type from the server.
	 *
	 * @param request Information about the sort of data to retrieve.
	 * @param <T> The model type of the data to fetch.
	 * @return The result of the fetch operation.
	 */
	public <T extends ModelData> @NotNull FetchDataResult<T> fetch(
		FetchDataRequest request
	) throws DataAccessException {
		AtomicReference<FetchDataResult<T>> result = new AtomicReference<>(null);
		
		db.runTransaction(conn -> {
			List<T> data = new ArrayList<>();
			
			switch (request.getTable()) {
				case PERSON:
					PersonDao personDao = new PersonDao(conn);
					if (request.getId() != null) {
						Person person = personDao.find(request.getId());
						if (person == null) {
							result.set(new FetchDataResult<T>(FetchDataFailureReason.NOT_FOUND));
							return false;
						}
						if (!person.getAssociatedUsername().equals(request.getUserName())) {
							result.set(new FetchDataResult<T>(FetchDataFailureReason.UNAUTHORIZED));
							return false;
						}
						data.add((T) person);
					} else {
						data = (List<T>) personDao.findForUser(request.getUserName());
					}
					break;
					
				case EVENT:
					EventDao eventDao = new EventDao(conn);
					if (request.getId() != null) {
						Event event = eventDao.find(request.getId());
						if (event != null) {
							result.set(new FetchDataResult<T>(FetchDataFailureReason.NOT_FOUND));
							return false;
						}
						if (!event.getAssociatedUsername().equals(request.getUserName())) {
							result.set(new FetchDataResult<T>(FetchDataFailureReason.UNAUTHORIZED));
							return false;
						}
						data.add((T) event);
					} else {
						data = (List<T>) eventDao.findForUser(request.getUserName());
					}
					break;
					
				case USER:
					UserDao userDao = new UserDao(conn);
					if (request.getId() != null) {
						data.add((T) userDao.find(request.getId()));
					} else {
						data.add((T) userDao.find(request.getUserName()));
					}
					break;
					
				case AUTH_TOKEN:
					AuthTokenDao authTokenDao = new AuthTokenDao(conn);
					if (request.getId() != null) {
						AuthToken token = authTokenDao.find(request.getId());
						if (token != null) {
							result.set(new FetchDataResult<T>(FetchDataFailureReason.NOT_FOUND));
							return false;
						}
						if (!token.getAssociatedUsername().equals(request.getUserName())) {
							result.set(new FetchDataResult<T>(FetchDataFailureReason.UNAUTHORIZED));
							return false;
						}
						data.add((T) token);
					} else {
						data = (List<T>) authTokenDao.findForUser(request.getUserName());
					}
					break;
					
				default:
					IllegalStateException e = new IllegalStateException(
						"Unknown request table: " + request.getTable().getName()
					);
					Server.logger.severe(e.getMessage());
					e.printStackTrace();
					throw e;
			}
			
			result.set(new FetchDataResult<T>(data));
			
			return false;
		});
		
		return result.get();
	}
}
