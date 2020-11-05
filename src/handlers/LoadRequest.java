package handlers;

import model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import transport.JSONSerialization;
import transport.MissingKeyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoadRequest extends JSONSerialization {
	private @Nullable List<User> users;
	private @Nullable List<Person> persons;
	private @Nullable List<Event> events;
	
	public LoadRequest() {
		this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
	}
	
	public LoadRequest(
		@Nullable List<User> users,
		@Nullable List<Person> persons,
		@Nullable List<Event> events
	) {
		this.users = users;
		this.persons = persons;
		this.events = events;
	}
	
	public @Nullable List<User> getUsers() {
		return users;
	}
	
	public @Nullable List<Person> getPersons() {
		return persons;
	}
	
	public @Nullable List<Event> getEvents() {
		return events;
	}
	
	/**
	 * Adds the given <code>User</code> object to the request.
	 * @param user A <code>User</code> representation.
	 */
	public void addUser(@NotNull User user) {
		if (users == null) {
			users = new ArrayList<>();
		}
		users.add(user);
	}
	
	/**
	 * Adds the given <code>Person</code> object to the request.
	 * @param person A <code>Person</code> representation.
	 */
	public void addPerson(@NotNull Person person) {
		if (persons == null) {
			persons = new ArrayList<>();
		}
		persons.add(person);
	}
	
	/**
	 * Adds the given <code>Event</code> object to the request.
	 * @param event A <code>Event</code> representation.
	 */
	public void addEvent(@NotNull Event event) {
		if (events == null) {
			events = new ArrayList<>();
		}
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
		if (users == null) {
			return null;
		}
		return removeElement(userName, users);
	}
	
	/**
	 * Removes the person with the given ID from the request.
	 * @param id A person ID string.
	 * @return The <code>Person</code> object that was removed, or <code>null</code> if no object existed in the request with the given ID.
	 */
	public @Nullable Person removePerson(@NotNull String id) {
		if (persons == null) {
			return null;
		}
		return removeElement(id, persons);
	}
	
	/**
	 * Removes the event with the given ID from the request.
	 * @param id An event ID string.
	 * @return The <code>Event</code> object that was removed, or <code>null</code> if no object existed in the request with the given ID.
	 */
	public @Nullable Event removeEvent(@NotNull String id) {
		if (events == null) {
			return null;
		}
		return removeElement(id, events);
	}
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public void assertCorrectDeserialization() throws MissingKeyException {
		if (this.users != null) {
			for (User user : users) {
				user.assertCorrectDeserialization();
			}
		}
		if (this.persons != null) {
			for (Person person : persons) {
				person.assertCorrectDeserialization();
			}
		}
		if (this.events != null) {
			for (Event event : events) {
				event.assertCorrectDeserialization();
			}
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LoadRequest that = (LoadRequest) o;
		return Objects.equals(getUsers(), that.getUsers()) &&
			Objects.equals(getPersons(), that.getPersons()) &&
			Objects.equals(getEvents(), that.getEvents());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getUsers(), getPersons(), getEvents());
	}
}
