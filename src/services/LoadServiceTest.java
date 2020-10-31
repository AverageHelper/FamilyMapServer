package services;

import dao.*;
import model.Event;
import model.Gender;
import model.Person;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class LoadServiceTest {
	
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
	
	private int personCount() throws DataAccessException {
		AtomicInteger count = new AtomicInteger(0);
		
		db.runTransaction(conn -> {
			PersonDao dao = new PersonDao(conn);
			count.set(dao.count());
			return false;
		});
		
		return count.get();
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
	
	@Test
	void testLoad_loadsOneEvent() throws DataAccessException {
		List<Event> events = new ArrayList<>(
			Collections.singleton(new Event(
				"test_event",
				"test_user",
				"test_person",
				null,
				0.0,
				"Belgium",
				"Brussels",
				"nothing, really",
				2020
			))
		);
		service.load(null, null, events);
		assertEquals(0, userCount());
		assertEquals(0, personCount());
		assertEquals(events.size(), eventCount());
	}
	
	@Test
	void testLoad_loadsOnePerson() throws DataAccessException {
		List<Person> persons = new ArrayList<>(
			Collections.singleton(new Person(
				"test_person",
				"test_user",
				"Sam",
				"Beckett",
				Gender.MALE,
				null,
				null,
				null
			))
		);
		service.load(null, persons, null);
		assertEquals(0, userCount());
		assertEquals(persons.size(), personCount());
		assertEquals(0, eventCount());
	}
	
	@Test
	void testLoad_loadsOneUser() throws DataAccessException {
		List<User> users = new ArrayList<>(
			Collections.singleton(new User(
				"test_user",
				"password",
				"user@example.com",
				"Sam",
				"Beckett",
				Gender.MALE,
				"somebody"
			))
		);
		service.load(users, null, null);
		assertEquals(users.size(), userCount());
		assertEquals(0, personCount());
		assertEquals(0, eventCount());
	}
}
