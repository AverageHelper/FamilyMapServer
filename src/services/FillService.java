package services;

import dao.*;
import model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sqlite.SQLiteErrorCode;
import server.Server;
import utilities.*;

import java.io.IOException;
import java.util.*;
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
	 * @throws IOException An exception if there was an error generating person or event data.
	 */
	public @NotNull FillResult fill(@NotNull String userName, int generations) throws DataAccessException, IOException {
		if (userName.isEmpty()) {
			throw new IllegalArgumentException("Username must not be empty");
		}
		if (generations < 0) {
			throw new IllegalArgumentException("Generation count must be positive or 0");
		}
		
		int personCount = 0;
		int eventCount = 0;
		
		// Prepare the database for fresh data
		clearFormerData(userName);
		
		Person userPerson = newPersonForUser(userName);
		personCount += 1;
		
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
	
	
	/**
	 * Writes new <code>Person</code> and <code>Event</code> entries to the database.
	 *
	 * @param personCount The number of persons written so far. This is added to the number
	 *                    of provided <code>Person</code> entries.
	 * @param eventCount The number of events written so far. This is added to the number of
	 *                   provided <code>Event</code> entries.
	 * @param newEntries The persons and events to write.
	 * @return The result of the operation.
	 * @throws DataAccessException An exception if there was a problem writing to the database.
	 */
	private @NotNull FillResult fillNewGenerations(
		int personCount,
		int eventCount,
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
					userPersons.size() + personCount,
					userEvents.size() + eventCount
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
	
	
	/**
	 * Creates a new <code>Person</code> entry for the user with the provided
	 * <code>userName</code>. If the user already has a <code>Person</code> entry, then that
	 * one is deleted and recreated.
	 *
	 * @param userName The ID of the user to modify.
	 * @return The new <code>Person</code> entry.
	 * @throws DataAccessException An exception if there was a problem writing to the database.
	 */
	public @NotNull Person newPersonForUser(@NotNull String userName) throws DataAccessException {
		if (userName.isEmpty()) {
			throw new IllegalArgumentException("Username must not be empty");
		}
		
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
				throw new DataAccessException(
					SQLiteErrorCode.SQLITE_NOTFOUND,
					"There was no user found with the username '" + userName + "'"
				);
			}
			
			return true;
		});
		
		return newUserPerson.get();
	}
	
	
	/**
	 * Deletes all <code>Person</code> and <code>Event</code> entries for the user with the
	 * provided <code>userName</code>.
	 *
	 * @param userName The ID of the user whose data to clear.
	 * @throws DataAccessException An exception if the write fails.
	 */
	public void clearFormerData(@NotNull String userName) throws DataAccessException {
		if (userName.isEmpty()) {
			throw new IllegalArgumentException("Username must not be empty");
		}
		
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
	
	
	/**
	 * @return The current Gregorian year as an integer.
	 */
	public int thisYear() {
		return new GregorianCalendar().get(Calendar.YEAR);
	}
	
	private static final int DADDY_AGE = 23;
	private static final int CHILD_CURRENT_AGE = 23;
	private static final int MARRIAGE_AGE = 24;
	private static final int DEATH_AGE = 85;
	
	
	/**
	 * Creates realistic <code>Person</code> and <code>Event</code> entries for the given number
	 * of <code>generations</code> prior to the given <code>child</code>, each tied to the
	 * provided <code>userName</code>. No new data is written to the database.
	 *
	 * @param generations The number of generations of events and people to generate.
	 * @param child The root of the new family tree.
	 * @param userName The ID of the user who owns the new records.
	 *
	 * @return A <code>Pair</code> of lists containing the new <code>Person</code> and
	 * <code>Event</code> entries, respectively.
	 * @throws IOException An exception if there was a problem accessing the bank of random names.
	 */
	public @NotNull Pair<List<Person>, List<Event>> generationsFromChild(
		int generations,
		@NotNull Person child,
		@NotNull String userName
	) throws IOException {
		if (userName.isEmpty()) {
			throw new IllegalArgumentException("Username must not be empty");
		}
		if (generations < 0) {
			throw new IllegalArgumentException("Generations must be positive or 0");
		}
		
		// Iteratively create parents from the child, and add the new people to the array.
		// For each generation...
		
		int thisYear = this.thisYear();
		int fathersBirthYear = thisYear - CHILD_CURRENT_AGE - DADDY_AGE;
		Server.logger.finest(
			"This year is " + thisYear + ". Father was born in " + fathersBirthYear + "."
		);
		
		List<Person> allNewPersons = new ArrayList<>();
		List<Event> allNewEvents = new ArrayList<>();
		
		List<Person> generation = new ArrayList<>(Collections.singletonList(child));
		
		if (generations == 0) {
			allNewPersons.add(child);
		}
		
		// Add the user's birth event
		Event userBirth = newEvent(userName, child,
			"birth", thisYear() - CHILD_CURRENT_AGE, null);
		allNewEvents.add(userBirth);
		
		for (int idx = 0; idx < generations; idx++) {
			fathersBirthYear -= DADDY_AGE;
			List<Person> newGeneration = new ArrayList<>();
			
			// Create parents for each person in the previous generation
			for (Person person : generation) {
				Pair<List<Person>, List<Event>> family =
					parentalGenerationFromChild(person, userName, fathersBirthYear);
				for (Person newPerson : family.getFirst()) {
					ArrayHelpers.updateElementInList(allNewPersons, newPerson);
					ArrayHelpers.updateElementInList(allNewPersons, child);
				}
				allNewEvents.addAll(family.getSecond());
				newGeneration.addAll(family.getFirst());
			}
			
			generation = newGeneration;
		}
		
		return new Pair<>(allNewPersons, allNewEvents);
	}
	
	
	
	
	/**
	 * Creates realistic <code>Person</code> and <code>Event</code> entries for a single
	 * generation past the given <code>child</code>, each tied to the
	 * provided <code>userName</code>. No new data is written to the database.
	 *
	 * @param child The child from which to generate parents and events.
	 * @param fathersBirthYear The approximate birth year of the previous generation.
	 * @param userName The ID of the user who owns the new records.
	 *
	 * @return A <code>Pair</code> of lists containing the new <code>Person</code> and
	 * <code>Event</code> entries, respectively. These represent the parents of the given child
	 * only.
	 * @throws IOException An exception if there was a problem accessing the bank of random names.
	 */
	public @NotNull Pair<List<Person>, List<Event>> parentalGenerationFromChild(
		@NotNull Person child,
		@NotNull String userName,
		int fathersBirthYear
	) throws IOException {
		if (userName.isEmpty()) {
			throw new IllegalArgumentException("Username must not be empty");
		}
		
		List<Person> people = new ArrayList<>();
		List<Event> events = new ArrayList<>();
		
		// Add them to a new generation array
		Pair<Person, Person> parents = newParents(child);
		Person father = parents.getFirst();
		Person mother = parents.getSecond();
		people.add(father);
		people.add(mother);
		
		// Create events for them
		
		// Birth
		int birthYear2 = NumberHelpers.randomNumberAround(fathersBirthYear, 2);
		Event dadBirth = newEvent(userName, father,
			"birth", fathersBirthYear, null);
		Event momBirth = newEvent(userName, mother,
			dadBirth.getEventType(), birthYear2, null);
		events.add(dadBirth);
		events.add(momBirth);
		
		// Marriage
		Location marriageLoc = LocationGenerator.randomLocation();
		Event dadMarriage = newEvent(userName, father,
			"marriage", fathersBirthYear + MARRIAGE_AGE, marriageLoc);
		Event momMarriage = newEvent(userName, mother,
			dadMarriage.getEventType(), dadMarriage.getYear(), marriageLoc);
		assert momMarriage.getYear() == dadMarriage.getYear();
		events.add(dadMarriage);
		events.add(momMarriage);
		
		// Death
		int deathYear = fathersBirthYear + DEATH_AGE;
		int deathYear2 = NumberHelpers.randomNumberAround(fathersBirthYear + DEATH_AGE, 5);
		assert deathYear > 1000;
		assert deathYear2 > 1000;
//		if (thisYear() > deathYear) {
			Location deathLoc = LocationGenerator.randomLocation();
			Event dadDeath = newEvent(userName, father,
				"death", deathYear, deathLoc);
			Event momDeath = newEvent(userName, mother,
				dadDeath.getEventType(), deathYear2, deathLoc);
			events.add(dadDeath);
			events.add(momDeath);
//		}
		
		return new Pair<>(people, events);
	}
	
	
	/**
	 * Returns a pair of new parents (ordered father then mother) for the given <code>child</code>.
	 *
	 * @param child The child from whom to derive a parent couple.
	 * @return The father and mother, respectively.
	 * @throws IOException An exception if there was a problem accessing our lists of dummy data.
	 */
	private @NotNull Pair<Person, Person> newParents(@NotNull Person child) throws IOException {
		Pair<Person, Person> result = new Pair<>();
		
		// TODO: Add a chance for the parents to retain the child's surname
		
		Person father = randomPerson(Gender.MALE, child.getAssociatedUsername());
		Person mother = randomPerson(Gender.FEMALE, child.getAssociatedUsername());
		mother.setLastName(father.getLastName());
		
		father.setSpouseID(mother.getId());
		mother.setSpouseID(father.getId());
		
		child.setFatherID(father.getId());
		child.setMotherID(mother.getId());
		
		result.setFirst(father);
		result.setSecond(mother);
		
		return result;
	}
	
	
	private @NotNull Event newEvent(@NotNull String userName, @NotNull Person person, @NotNull String type, int year, @Nullable Location loc) throws IOException {
		if (loc == null) {
			loc = LocationGenerator.randomLocation();
		}
		
		return new Event(
			NameGenerator.newObjectIdentifier(),
			userName,
			person.getId(),
			loc.getLatitude(),
			loc.getLongitude(),
			loc.getCountry(),
			loc.getCity(),
			type,
			year
		);
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
