package dao;

import database.DataAccessException;
import database.Database;
import model.Gender;
import model.User;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
	private Database<DatabaseTable> db;
	private UserDao userDao;
	private User testUser;
	
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
		
		db = new Database<>(DatabaseTable.values());
		db.clearTables();
		
		Connection conn = db.getConnection();
		userDao = new UserDao(conn);
	}
	
	@AfterEach
	void tearDown() throws DataAccessException {
		// Close the old connection without saving.
		db.closeConnection(false);
	}
	
	// - Meta
	
	@Test
	void testDao_usesUserTable() {
		assertEquals(DatabaseTable.USER, userDao.table());
	}
	
	// - Insert
	
	@Test
	void testInsert_addsToEmptyDatabase() throws DataAccessException {
		userDao.insert(testUser);
		User compareTest = userDao.find(testUser.getId());
		assertNotNull(compareTest);
		assertEquals(testUser, compareTest);
	}
	
	@Test
	void testInsert_failsOnDuplicateID() throws DataAccessException {
		userDao.insert(testUser);
		assertThrows(DataAccessException.class, () -> userDao.insert(testUser));
	}
	
	// - Find
	
	@Test
	void testFind_returnsExtantRecord() throws DataAccessException {
		userDao.insert(testUser);
		@Nullable User foundEvent = userDao.find(testUser.getId());
		assertNotNull(foundEvent);
		assertEquals(testUser, foundEvent);
	}
	
	@Test
	void testFind_returnsNullIfNotFound() throws DataAccessException {
		userDao.insert(testUser);
		assertNull(userDao.find("i_hope_not"));
	}
	
	@Test
	void testFind_returnsNullIfDatabaseEmpty() throws DataAccessException {
		assertNull(userDao.find(testUser.getId()));
	}
	
	// Delete
	
	@Test
	void testDelete_removesExtantRecord() throws DataAccessException {
		userDao.insert(testUser);
		userDao.delete(testUser.getId());
		@Nullable User hopefullyNothing = userDao.find(testUser.getId());
		assertNull(hopefullyNothing, "Test data should have been removed from the database, but was found");
	}
	
	@Test
	void testDelete_doesNothingToNonexistentRecord() throws DataAccessException {
		userDao.insert(testUser);
		userDao.delete("something_else");
		assertNotNull(userDao.find(testUser.getId()), "Test data shouldn't have been touched by deleting another record. The wrong record was deleted");
	}
	
	@Test
	void testDelete_doesNothingToEmptyDatabase() throws DataAccessException {
		userDao.delete(testUser.getId());
		assertNull(userDao.find(testUser.getId()), "SANITY FAIL: No test data was put in before delete, but it was found in the database");
	}
	
	// Clear All
	
	@Test
	void testClearAll_doesNothingToEmptyDatabase() throws DataAccessException {
		userDao.clearAll();
		assertNull(userDao.find(testUser.getId()), "SANITY FAIL: Database should be clear, but test data was found");
	}
	
	@Test
	void testClearAll_clearsAllData() throws DataAccessException {
		userDao.insert(testUser);
		userDao.clearAll();
		assertNull(userDao.find(testUser.getId()), "Test event should have been cleared, but was found");
	}
}
