package services;

import dao.*;
import handlers.FetchDataRequest;
import model.*;
import org.jetbrains.annotations.NotNull;
import server.Server;

import java.sql.Connection;
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
	 * @throws DataAccessException An exception if there was a problem accessing the database.
	 */
	public <T extends ModelData> @NotNull FetchDataResult<T> fetch(
		FetchDataRequest request
	) throws DataAccessException {
		AtomicReference<FetchDataResult<T>> result = new AtomicReference<>(null);
		
		db.runTransaction(conn -> {
			List<T> data = new ArrayList<>();
			
			try {
				switch (request.getTable()) {
					case PERSON:
						//noinspection unchecked
						data.addAll((List<T>) fetchPersons(request, conn));
						break;
					
					case EVENT:
						//noinspection unchecked
						data.addAll((List<T>) fetchEvents(request, conn));
						break;
					
					case AUTH_TOKEN:
						//noinspection unchecked
						data.addAll((List<T>) fetchAuthTokens(request, conn));
						break;
					
					case USER:
						//noinspection unchecked
						data.addAll((List<T>) fetchUsers(request, conn));
						break;
					
					default:
						IllegalStateException e = new IllegalStateException(
							"Unknown request table: " + request.getTable().getName()
						);
						Server.logger.severe(e.getMessage());
						e.printStackTrace();
						throw e;
				}
				
				result.set(new FetchDataResult<>(data));
				
			} catch (FetchDataFailureException e) {
				result.set(new FetchDataResult<>(e.getReason()));
			}
			
			return false;
		});
		
		return result.get();
	}
	
	
	/**
	 * Fetches <code>Person</code> entries that match details specified in the provided
	 * <code>request</code>.
	 *
	 * @param request Information about the data to be returned.
	 * @param conn The database connection.
	 *
	 * @return A list of matching <code>Person</code> entries.
	 * @throws DataAccessException An exception if there was a problem accessing the database.
	 * @throws FetchDataFailureException An exception if there is no matching data or the user
	 * doesn't have permission to access the requested data.
	 */
	private @NotNull List<Person> fetchPersons(
		@NotNull FetchDataRequest request,
		@NotNull Connection conn
	) throws DataAccessException, FetchDataFailureException {
		List<Person> data = new ArrayList<>();
		
		PersonDao personDao = new PersonDao(conn);
		if (request.getId() != null) {
			Person person = personDao.find(request.getId());
			if (person == null) {
				throw new FetchDataFailureException(FetchDataFailureReason.NOT_FOUND);
			}
			if (!person.getAssociatedUsername().equals(request.getUserName())) {
				throw new FetchDataFailureException(FetchDataFailureReason.UNAUTHORIZED);
			}
			data.add(person);
		} else {
			data = personDao.findForUser(request.getUserName());
		}
		
		return data;
	}
	
	
	/**
	 * Fetches <code>Event</code> entries that match details specified in the provided
	 * <code>request</code>.
	 *
	 * @param request Information about the data to be returned.
	 * @param conn The database connection.
	 *
	 * @return A list of matching <code>Event</code> entries.
	 * @throws DataAccessException An exception if there was a problem accessing the database.
	 * @throws FetchDataFailureException An exception if there is no matching data or the user
	 * doesn't have permission to access the requested data.
	 */
	private @NotNull List<Event> fetchEvents(
		@NotNull FetchDataRequest request,
		@NotNull Connection conn
	) throws DataAccessException, FetchDataFailureException {
		List<Event> data = new ArrayList<>();
		
		EventDao eventDao = new EventDao(conn);
		if (request.getId() != null) {
			Event event = eventDao.find(request.getId());
			if (event == null) {
				throw new FetchDataFailureException(FetchDataFailureReason.NOT_FOUND);
			}
			if (!event.getAssociatedUsername().equals(request.getUserName())) {
				throw new FetchDataFailureException(FetchDataFailureReason.UNAUTHORIZED);
			}
			data.add(event);
		} else {
			data = eventDao.findForUser(request.getUserName());
		}
		
		return data;
	}
	
	
	/**
	 * Fetches <code>AuthToken</code> entries that match details specified in the provided
	 * <code>request</code>.
	 *
	 * @param request Information about the data to be returned.
	 * @param conn The database connection.
	 *
	 * @return A list of matching <code>AuthToken</code> entries.
	 * @throws DataAccessException An exception if there was a problem accessing the database.
	 * @throws FetchDataFailureException An exception if there is no matching data or the user
	 * doesn't have permission to access the requested data.
	 */
	private @NotNull List<AuthToken> fetchAuthTokens(
		@NotNull FetchDataRequest request,
		@NotNull Connection conn
	) throws DataAccessException, FetchDataFailureException {
		List<AuthToken> data = new ArrayList<>();
		
		AuthTokenDao authTokenDao = new AuthTokenDao(conn);
		if (request.getId() != null) {
			AuthToken token = authTokenDao.find(request.getId());
			if (token == null) {
				throw new FetchDataFailureException(FetchDataFailureReason.NOT_FOUND);
			}
			if (!token.getAssociatedUsername().equals(request.getUserName())) {
				throw new FetchDataFailureException(FetchDataFailureReason.UNAUTHORIZED);
			}
			data.add(token);
		} else {
			data = authTokenDao.findForUser(request.getUserName());
		}
		
		return data;
	}
	
	
	/**
	 * Fetches <code>User</code> entries that match details specified in the provided
	 * <code>request</code>.
	 *
	 * @param request Information about the data to be returned.
	 * @param conn The database connection.
	 *
	 * @return A list of matching <code>User</code> entries.
	 * @throws DataAccessException An exception if there was a problem accessing the database.
	 * @throws FetchDataFailureException An exception if there is no matching data or the user
	 * doesn't have permission to access the requested data.
	 */
	private @NotNull List<User> fetchUsers(
		@NotNull FetchDataRequest request,
		@NotNull Connection conn
	) throws DataAccessException, FetchDataFailureException {
		List<User> data = new ArrayList<>();
		
		UserDao userDao = new UserDao(conn);
		User user;
		
		if (request.getId() != null) {
			user = userDao.find(request.getId());
			if (user == null) {
				throw new FetchDataFailureException(FetchDataFailureReason.NOT_FOUND);
			}
		} else {
			user = userDao.find(request.getUserName());
		}
		
		if (user != null) {
			data.add(user);
		}
		
		return data;
	}
}
