package services;

import dao.*;
import model.Event;
import model.Person;
import model.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
		// Clear everything
		db.clearTables();
		
		// Add new entries
		AtomicInteger usersAdded = new AtomicInteger(0);
		AtomicInteger personsAdded = new AtomicInteger(0);
		AtomicInteger eventsAdded = new AtomicInteger(0);
		
		db.runTransaction(conn -> {
			UserDao userDao = new UserDao(conn);
			PersonDao personDao = new PersonDao(conn);
			EventDao eventDao = new EventDao(conn);
			
			if (users != null) {
				for (User user : users) {
					if (userDao.insertIfNotExists(user)) {
						usersAdded.addAndGet(1);
					}
				}
			}
			if (persons != null) {
				for (Person person : persons) {
					if (personDao.insertIfNotExists(person)) {
						personsAdded.addAndGet(1);
					}
				}
			}
			if (events != null) {
				for (Event event : events) {
					if (eventDao.insertIfNotExists(event)) {
						eventsAdded.addAndGet(1);
					}
				}
			}
			
			return true;
		});
		
		return new LoadResult(
			usersAdded.get(),
			personsAdded.get(),
			eventsAdded.get()
		);
	}
	
}
