package dao;

import model.Gender;
import model.Person;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class PersonDaoTest {
	private Database db;
	private PersonDao personDao;
	private Person testPerson;
	
	@BeforeEach
	void setUp() throws DataAccessException {
		testPerson = new Person("person_id", "some_user", "John", "Seena", Gender.MALE, "some_father", "some_mother", null);
		
		db = new Database();
		Connection conn = db.getConnection();
		// Clear old data
		db.clearTables();
		
		personDao = new PersonDao(conn);
	}
	
	@AfterEach
	void tearDown() throws DataAccessException {
		// Close the old connection without saving.
		db.closeConnection(false);
	}
	
	@Test
	void testInsert_addsToEmptyDatabase() throws DataAccessException {
		personDao.insert(testPerson);
		Person compareTest = personDao.find(testPerson.getId());
		assertNotNull(compareTest);
		assertEquals(testPerson, compareTest);
	}
	
	@Test
	void testInsert_failsOnDuplicateID() throws DataAccessException {
		personDao.insert(testPerson);
		assertThrows(DataAccessException.class, () -> personDao.insert(testPerson));
	}
	
	@Test
	void testFind_returnsExtantRecord() throws DataAccessException {
		personDao.insert(testPerson);
		@Nullable Person foundEvent = personDao.find(testPerson.getId());
		assertNotNull(foundEvent);
		assertEquals(testPerson, foundEvent);
	}
	
	@Test
	void testFind_returnsNullIfNotFound() throws DataAccessException {
		personDao.insert(testPerson);
		assertNull(personDao.find("i_hope_not"));
	}
	
	@Test
	void testFind_returnsNullIfDatabaseEmpty() throws DataAccessException {
		assertNull(personDao.find(testPerson.getId()));
	}
	
	@Test
	void testDelete_removesExtantRecord() throws DataAccessException {
		personDao.insert(testPerson);
		personDao.delete(testPerson.getId());
		@Nullable Person hopefullyNothing = personDao.find(testPerson.getId());
		assertNull(hopefullyNothing, "Test data should have been removed from the database, but was found");
	}
	
	@Test
	void testDelete_doesNothingToNonexistentRecord() throws DataAccessException {
		personDao.insert(testPerson);
		personDao.delete("something_else");
		assertNotNull(personDao.find(testPerson.getId()), "Test data shouldn't have been touched by deleting another record. The wrong record was deleted");
	}
	
	@Test
	void testDelete_doesNothingToEmptyDatabase() throws DataAccessException {
		personDao.delete(testPerson.getId());
		assertNull(personDao.find(testPerson.getId()), "SANITY FAIL: No test data was put in before delete, but it was found in the database");
	}
	
	@Test
	void testClearAll_doesNothingToEmptyDatabase() throws DataAccessException {
		personDao.clearAll();
		assertNull(personDao.find(testPerson.getId()), "SANITY FAIL: Database should be clear, but test data was found");
	}
	
	@Test
	void testClearAll_clearsAllData() throws DataAccessException {
		personDao.insert(testPerson);
		personDao.clearAll();
		assertNull(personDao.find(testPerson.getId()), "Test event should have been cleared, but was found");
	}
}