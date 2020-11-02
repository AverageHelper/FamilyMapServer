package services;

import dao.*;
import handlers.JSONSerialization;
import handlers.LoadRequest;
import handlers.MissingKeyException;
import model.Event;
import model.Gender;
import model.Person;
import model.User;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.FileHelpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class LoadServiceTest {
	
	private static final String TEST_USER_ID = "test_user";
	
	private Database db;
	private LoadService service;
	
	@BeforeEach
	void setUp() throws DataAccessException {
		db = new Database(Database.TEST_DATABASE_NAME);
		service = new LoadService(db);
		db.clearTables();
	}
	
	private int userCount() throws DataAccessException {
		AtomicInteger count = new AtomicInteger(0);
		
		db.runTransaction(conn -> {
			UserDao dao = new UserDao(conn);
			count.set(dao.count());
			return false;
		});
		
		return count.get();
	}
	
	private List<Person> fetchPersonsForUser(@NotNull String userName) throws DataAccessException {
		AtomicReference<List<Person>> result = new AtomicReference<>(null);
		db.runTransaction(conn -> {
			PersonDao dao = new PersonDao(conn);
			result.set(dao.findForUser(userName));
			return false;
		});
		return result.get();
	}
	
	private int personCount() throws DataAccessException {
		AtomicInteger count = new AtomicInteger(0);
		
		db.runTransaction(conn -> {
			PersonDao dao = new PersonDao(conn);
			count.set(dao.count());
			return false;
		});
		
		return count.get();
	}
	
	private List<Event> fetchEventsForUser(@NotNull String userName) throws DataAccessException {
		AtomicReference<List<Event>> result = new AtomicReference<>(null);
		db.runTransaction(conn -> {
			EventDao dao = new EventDao(conn);
			result.set(dao.findForUser(userName));
			return false;
		});
		return result.get();
	}
	
	private Event fetchEventWithID(@NotNull String id) throws DataAccessException {
		AtomicReference<Event> result = new AtomicReference<>(null);
		db.runTransaction(conn -> {
			EventDao dao = new EventDao(conn);
			result.set(dao.find(id));
			return false;
		});
		return result.get();
	}
	
	private int eventCount() throws DataAccessException {
		AtomicInteger count = new AtomicInteger(0);
		
		db.runTransaction(conn -> {
			EventDao dao = new EventDao(conn);
			count.set(dao.count());
			return false;
		});
		
		return count.get();
	}
	
	void assertDatabaseEmpty() throws DataAccessException {
		String prefix = "Database was expected to be empty, but ";
		assertEquals(0, userCount(),
			prefix + userCount() + " users were found");
		assertEquals(0, personCount(),
			prefix + personCount() + " persons were found");
		assertEquals(0, eventCount(),
			prefix + eventCount() + " events were found");
	}

	@Test
	void testLoad_doesNothingWithAllNullParameters() throws DataAccessException {
		service.load(null, null, null);
		assertDatabaseEmpty();
	}
	
	
	
	// ** One Thing at a Time
	
	@Test
	void testLoad_loadsOneUser() throws DataAccessException {
		List<User> users = new ArrayList<>(
			Collections.singleton(new User(
				TEST_USER_ID,
				"password",
				"user@example.com",
				"Sam",
				"Beckett",
				Gender.MALE,
				"somebody"
			))
		);
		LoadResult res = service.load(users, null, null);
		
		// Check that the service reports the right number of loaded persons
		assertEquals(users.size(), res.getUserCount());
		assertEquals(0, res.getPersonCount());
		assertEquals(0, res.getEventCount());
		
		// Check that the database has the right number of persons
		assertEquals(users.size(), userCount());
		assertEquals(0, personCount());
		assertEquals(0, eventCount());
	}
	
	@Test
	void testLoad_loadsOneEvent() throws DataAccessException {
		List<Event> events = new ArrayList<>(
			Collections.singleton(new Event(
				"test_event",
				TEST_USER_ID,
				"test_person",
				null,
				0.0,
				"Belgium",
				"Brussels",
				"nothing, really",
				2020
			))
		);
		LoadResult res = service.load(null, null, events);
		
		// Check that the service reports the right number of loaded events
		assertEquals(0, res.getUserCount());
		assertEquals(0, res.getPersonCount());
		assertEquals(events.size(), res.getEventCount());
		
		// Check that the database has the right number of events
		assertEquals(0, userCount());
		assertEquals(0, personCount());
		assertEquals(events.size(), eventCount());
		
		List<Event> loadedEvents = fetchEventsForUser(TEST_USER_ID);
		assertEquals(events, loadedEvents);
	}
	
	@Test
	void testLoad_loadsOnePerson() throws DataAccessException {
		List<Person> persons = new ArrayList<>(
			Collections.singleton(new Person(
				"test_person",
				TEST_USER_ID,
				"Sam",
				"Beckett",
				Gender.MALE,
				null,
				null,
				null
			))
		);
		LoadResult res = service.load(null, persons, null);
		
		// Check that the service reports the right number of loaded persons
		assertEquals(0, res.getUserCount());
		assertEquals(persons.size(), res.getPersonCount());
		assertEquals(0, res.getEventCount());
		
		// Check that the database has the right number of persons
		assertEquals(0, userCount());
		assertEquals(persons.size(), personCount());
		assertEquals(0, eventCount());
		
		List<Person> loadedPersons = fetchPersonsForUser(TEST_USER_ID);
		assertEquals(persons, loadedPersons);
	}
	
	
	
	// ** Multiple Items
	
	@Test
	void testLoad_passoffItems() throws IOException, DataAccessException {
		try {
			String rawRequest = FileHelpers.stringFromFile(
				new File("test/driver/passoffFiles/LoadData.json").getAbsoluteFile()
			);
			LoadRequest loadRequest = JSONSerialization.fromJson(rawRequest, LoadRequest.class);
			LoadResult loginResult = service.load(
				loadRequest.getUsers(),
				loadRequest.getPersons(),
				loadRequest.getEvents()
			);
			
			assertNotNull(loadRequest.getUsers());
			assertNotNull(loadRequest.getPersons());
			assertNotNull(loadRequest.getEvents());
			assertEquals(loadRequest.getUsers().size(), loginResult.getUserCount());
			assertEquals(loadRequest.getPersons().size(), loginResult.getPersonCount());
			assertEquals(loadRequest.getEvents().size(), loginResult.getEventCount());
			
			final String ASTEROIDS1_EVENT_ID = "Sheila_Asteroids";
			final String ASTEROIDS2_EVENT_ID = "Other_Asteroids";
			
			Event testEvent = fetchEventWithID(ASTEROIDS1_EVENT_ID);
			assertEquals(
				new Event(
					ASTEROIDS1_EVENT_ID,
					"sheila",
					"Sheila_Parker",
					77.4667,
					-68.7667,
					"Denmark",
					"Qaanaaq",
					"completed asteroids",
					2014
				),
				testEvent,
				"Event returned does not match event from LoadRequest"
			);
			testEvent = fetchEventWithID(ASTEROIDS2_EVENT_ID);
			assertEquals(
				new Event(
					ASTEROIDS2_EVENT_ID,
					"sheila",
					"Sheila_Parker",
					74.4667,
					-60.7667,
					"Denmark",
					"Qaanaaq",
					"COMPLETED ASTEROIDS",
					2014
				),
				testEvent,
				"Event returned does not match event from LoadRequest"
			);
		} catch (FileNotFoundException fileNotFoundException) {
			Assertions.fail("passoffFiles/LoadData.json not found in project root directory");
		} catch (MissingKeyException e) {
			Assertions.fail("passoffFiles.LoadData.json was not suitable for testing");
		}
	}
}
