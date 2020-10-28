package handlers;

import model.Event;
import model.Identifiable;
import model.Person;
import model.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LoadRequest extends JSONSerialization {
	private final @NotNull List<User> users;
	private final @NotNull List<Person> persons;
	private final @NotNull List<Event> events;
	
	public LoadRequest() {
		this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
	}
	
	public LoadRequest(
		@NotNull List<User> users,
		@NotNull List<Person> persons,
		@NotNull List<Event> events
	) {
		this.users = users;
		this.persons = persons;
		this.events = events;
	}
	
	public @NotNull List<User> getUsers() {
		return users;
	}
	
	public @NotNull List<Person> getPersons() {
		return persons;
	}
	
	public @NotNull List<Event> getEvents() {
		return events;
	}
	
	/**
	 * Adds the given <code>User</code> object to the request.
	 * @param user A <code>User</code> representation.
	 */
	public void addUser(@NotNull User user) {
		users.add(user);
	}
	
	/**
	 * Adds the given <code>Person</code> object to the request.
	 * @param person A <code>Person</code> representation.
	 */
	public void addPerson(@NotNull Person person) {
		persons.add(person);
	}
	
	/**
	 * Adds the given <code>Event</code> object to the request.
	 * @param event A <code>Event</code> representation.
	 */
	public void addEvent(@NotNull Event event) {
		events.add(event);
	}
	
	private <ID, T extends Identifiable<ID>> @Nullable T removeElement(
		@NotNull ID id,
		@NotNull List<T> container
	) {
		int itemIdx = -1;
		for (int idx = 0; idx < container.size(); idx++) {
			if (container.get(idx).getId().equals(id)) {
				itemIdx = idx;
				break;
			}
		}
		if (itemIdx >= 0) {
			T itemToRemove = container.get(itemIdx);
			container.remove(itemIdx);
			return itemToRemove;
		}
		return null;
	}
	
	/**
	 * Removes the user with the given ID from the request.
	 * @param userName A user ID string.
	 * @return The <code>User</code> object that was removed, or <code>null</code> if no user existed in the request with the given ID.
	 */
	public @Nullable User removeUser(@NotNull String userName) {
		return removeElement(userName, users);
	}
	
	/**
	 * Removes the person with the given ID from the request.
	 * @param id A person ID string.
	 * @return The <code>Person</code> object that was removed, or <code>null</code> if no object existed in the request with the given ID.
	 */
	public @Nullable Person removePerson(@NotNull String id) {
		return removeElement(id, persons);
	}
	
	/**
	 * Removes the event with the given ID from the request.
	 * @param id An event ID string.
	 * @return The <code>Event</code> object that was removed, or <code>null</code> if no object existed in the request with the given ID.
	 */
	public @Nullable Event removeEvent(@NotNull String id) {
		return removeElement(id, events);
	}
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public void assertCorrectDeserialization() throws MissingKeyException {
		if (this.users == null) {
			throw new MissingKeyException("users");
		}
		if (this.persons == null) {
			throw new MissingKeyException("persons");
		}
		if (this.events == null) {
			throw new MissingKeyException("events");
		}
	}
}
