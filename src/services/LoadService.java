package services;

import dao.*;
import model.Event;
import model.Person;
import model.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utilities.ArrayHelpers;

import java.util.ArrayList;
import java.util.List;

/**
 * An object that serves a single clear-then-load request.
 */
public class LoadService {
	private final Database db;
	
	public LoadService(@NotNull Database database) {
		this.db = database;
	}
	
	public @NotNull LoadResult load(
		@Nullable List<User> users,
		@Nullable List<Person> persons,
		@Nullable List<Event> events
	) throws DataAccessException {
		db.clearTables();
		
		// Remove duplicate entries
		// FIXME: We should be fine leaning on SQL for this
		List<User> usersToAdd = new ArrayList<>();
		List<Person> personsToAdd = new ArrayList<>();
		List<Event> eventsToAdd = new ArrayList<>();
		
		if (users != null) {
			for (User user : users) {
				if (!ArrayHelpers.containsObjectWithSameId(usersToAdd, user)) {
					usersToAdd.add(user);
				}
			}
		}
		if (persons != null) {
			for (Person person : persons) {
				if (!ArrayHelpers.containsObjectWithSameId(personsToAdd, person)) {
					personsToAdd.add(person);
				}
			}
		}
		if (events != null) {
			for (Event event : events) {
				if (!ArrayHelpers.containsObjectWithSameId(eventsToAdd, event)) {
					eventsToAdd.add(event);
				}
			}
		}
		
		db.runTransaction(conn -> {
			UserDao userDao = new UserDao(conn);
			PersonDao personDao = new PersonDao(conn);
			EventDao eventDao = new EventDao(conn);
			
			for (User user : usersToAdd) {
				userDao.insertIfNotExists(user);
			}
			for (Person person : personsToAdd) {
				personDao.insertIfNotExists(person);
			}
			for (Event event : eventsToAdd) {
				eventDao.insertIfNotExists(event);
			}
			
			return true;
		});
		
		return new LoadResult(
			usersToAdd.size(),
			personsToAdd.size(),
			eventsToAdd.size()
		);
	}
	
}
