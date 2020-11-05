package dao;

import model.Gender;
import model.Person;
import model.User;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonDaoTest {
	private Database<DatabaseTable> db;
	private UserDao userDao;
	private PersonDao personDao;
	private User testUser;
	private Person testPersonA;
	private Person testPersonB;
	
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
		testPersonA = new Person(
			"person_a",
			testUser.getUserName(),
			"John",
			"Cena",
			Gender.MALE,
			"some_father",
			"some_mother",
			null
		);
		testPersonB = new Person(
			"person_b",
			testUser.getUserName(),
			"Karen",
			testPersonA.getLastName(),
			Gender.FEMALE,
			testPersonA.getFatherID(),
			testPersonA.getMotherID(),
			"somebody"
		);
		
		db = new Database<>(DatabaseTable.values());
		db.clearTables();
		
		Connection conn = db.getConnection();
		userDao = new UserDao(conn);
		personDao = new PersonDao(conn);
	}
	
	@AfterEach
	void tearDown() throws DataAccessException {
		// Close the old connection without saving.
		db.closeConnection(false);
	}
	
	// - Meta
	
	@Test
	void testDao_usesPersonTable() {
		assertEquals(DatabaseTable.PERSON, personDao.table());
	}
	
	// - Insert
	
	@Test
	void testInsert_addsToEmptyDatabase() throws DataAccessException {
		personDao.insert(testPersonA);
		Person compareTest = personDao.find(testPersonA.getId());
		assertNotNull(compareTest);
		assertEquals(testPersonA, compareTest);
	}
	
	@Test
	void testInsert_failsOnDuplicateID() throws DataAccessException {
		personDao.insert(testPersonA);
		assertThrows(DataAccessException.class, () -> personDao.insert(testPersonA));
	}
	
	// - Find Single
	
	@Test
	void testFind_returnsExtantRecord() throws DataAccessException {
		personDao.insert(testPersonA);
		@Nullable Person foundEvent = personDao.find(testPersonA.getId());
		assertNotNull(foundEvent);
		assertEquals(testPersonA, foundEvent);
	}
	
	@Test
	void testFind_returnsNullIfNotFound() throws DataAccessException {
		personDao.insert(testPersonA);
		assertNull(personDao.find("i_hope_not"));
	}
	
	@Test
	void testFind_returnsNullIfDatabaseEmpty() throws DataAccessException {
		assertNull(personDao.find(testPersonA.getId()));
	}
	
	// - Find Multiple
	
	@Test
	void testFindForUser_returnsNoPersonsForUnknownUser() throws DataAccessException {
		userDao.insert(testUser);
		personDao.insert(testPersonA);
		List<Person> events = personDao.findForUser("not_sure....");
		assertNotNull(events);
		assertTrue(events.isEmpty());
	}
	
	@Test
	void testFindForUser_returnsNoPersons() throws DataAccessException {
		List<Person> events = personDao.findForUser(testUser.getUserName());
		assertNotNull(events);
		assertTrue(events.isEmpty());
	}
	
	@Test
	void testFindForUser_returnsOnePersonForUser() throws DataAccessException {
		userDao.insert(testUser);
		personDao.insert(testPersonA);
		List<Person> events = personDao.findForUser(testUser.getUserName());
		assertNotNull(events);
		assertFalse(events.isEmpty());
		assertEquals(1, events.size());
		assertEquals(testPersonA, events.get(0));
	}
	
	@Test
	void testFindForUser_returnsMultiplePersonsForUser() throws DataAccessException {
		userDao.insert(testUser);
		personDao.insert(testPersonA);
		personDao.insert(testPersonB);
		List<Person> events = personDao.findForUser(testUser.getUserName());
		assertNotNull(events);
		assertFalse(events.isEmpty());
		assertEquals(2, events.size());
		assertEquals(testPersonA, events.get(0));
		assertEquals(testPersonB, events.get(1));
	}
	
	// - Delete
	
	@Test
	void testDelete_removesExtantRecord() throws DataAccessException {
		personDao.insert(testPersonA);
		personDao.delete(testPersonA.getId());
		@Nullable Person hopefullyNothing = personDao.find(testPersonA.getId());
		assertNull(hopefullyNothing, "Test data should have been removed from the database, but was found");
	}
	
	@Test
	void testDelete_doesNothingToNonexistentRecord() throws DataAccessException {
		personDao.insert(testPersonA);
		personDao.delete("something_else");
		assertNotNull(personDao.find(testPersonA.getId()), "Test data shouldn't have been touched by deleting another record. The wrong record was deleted");
	}
	
	@Test
	void testDelete_doesNothingToEmptyDatabase() throws DataAccessException {
		personDao.delete(testPersonA.getId());
		assertNull(personDao.find(testPersonA.getId()), "SANITY FAIL: No test data was put in before delete, but it was found in the database");
	}
	
	// - Clear All
	
	@Test
	void testClearAll_doesNothingToEmptyDatabase() throws DataAccessException {
		personDao.clearAll();
		assertNull(personDao.find(testPersonA.getId()), "SANITY FAIL: Database should be clear, but test data was found");
	}
	
	@Test
	void testClearAll_clearsAllData() throws DataAccessException {
		personDao.insert(testPersonA);
		personDao.clearAll();
		assertNull(personDao.find(testPersonA.getId()), "Test event should have been cleared, but was found");
	}
}
