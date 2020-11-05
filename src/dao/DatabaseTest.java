package dao;

import database.DataAccessException;
import database.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
	
	Database<DatabaseTable> db;
	
	@BeforeEach
	void setUp() {
		db = new Database<>(Database.TEST_DATABASE_NAME, DatabaseTable.values());
	}
	
	@Test
	void testClearTables_clearsAllTables() throws DataAccessException {
		// TODO: Test clearTables() and clearTablesUsingConnection()
		// Spy on SQL API calls?
	}
	
	@Test
	void testRunTransaction_throwsWithOpenConnection() throws DataAccessException {
		db.openConnection();
		assertThrows(DataAccessException.class, () -> db.runTransaction(conn -> false));
	}
	
	@Test
	void testRunTransaction_runsWithoutConnection() throws DataAccessException {
		db.runTransaction(conn -> false);
	}
}
