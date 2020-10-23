package services;

import dao.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
	private Database db;
	private ClearService service;
	private final String testTokenId = "this_authorizes_test_user";
	private final String testEventId = "test_event";
	private final String testUserId = "test_user";
	private final String testPersonId = "person_a";
	
	private void preloadDatabase() throws DataAccessException {
		db.runTransaction(conn -> {
			EventDao eventDao = new EventDao(conn);
			PersonDao personDao = new PersonDao(conn);
			UserDao userDao = new UserDao(conn);
			
			Event testEvent = new Event(
				testEventId,
				testUserId,
				testPersonId,
				null,
				null,
				null,
				null,
				EventType.BIRTH,
				2020
			);
			eventDao.insert(testEvent);
			
			User testUser = new User(
				testUserId,
				"Pa$$w0rd",
				"test.user@example.com",
				"Test",
				"User",
				Gender.FEMALE,
				null
			);
			userDao.insert(testUser);
			
			Person testPerson = new Person(
				testPersonId,
				testUser.getUsername(),
				"John",
				"Cena",
				Gender.MALE,
				"some_father",
				"some_mother",
				null
			);
			personDao.insert(testPerson);
			
			return true;
		});
	}
	
	@BeforeEach
	void setUp() throws DataAccessException {
		db = new Database(Database.TEST_DATABASE_NAME);
		service = new ClearService(db);
		db.clearTables();
	}
	
	private void assertDatabaseEmpty() throws DataAccessException {
		// We should find nothing in the database now
		db.runTransaction(conn -> {
			AuthTokenDao authTokenDao = new AuthTokenDao(conn);
			EventDao eventDao = new EventDao(conn);
			PersonDao personDao = new PersonDao(conn);
			UserDao userDao = new UserDao(conn);
			
			assertNull(authTokenDao.find(testTokenId));
			assertNull(eventDao.find(testEventId));
			assertNull(userDao.find(testUserId));
			assertNull(personDao.find(testPersonId));
			
			return false;
		});
	}
	
	@Test
	void testClear_clearsDataFromAllTables() throws DataAccessException {
		preloadDatabase();
		assertTrue(service.clear());
		assertDatabaseEmpty();
	}
}