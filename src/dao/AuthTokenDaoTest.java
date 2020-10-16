package dao;

import model.AuthToken;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AuthTokenDaoTest {
	private Database db;
	private AuthTokenDao authTokenDao;
	private AuthToken testToken;
	
	@BeforeEach
	void setUp() throws DataAccessException {
		testToken = new AuthToken(
			"token_id",
			"some_user",
			new Date(),
			true
		);
		
		db = new Database();
		Connection conn = db.getConnection();
		// Clear old data
		db.clearTables();
		
		authTokenDao = new AuthTokenDao(conn);
	}
	
	@AfterEach
	void tearDown() throws DataAccessException {
		// Close the old connection without saving.
		db.closeConnection(false);
	}
	
	// - Meta
	
	@Test
	void testDao_usesAuthTokenTable() {
		assertEquals(DatabaseTable.AUTH_TOKEN, authTokenDao.table());
	}
	
	// - Insert
	
	@Test
	void testInsert_addsToEmptyDatabase() throws DataAccessException {
		authTokenDao.insert(testToken);
		AuthToken compareTest = authTokenDao.find(testToken.getId());
		assertNotNull(compareTest);
		assertEquals(testToken, compareTest);
	}
	
	@Test
	void testInsert_failsOnDuplicateID() throws DataAccessException {
		authTokenDao.insert(testToken);
		assertThrows(DataAccessException.class, () -> authTokenDao.insert(testToken));
	}
	
	// - Find
	
	@Test
	void testFind_returnsExtantRecord() throws DataAccessException {
		authTokenDao.insert(testToken);
		@Nullable AuthToken foundEvent = authTokenDao.find(testToken.getId());
		assertNotNull(foundEvent);
		assertEquals(testToken, foundEvent);
	}
	
	@Test
	void testFind_returnsNullIfNotFound() throws DataAccessException {
		authTokenDao.insert(testToken);
		assertNull(authTokenDao.find("i_hope_not"));
	}
	
	@Test
	void testFind_returnsNullIfDatabaseEmpty() throws DataAccessException {
		assertNull(authTokenDao.find(testToken.getId()));
	}
	
	// - Delete
	
	@Test
	void testDelete_removesExtantRecord() throws DataAccessException {
		authTokenDao.insert(testToken);
		authTokenDao.delete(testToken.getId());
		@Nullable AuthToken hopefullyNothing = authTokenDao.find(testToken.getId());
		assertNull(hopefullyNothing, "Test data should have been removed from the database, but was found");
	}
	
	@Test
	void testDelete_doesNothingToNonexistentRecord() throws DataAccessException {
		authTokenDao.insert(testToken);
		authTokenDao.delete("something_else");
		assertNotNull(authTokenDao.find(testToken.getId()), "Test data shouldn't have been touched by deleting another record. The wrong record was deleted");
	}
	
	@Test
	void testDelete_doesNothingToEmptyDatabase() throws DataAccessException {
		authTokenDao.delete(testToken.getId());
		assertNull(authTokenDao.find(testToken.getId()), "SANITY FAIL: No test data was put in before delete, but it was found in the database");
	}
	
	// - Clear All
	
	@Test
	void testClearAll_doesNothingToEmptyDatabase() throws DataAccessException {
		authTokenDao.clearAll();
		assertNull(authTokenDao.find(testToken.getId()), "SANITY FAIL: Database should be clear, but test data was found");
	}
	
	@Test
	void testClearAll_clearsAllData() throws DataAccessException {
		authTokenDao.insert(testToken);
		authTokenDao.clearAll();
		assertNull(authTokenDao.find(testToken.getId()), "Test event should have been cleared, but was found");
	}
}
