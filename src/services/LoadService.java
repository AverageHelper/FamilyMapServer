package services;

import dao.*;
import model.Event;
import model.Identifiable;
import model.Person;
import model.User;
import org.jetbrains.annotations.NotNull;

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
		@NotNull List<User> users,
		@NotNull List<Person> persons,
		@NotNull List<Event> events
	) throws DataAccessException {
		db.clearTables();
		
		// Remove duplicate entries
		// TODO: We may be fine leaning on SQL for this
		List<User> usersToAdd = new ArrayList<>();
		List<Person> personsToAdd = new ArrayList<>();
		List<Event> eventsToAdd = new ArrayList<>();
		
		for (User user : users) {
			if (!containsObjectWithSameId(usersToAdd, user)) {
				usersToAdd.add(user);
			}
		}
		for (Person person : persons) {
			if (!containsObjectWithSameId(personsToAdd, person)) {
				personsToAdd.add(person);
			}
		}
		for (Event event : events) {
			if (!containsObjectWithSameId(eventsToAdd, event)) {
				eventsToAdd.add(event);
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
	
	private <S, T extends Identifiable<S>> boolean containsObjectWithSameId(
		final List<T> list,
		final T key
	) {
		return list.stream().anyMatch(o -> o.getId().equals(key.getId()));
	}
	
}
