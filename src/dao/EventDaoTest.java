package dao;

import model.Event;
import model.EventType;
import model.Gender;
import model.User;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventDaoTest {
	private Database db;
	private UserDao userDao;
	private EventDao eventDao;
	private User testUser;
	private Event testEventA;
	private Event testEventB;
	
	@BeforeEach
	void setUp() throws DataAccessException {
		testUser = new User(
			"test_user",
			"Pa$$w0rd",
			"test.user@example.com",
			"Test",
			"User",
			Gender.FEMALE,
			null
		);
		testEventA = new Event(
			"event_a",
			testUser.getUserName(),
			"karen",
			35.9,
			140.1,
			"United States",
			"Provo",
			"Death",
			1987
		);
		testEventB = new Event(
			"event_b",
			testUser.getUserName(),
			testEventA.getPersonID(),
			testEventA.getLatitude(),
			testEventA.getLongitude(),
			testEventA.getCountry(),
			testEventA.getCity(),
			"Death",
			2020
		);
		
		db = new Database();
		db.clearTables();
		
		Connection conn = db.getConnection();
		userDao = new UserDao(conn);
		eventDao = new EventDao(conn);
	}
	
	@AfterEach
	void tearDown() throws DataAccessException {
		// Close the old connection without saving.
		db.closeConnection(false);
	}
	
	// - Meta
	
	@Test
	void testDao_usesEventTable() {
		assertEquals(DatabaseTable.EVENT, eventDao.table());
	}
	
	// - Insert
	
	@Test
	void testInsert_addsToEmptyDatabase() throws DataAccessException {
		eventDao.insert(testEventA);
		Event compareTest = eventDao.find(testEventA.getId());
		assertNotNull(compareTest);
		assertEquals(testEventA, compareTest);
	}
	
	@Test
	void testInsert_failsOnDuplicateID() throws DataAccessException {
		eventDao.insert(testEventA);
		assertThrows(DataAccessException.class, () -> eventDao.insert(testEventA));
	}
	
	// - Find Single
	
	@Test
	void testFind_returnsExtantRecord() throws DataAccessException {
		eventDao.insert(testEventA);
		@Nullable Event foundEvent = eventDao.find(testEventA.getId());
		assertNotNull(foundEvent);
		assertEquals(testEventA, foundEvent);
	}
	
	@Test
	void testFind_returnsNullIfNotFound() throws DataAccessException {
		eventDao.insert(testEventA);
		assertNull(eventDao.find("i_hope_not"));
	}
	
	@Test
	void testFind_returnsNullIfDatabaseEmpty() throws DataAccessException {
		assertNull(eventDao.find(testEventA.getId()));
	}
	
	// - Find Multiple
	
	@Test
	void testFindForUser_returnsNoEventsForUnknownUser() throws DataAccessException {
		userDao.insert(testUser);
		eventDao.insert(testEventA);
		List<Event> events = eventDao.findForUser("not_sure....");
		assertNotNull(events);
		assertTrue(events.isEmpty());
	}
	
	@Test
	void testFindForUser_returnsNoEvents() throws DataAccessException {
		List<Event> events = eventDao.findForUser(testUser.getUserName());
		assertNotNull(events);
		assertTrue(events.isEmpty());
	}
	
	@Test
	void testFindForUser_returnsOneEventForUser() throws DataAccessException {
		userDao.insert(testUser);
		eventDao.insert(testEventA);
		List<Event> events = eventDao.findForUser(testUser.getUserName());
		assertNotNull(events);
		assertFalse(events.isEmpty());
		assertEquals(1, events.size());
		assertEquals(testEventA, events.get(0));
	}
	
	@Test
	void testFindForUser_returnsMultipleEventsForUser() throws DataAccessException {
		userDao.insert(testUser);
		eventDao.insert(testEventA);
		eventDao.insert(testEventB);
		List<Event> events = eventDao.findForUser(testUser.getUserName());
		assertNotNull(events);
		assertFalse(events.isEmpty());
		assertEquals(2, events.size());
		assertEquals(testEventA, events.get(0));
		assertEquals(testEventB, events.get(1));
	}
	
	// - Delete
	
	@Test
	void testDelete_removesExtantRecord() throws DataAccessException {
		eventDao.insert(testEventA);
		eventDao.delete(testEventA.getId());
		@Nullable Event hopefullyNothing = eventDao.find(testEventA.getId());
		assertNull(hopefullyNothing, "Test data should have been removed from the database, but was found");
	}
	
	@Test
	void testDelete_doesNothingToNonexistentRecord() throws DataAccessException {
		eventDao.insert(testEventA);
		eventDao.delete("something_else");
		assertNotNull(eventDao.find(testEventA.getId()), "Test data shouldn't have been touched by deleting another record. The wrong record was deleted");
	}
	
	@Test
	void testDelete_doesNothingToEmptyDatabase() throws DataAccessException {
		eventDao.delete(testEventA.getId());
		assertNull(eventDao.find(testEventA.getId()), "SANITY FAIL: No test data was put in before delete, but it was found in the database");
	}
	
	// - Clear All
	
	@Test
	void testClearAll_doesNothingToEmptyDatabase() throws DataAccessException {
		eventDao.clearAll();
		assertNull(eventDao.find(testEventA.getId()), "SANITY FAIL: Database should be clear, but test data was found");
	}
	
	@Test
	void testClearAll_clearsAllData() throws DataAccessException {
		eventDao.insert(testEventA);
		eventDao.clearAll();
		assertNull(eventDao.find(testEventA.getId()), "Test event should have been cleared, but was found");
	}
}
