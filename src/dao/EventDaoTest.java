package dao;

import model.Event;
import model.EventType;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class EventDaoTest {
	private Database db;
	private EventDao eventDao;
	private Event testEvent;
	
	@BeforeEach
	void setUp() throws DataAccessException {
		testEvent = new Event("event_id", "somebody", "karen", 35.9, 140.1, "United States", "Provo", EventType.BIRTH, 2019);
		
		db = new Database();
		Connection conn = db.getConnection();
		// Clear old data
		db.clearTables();
		
		eventDao = new EventDao(conn);
	}
	
	@AfterEach
	void tearDown() throws DataAccessException {
		// Close the old connection without saving.
		db.closeConnection(false);
	}
	
	@Test
	void testInsert_addsToEmptyDatabase() throws DataAccessException {
		eventDao.insert(testEvent);
		Event compareTest = eventDao.find(testEvent.getId());
		assertNotNull(compareTest);
		assertEquals(testEvent, compareTest);
	}
	
	@Test
	void testInsert_failsOnDuplicateID() throws DataAccessException {
		eventDao.insert(testEvent);
		assertThrows(DataAccessException.class, () -> eventDao.insert(testEvent));
	}
	
	@Test
	void testFind_returnsExtantRecord() throws DataAccessException {
		eventDao.insert(testEvent);
		@Nullable Event foundEvent = eventDao.find(testEvent.getId());
		assertNotNull(foundEvent);
		assertEquals(testEvent, foundEvent);
	}
	
	@Test
	void testFind_returnsNullIfNotFound() throws DataAccessException {
		eventDao.insert(testEvent);
		assertNull(eventDao.find("i_hope_not"));
	}
	
	@Test
	void testFind_returnsNullIfDatabaseEmpty() throws DataAccessException {
		assertNull(eventDao.find(testEvent.getId()));
	}
	
	@Test
	void testDelete_removesExtantRecord() throws DataAccessException {
		eventDao.insert(testEvent);
		eventDao.delete(testEvent.getId());
		@Nullable Event hopefullyNothing = eventDao.find(testEvent.getId());
		assertNull(hopefullyNothing, "Test data should have been removed from the database, but was found");
	}
	
	@Test
	void testDelete_doesNothingToNonexistentRecord() throws DataAccessException {
		eventDao.insert(testEvent);
		eventDao.delete("something_else");
		assertNotNull(eventDao.find(testEvent.getId()), "Test data shouldn't have been touched by deleting another record. The wrong record was deleted");
	}
	
	@Test
	void testDelete_doesNothingToEmptyDatabase() throws DataAccessException {
		eventDao.delete(testEvent.getId());
		assertNull(eventDao.find(testEvent.getId()), "SANITY FAIL: No test data was put in before delete, but it was found in the database");
	}
	
	@Test
	void testClearAll_doesNothingToEmptyDatabase() throws DataAccessException {
		eventDao.clearAll();
		assertNull(eventDao.find(testEvent.getId()), "SANITY FAIL: Database should be clear, but test data was found");
	}
	
	@Test
	void testClearAll_clearsAllData() throws DataAccessException {
		eventDao.insert(testEvent);
		eventDao.clearAll();
		assertNull(eventDao.find(testEvent.getId()), "Test event should have been cleared, but was found");
	}
}
