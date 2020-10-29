package services;

import dao.*;
import model.*;
import org.jetbrains.annotations.NotNull;
import org.sqlite.SQLiteErrorCode;
import server.Server;
import utilities.ArrayHelpers;
import utilities.NameGenerator;
import utilities.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An object that serves a single database-fill request.
 */
public class FillService {
	private final Database db;
	
	public FillService(@NotNull Database database) {
		this.db = database;
	}
	
	
	
	/**
	 * Sets the family tree for the given user to a new tree with the given number of generations.
	 *
	 * @param userName The ID of the user to populate.
	 * @param generations The number of generations to fill. Must be non-negative.
	 *
	 * @return An object that describes the event.
	 * @throws DataAccessException An exception if there was an error accessing the database.
	 */
	public @NotNull FillResult fill(@NotNull String userName, int generations) throws DataAccessException, IOException {
		assert generations >= 0;
		assert !userName.isEmpty();
		
		AtomicInteger personCount = new AtomicInteger(0);
		AtomicInteger eventCount = new AtomicInteger(0);
		
		// Prepare the database for fresh data
		clearFormerData(userName);
		
		Person userPerson = newPersonForUser(userName);
		personCount.addAndGet(1);
		
		// Create new generations
		Pair<List<Person>, List<Event>> newEntries =
			generationsFromChild(generations, userPerson, userName);
		
		// Write them in
		FillResult result = fillNewGenerations(personCount, eventCount, newEntries);
		
		Server.logger.info("Filled " + generations + " generations of people. Added " +
			result.getPersonCount() + " people and " +
			result.getPersonCount() + " events."
		);
		
		return result;
	}
	
	
	
	private @NotNull FillResult fillNewGenerations(
		AtomicInteger personCount,
		AtomicInteger eventCount,
		Pair<List<Person>, List<Event>> newEntries
	) throws DataAccessException {
		AtomicReference<FillResult> result = new AtomicReference<>(null);
		
		try {
			db.runTransaction(conn -> {
				PersonDao personDao = new PersonDao(conn);
				EventDao eventDao = new EventDao(conn);
				
				List<Person> userPersons = newEntries.getFirst();
				List<Event> userEvents = newEntries.getSecond();
				
				for (Person person : userPersons) {
					personDao.update(person);
				}
				for (Event event : userEvents) {
					eventDao.update(event);
				}
				
				result.set(new FillResult(
					userPersons.size() + personCount.get(),
					userEvents.size() + eventCount.get()
				));
				return true;
			});
			
		} catch (DataAccessException e) {
			SQLiteErrorCode code = e.getErrorCode();
			String message = e.getMessage();
			if (code != SQLiteErrorCode.SQLITE_CONSTRAINT || !message.contains(".id")) {
				throw e;
			}
			// Duplicate object
			result.set(new FillResult(FillFailureReason.DUPLICATE_OBJECT_ID));
		}
		
		return result.get();
	}
	
	
	private @NotNull Person newPersonForUser(@NotNull String userName) throws DataAccessException {
		// Make sure the user has a Person entry
		AtomicReference<Person> newUserPerson = new AtomicReference<>(null);
		db.runTransaction(conn -> {
			UserDao userDao = new UserDao(conn);
			PersonDao personDao = new PersonDao(conn);
			
			User user = userDao.find(userName);
			
			if (user != null) {
				String newPersonID;
				if (user.getPersonID() != null) {
					newPersonID = user.getPersonID();
				} else {
					newPersonID = NameGenerator.newObjectIdentifier();
				}
				newUserPerson.set(new Person(
					newPersonID,
					userName,
					user.getFirstName(),
					user.getLastName(),
					user.getGender(),
					null,
					null,
					null
				));
				user.setPersonID(newUserPerson.get().getId());
				userDao.update(user);
				personDao.update(newUserPerson.get());
			} else {
				// TODO: Fatal error here
			}
			
			return true;
		});
		
		return newUserPerson.get();
	}
	
	
	private void clearFormerData(@NotNull String userName) throws DataAccessException {
		// Delete the user's old data
		
		db.runTransaction(conn -> {
			UserDao userDao = new UserDao(conn);
			PersonDao personDao = new PersonDao(conn);
			EventDao eventDao = new EventDao(conn);
			
			// Get the user's family tree
			List<Person> userPersons = personDao.findForUser(userName);
			
			// Get the user's Person entry, if they have one
			User user = userDao.find(userName);
			if (user == null) {
				throw new DataAccessException(
					SQLiteErrorCode.SQLITE_NOTFOUND,
					"There was no user found with the username '" + userName + "'"
				);
			}
			
			// Eat the user's tree
			for (Person person : userPersons) {
				personDao.delete(person.getId());
				
				List<Event> personEvents = eventDao.findForPerson(person.getId());
				for (Event event : personEvents) {
					eventDao.delete(event.getId());
				}
			}
			
			return true;
		});
	}
	
	
	
	private @NotNull Pair<List<Person>, List<Event>> generationsFromChild(
		int generations,
		@NotNull Person child,
		@NotNull String userName
	) throws IOException {
		
		// Iteratively create parents from the child, and add the new people to the array.
		// For each generation...
		
		// Add first generation
		Pair<List<Person>, List<Event>> firstGeneration =
			generationFrom(Collections.singletonList(child), userName);
		
		List<Person> generation = new ArrayList<>(firstGeneration.getFirst());
		List<Event> allNewEvents = new ArrayList<>(firstGeneration.getSecond());
		generations -= 1;
		
		List<Person> allNewPersons = new ArrayList<>(generation);
		
		for (int idx = 0; idx < generations; idx++) {
			// Add the generation to the results
			Pair<List<Person>, List<Event>> newGeneration = generationFrom(generation, userName);
			generation = newGeneration.getFirst();
			
			allNewEvents.addAll(newGeneration.getSecond());
			allNewPersons.addAll(generation);
		}
		
		return new Pair<>(allNewPersons, allNewEvents);
	}
	
	private @NotNull Pair<List<Person>, List<Event>> generationFrom(
		@NotNull List<Person> oldGeneration,
		@NotNull String userName
	) throws IOException {
		List<Person> people = new ArrayList<>();
		List<Event> events = new ArrayList<>();
		
		// Create parents for each person in the previous generation
		for (Person person : oldGeneration) {
			// Add them to a new generation array
			Pair<Person, Person> parents = randomParents(person);
			Person father = parents.getFirst();
			Person mother = parents.getSecond();
			if (!ArrayHelpers.containsObjectWithSameId(people, person)) {
				people.add(person);
			}
			people.add(father);
			people.add(mother);
			
			// Create events for them
			// Birth
			Event dadBirth = newEvent(userName, father, "Birth");
			Event momBirth = newEvent(userName, mother, "Birth");
			Event dadMarriage = newEvent(userName, father, "Marriage");
			Event momMarriage = newEvent(userName, mother, "Marriage");
			events.add(dadBirth);
			events.add(momBirth);
			events.add(dadMarriage);
			events.add(momMarriage);
		}
		
		return new Pair<>(people, events);
	}
	
	private @NotNull Event newEvent(@NotNull String userName, @NotNull Person person, @NotNull String type) {
		// TODO: Add location info
		return new Event(
			NameGenerator.newObjectIdentifier(),
			userName,
			person.getId(),
			null,
			null,
			"United States",
			"Provo",
			type,
			2020
		);
	}
	
	/**
	 * Returns a pair of new parents (ordered father then mother) for the given <code>child</code>.
	 */
	private @NotNull Pair<Person, Person> randomParents(@NotNull Person child) throws IOException {
		Pair<Person, Person> result = new Pair<>();
		
		// TODO: Add a chance for the parents to retain the child's surname
		
		Person father = randomPerson(Gender.MALE, child.getAssociatedUsername());
		Person mother = randomPerson(Gender.FEMALE, child.getAssociatedUsername());
		mother.setLastName(father.getLastName());
		father.setSpouseID(mother.getId());
		mother.setSpouseID(father.getId());
		
		result.setFirst(father);
		result.setSecond(mother);
		
		child.setFatherID(father.getId());
		child.setMotherID(mother.getId());
		
		return result;
	}
	
	
	
	private @NotNull Person randomPerson(
		@NotNull Gender gender,
		@NotNull String associatedUsername
	) throws IOException {
		return new Person(
			NameGenerator.newObjectIdentifier(),
			associatedUsername,
			NameGenerator.randomFirstName(gender),
			NameGenerator.randomFirstName(gender),
			gender,
			null,
			null,
			null
		);
	}
}
