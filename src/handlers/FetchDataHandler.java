package handlers;

import dao.DatabaseTable;
import database.Database;
import database.DataAccessException;
import model.Event;
import model.ModelData;
import model.Person;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import responses.FetchDataResponse;
import responses.FetchSingleEventResponse;
import responses.FetchSinglePersonResponse;
import server.Server;
import services.FetchDataFailureReason;
import services.FetchDataResult;
import services.FetchDataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * An object that handles data-fetch requests.
 */
public class FetchDataHandler extends Handler<FetchDataResponse> {
	
	public FetchDataHandler() {
		super();
	}
	
	public FetchDataHandler(@NotNull Database<DatabaseTable> database) {
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
	public @NotNull FetchDataResponse run(@NotNull String path, @Nullable String userName, @NotNull String req) throws HandlingFailureException, DataAccessException {
		if (userName == null) {
			// Should never happen while `requiresAuthToken` returns `true`.
			throw new HandlingFailureException(
				HandlingFailureReason.USER_NOT_FOUND,
				"No user ID was provided."
			);
		}
		// Path: /event/{eventID} or /person/{personID}
		// Parse the path for the ID, if provided
		List<String> components = new ArrayList<>(Arrays.asList(path.split(Pattern.quote("/"))));
		
		// Drop the leading '/'
		components.remove(0);
		if (components.size() < 1 || components.size() > 2) {
			throw new HandlingFailureException(HandlingFailureReason.TOO_FEW_PATH_COMPONENTS);
		}
		
		// Get /person or /event
		String domain = components.get(0);
		
		// Get /{itemID}
		String id = null;
		if (components.size() > 1) {
			id = components.get(1);
		}
		
		if (domain.equals("person")) {
			// Path: /person/{personID}
			
			if (id != null) {
				// Fetch single person
				Person person = getPersonWithId(id, userName);
				if (person != null) {
					// Person found!
					return new FetchSinglePersonResponse(
						person.getAssociatedUsername(),
						person.getPersonID(),
						person.getFirstName(),
						person.getLastName(),
						person.getGender(),
						person.getFatherID(),
						person.getMotherID(),
						person.getSpouseID()
					);
					
				} else {
					// Person not found
					throw new HandlingFailureException(
						HandlingFailureReason.OBJECT_NOT_FOUND,
						"No Person with ID '" + id + "'"
					);
				}
				
			} else {
				// List all persons
				List<Person> results = listAllPeople(userName);
				return new FetchMultipleItemsResponse<>(results);
			}
			
		} else if (domain.equals("event")) {
			// Path: /event/{eventID}
			
			if (id != null) {
				// Fetch single event
				Event event = getEventWithId(id, userName);
				if (event != null) {
					// Event found!
					return new FetchSingleEventResponse(
						event.getAssociatedUsername(),
						event.getId(),
						event.getPersonID(),
						event.getLatitude(),
						event.getLongitude(),
						event.getCountry(),
						event.getCity(),
						event.getEventType(),
						event.getYear()
					);
					
				} else {
					// Event not found
					throw new HandlingFailureException(
						HandlingFailureReason.OBJECT_NOT_FOUND,
						"No Event with ID '" + id + "'"
					);
				}
				
			} else {
				// List all events
				List<Event> results = listAllEvents(userName);
				return new FetchMultipleItemsResponse<>(results);
			}
			
		} else {
			throw new HandlingFailureException(
				HandlingFailureReason.BAD_INPUT,
				"Unknown fetch domain " + domain
			);
		}
	}
	
	private IllegalStateException illegalState(@NotNull Object result) {
		IllegalStateException e = new IllegalStateException(
			"There is no valid case where a fetch result has neither value nor error, yet here we are: " + result.toString()
		);
		Server.logger.severe(e.getMessage());
		e.printStackTrace();
		return e;
	}
	
	private <T extends ModelData> @NotNull List<T> performFetch(
		@NotNull FetchDataRequest request
	) throws HandlingFailureException, DataAccessException {
		FetchDataService service = new FetchDataService(database);
		
		FetchDataResult<T> result = service.fetch(request);
		
		if (result.getFailureReason() == FetchDataFailureReason.NOT_FOUND) {
			return new ArrayList<>();
		}
		if (result.getFailureReason() != null) {
			throw HandlingFailureException.from(result.getFailureReason());
		}
		if (result.getData() != null) {
			return result.getData();
		}
		
		throw illegalState(result);
	}
	
	/**
	 * Attempts to fetch data about all stored <code>Person</code> entries for the current user.
	 *
	 * @param userName The ID of the current user.
	 * @return A list of resolved <code>Person</code> entries.
	 * @throws HandlingFailureException If the fetch fails.
	 * @throws DataAccessException If there was a problem accessing the database.
	 */
	public @NotNull List<Person> listAllPeople(@NotNull String userName) throws HandlingFailureException, DataAccessException {
		FetchDataRequest request = new FetchDataRequest(
			DatabaseTable.PERSON,
			userName
		);
		
		return performFetch(request);
	}
	
	/**
	 * Attempts to fetch data about all stored <code>Event</code> entries for the current user.
	 *
	 * @param userName The ID of the current user.
	 * @return A list of resolved <code>Event</code> entries.
	 * @throws HandlingFailureException If the fetch fails.
	 * @throws DataAccessException If there was a problem accessing the database.
	 */
	public @NotNull List<Event> listAllEvents(@NotNull String userName) throws HandlingFailureException, DataAccessException {
		FetchDataRequest request = new FetchDataRequest(
			DatabaseTable.EVENT,
			userName
		);
		
		return performFetch(request);
	}
	
	/**
	 * Attempts to fetch data about a <code>Person</code> entry with the given <code>id</code>.
	 *
	 * @param id The ID of the person to fetch.
	 * @param callerUserName The ID of the calling user.
	 * @return The resolved <code>Person</code> object, or <code>null</code> if the person is not found.
	 * @throws HandlingFailureException If the fetch fails.
	 * @throws DataAccessException If there was a problem reading from the database.
	 */
	public @Nullable Person getPersonWithId(
		@NotNull String id,
		@NotNull String callerUserName
	) throws HandlingFailureException, DataAccessException {
		FetchDataRequest request = new FetchDataRequest(
			DatabaseTable.PERSON,
			callerUserName
		);
		request.setId(id);
		
		return (Person) performFetch(request).get(0);
	}
	
	/**
	 * Attempts to fetch data about an <code>Event</code> entry with the given <code>id</code>.
	 *
	 * @param id The ID of the person to fetch.
	 * @param callerUserName The ID of the calling user.
	 * @return The resolved <code>Event</code> object, or <code>null</code> if the person is not found.
	 * @throws HandlingFailureException If the fetch fails.
	 * @throws DataAccessException If there was a problem reading from the database.
	 */
	public @Nullable Event getEventWithId(
		@NotNull String id,
		@NotNull String callerUserName
	) throws HandlingFailureException, DataAccessException {
		FetchDataRequest request = new FetchDataRequest(
			DatabaseTable.EVENT,
			callerUserName
		);
		request.setId(id);
		
		return (Event) performFetch(request).get(0);
	}
}
