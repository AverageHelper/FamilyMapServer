package services;

import dao.*;
import handlers.FetchDataRequest;
import model.ModelData;
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
	public <T extends ModelData> FetchDataResult<T> fetch(
		FetchDataRequest request
	) throws DataAccessException {
		AtomicReference<FetchDataResult<T>> result = new AtomicReference<>(null);
		
		db.runTransaction(conn -> {
			List<T> data = new ArrayList<>();
			
			switch (request.getTable()) {
				case PERSON:
					PersonDao personDao = new PersonDao(conn);
					if (request.getId() != null) {
						data.add((T) personDao.find(request.getId()));
					} else if (request.getUserName() != null) {
						data = (List<T>) personDao.findForUser(request.getUserName());
					}
					break;
					
				case EVENT:
					EventDao eventDao = new EventDao(conn);
					if (request.getId() != null) {
						data.add((T) eventDao.find(request.getId()));
					} else if (request.getUserName() != null) {
						data = (List<T>) eventDao.findForUser(request.getUserName());
					}
					break;
					
				case USER:
					UserDao userDao = new UserDao(conn);
					if (request.getId() != null) {
						data.add((T) userDao.find(request.getId()));
					} else if (request.getUserName() != null) {
						data.add((T) userDao.find(request.getUserName()));
					}
					break;
					
				case AUTH_TOKEN:
					AuthTokenDao authTokenDao = new AuthTokenDao(conn);
					if (request.getId() != null) {
						data.add((T) authTokenDao.find(request.getId()));
					} else if (request.getUserName() != null) {
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
