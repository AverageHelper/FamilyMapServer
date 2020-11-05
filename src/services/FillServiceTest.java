package services;

import dao.*;
import model.Event;
import model.Gender;
import model.Person;
import model.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import utilities.ArrayHelpers;
import utilities.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FillServiceTest {
	
	private Database<DatabaseTable> db;
	private FillService service;
	private static final String TEST_USER_ID = "bob";
	private static final String INITIAL_PERSON_ID = "test_person";
	
	@BeforeEach
	void setUp() throws DataAccessException {
		db = new Database<>(Database.TEST_DATABASE_NAME, DatabaseTable.values());
		service = new FillService(db);
		db.clearTables();
	}
	
	private void setUpUser() throws DataAccessException {
		db.runTransaction(conn -> {
			UserDao userDao = new UserDao(conn);
			
			userDao.insert(new User(
				TEST_USER_ID,
				"password",
				"bsmiley@example.com",
				"Bob",
				"Smiley",
				Gender.MALE,
				null
			));
			
			return true;
		});
	}
	
	private @NotNull Person setUpUserAndPerson() throws DataAccessException {
		setUpUser();
		db.runTransaction(conn -> {
			UserDao userDao = new UserDao(conn);
			PersonDao personDao = new PersonDao(conn);
			
			userDao.update(new User(
				TEST_USER_ID,
				"password",
				"bsmiley@example.com",
				"Bob",
				"Smiley",
				Gender.MALE,
				INITIAL_PERSON_ID
			));
			personDao.insert(new Person(
				INITIAL_PERSON_ID,
				TEST_USER_ID,
				"Bob",
				"Smiley",
				Gender.MALE,
				null,
				null,
				null
			));
			
			return true;
		});
		
		Person child = getPersonForTestUser();
		assertNotNull(child);
		return child;
	}
	
	private int personCount() throws DataAccessException {
		AtomicInteger count = new AtomicInteger(0);
		
		db.runTransaction(conn -> {
			PersonDao personDao = new PersonDao(conn);
			count.set(personDao.count());
			return false;
		});
		
		return count.get();
	}
	
	private int eventCount() throws DataAccessException {
		AtomicInteger count = new AtomicInteger(0);
		
		db.runTransaction(conn -> {
			EventDao eventDao = new EventDao(conn);
			count.set(eventDao.count());
			return false;
		});
		
		return count.get();
	}
	
	private @Nullable User getTestUser() throws DataAccessException {
		AtomicReference<User> user = new AtomicReference<>(null);
		
		db.runTransaction(conn -> {
			UserDao userDao = new UserDao(conn);
			user.set(userDao.find(TEST_USER_ID));
			return false;
		});
		
		return user.get();
	}
	
	private @Nullable Person getPersonForTestUser() throws DataAccessException {
		AtomicReference<Person> person = new AtomicReference<>(null);
		
		User user = getTestUser();
		db.runTransaction(conn -> {
			if (user != null && user.getPersonID() != null) {
				PersonDao personDao = new PersonDao(conn);
				person.set(personDao.find(user.getPersonID()));
			}
			return false;
		});
		
		return person.get();
	}
	
	private static Stream<Arguments> provideGenerationCasesForFill() {
		return Stream.of(
			Arguments.of(0),
			Arguments.of(1),
			Arguments.of(2),
			Arguments.of(3),
			Arguments.of(4)
		);
	}
	
	private static Stream<Arguments> provideInvalidGenerationCasesForFill() {
		return Stream.of(
			Arguments.of(-1),
			Arguments.of(-50),
			Arguments.of(Integer.MIN_VALUE)
		);
	}
	
	private static Stream<Arguments> provideCasesForMultiGenerationFill() {
		return Stream.of(
			Arguments.of(0, 1, 1),
			Arguments.of(1, 3, 5),
			Arguments.of(2, 7, 11),
			Arguments.of(3, 15, 35),
			Arguments.of(4, 31, 63)
		);
	}
	
	
	
	// ** Fill
	
	@ParameterizedTest
	@MethodSource("provideGenerationCasesForFill")
	void testFill_usesTheUsersExistingPersonID(int generations) throws DataAccessException, IOException {
		setUpUserAndPerson();
		// Get the user
		User user = getTestUser();
		assertNotNull(user, "There is no test user");
		String personID = user.getPersonID();
		assertEquals(INITIAL_PERSON_ID, personID);
		
		service.fill(TEST_USER_ID, generations);
		
		user = getTestUser();
		assertNotNull(user);
		assertEquals(personID, user.getPersonID());
	}
	
	@ParameterizedTest
	@MethodSource("provideGenerationCasesForFill")
	void testFill_setsTheUsersPersonID(int generations) throws DataAccessException, IOException {
		setUpUser();
		// Get the user
		User user = getTestUser();
		assertNotNull(user, "There is no test user");
		assertNull(user.getPersonID());
		
		service.fill(TEST_USER_ID, generations);
		
		user = getTestUser();
		assertNotNull(user);
		assertNotNull(user.getPersonID());
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	void testFill_throwsIfUsernameIsNullOrEmpty(String userName) {
		assertThrows(IllegalArgumentException.class, () -> service.fill(userName, 4));
	}
	
	@ParameterizedTest
	@MethodSource("provideInvalidGenerationCasesForFill")
	void testFill_throwsIfGenerationsIsInvalid(int generations) {
		assertThrows(
			IllegalArgumentException.class,
			() -> service.fill(TEST_USER_ID, generations)
		);
	}
	
	
	
	// ** New Person for User
	
	@ParameterizedTest
	@NullAndEmptySource
	void testNewPersonForUser_throwsIfUsernameIsNullOrEmpty(String userName) {
		assertThrows(
			IllegalArgumentException.class,
			() -> service.newPersonForUser(userName)
		);
	}
	
	@Test
	void testNewPersonForUser_throwsIfUserIsNotFound() throws DataAccessException {
		setUpUser();
		assertThrows(
			DataAccessException.class,
			() -> service.newPersonForUser("cant_touch_this")
		);
	}
	
	@Test
	void testNewPersonForUser_createsNewPerson() throws DataAccessException {
		setUpUser();
		assertEquals(0, personCount(), "Setup phase created a person");
		service.newPersonForUser(TEST_USER_ID);
		assertEquals(1, personCount());
	}
	
	@Test
	void testNewPersonForUser_attachesNewPersonToUser() throws DataAccessException {
		setUpUser();
		Person newPerson = service.newPersonForUser(TEST_USER_ID);
		assertNotNull(newPerson);
		
		User user = getTestUser();
		assertNotNull(user);
		assertEquals(newPerson.getId(), user.getPersonID());
	}
	
	
	
	// ** Clear Old Data
	
	@ParameterizedTest
	@NullAndEmptySource
	void testClearFormerData_throwsIfUsernameIsNullOrEmpty(String userName) {
		assertThrows(
			IllegalArgumentException.class,
			() -> service.clearFormerData(userName)
		);
	}
	
	@Test
	void testClearFormerData_throwsIfUserIsNotFound() throws DataAccessException {
		setUpUser();
		assertThrows(
			DataAccessException.class,
			() -> service.clearFormerData("cant_touch_this")
		);
	}
	
	@Test
	void testClearFormerData_doesNotDeleteUser() throws DataAccessException {
		setUpUserAndPerson();
		User user = getTestUser();
		assertNotNull(user);
		
		service.clearFormerData(TEST_USER_ID);
		
		user = getTestUser();
		assertNotNull(user);
	}
	
	@Test
	void testClearFormerData_deletesPersonData() throws DataAccessException {
		setUpUserAndPerson();
		assertEquals(1, personCount());
		
		service.clearFormerData(TEST_USER_ID);
		
		assertEquals(0, personCount());
	}
	
	
	
	// ** This Year
	
	@Test
	void testThisYear_returnsAtLeast2020() {
		assertTrue(service.thisYear() >= 2020);
	}
	
	
	
	// ** Generating Persons and Events
	
	@ParameterizedTest
	@NullAndEmptySource
	void testGenerationsFromChild_throwsIfUsernameIsNullOrEmpty(String userName) throws DataAccessException {
		Person testPerson = setUpUserAndPerson();
		
		assertThrows(
			IllegalArgumentException.class,
			() -> service.generationsFromChild(4, testPerson, userName)
		);
	}
	
	@ParameterizedTest
	@MethodSource("provideInvalidGenerationCasesForFill")
	void testGenerationsFromChild_throwsIfGenerationsIsInvalid(int generations) throws DataAccessException {
		Person testPerson = setUpUserAndPerson();
		
		assertThrows(
			IllegalArgumentException.class,
			() -> service.generationsFromChild(generations, testPerson, TEST_USER_ID)
		);
	}
	
	@ParameterizedTest
	@NullSource
	void testGenerationsFromChild_throwsIfChildIsNull(Person child) {
		assertThrows(
			IllegalArgumentException.class,
			() -> service.generationsFromChild(4, child, TEST_USER_ID)
		);
	}
	
	@ParameterizedTest
	@MethodSource("provideGenerationCasesForFill")
	void testGenerationsFromChild_doesNotAddData(int generations) throws DataAccessException, IOException {
		Person child = setUpUserAndPerson();
		
		assertEquals(0, eventCount());
		assertEquals(1, personCount());
		service.generationsFromChild(generations, child, TEST_USER_ID);
		assertEquals(0, eventCount());
		assertEquals(1, personCount());
	}
	
	@ParameterizedTest
	@MethodSource("provideGenerationCasesForFill")
	void testGenerationsFromChild_userOwnsAllGeneratedRecords(int generations) throws DataAccessException, IOException {
		Person child = setUpUserAndPerson();
		
		Pair<List<Person>, List<Event>> result =
			service.generationsFromChild(generations, child, TEST_USER_ID);
		
		List<Person> personsToAdd = result.getFirst();
		List<Event> eventsToAdd = result.getSecond();
		
		for (Person person : personsToAdd) {
			assertEquals(TEST_USER_ID, person.getAssociatedUsername());
		}
		for (Event event : eventsToAdd) {
			assertEquals(TEST_USER_ID, event.getAssociatedUsername());
		}
	}
	
	@ParameterizedTest
	@MethodSource("provideCasesForMultiGenerationFill")
	void testGenerationsFromChild_fillsTheCorrectNumberOfNewEntries(
		int generations, int addedPersons, int addedEvents
	) throws DataAccessException, IOException {
		Person child = setUpUserAndPerson();
		
		Pair<List<Person>, List<Event>> result =
			service.generationsFromChild(generations, child, TEST_USER_ID);
		
		List<Person> personsToAdd = result.getFirst();
		List<Event> eventsToAdd = result.getSecond();
		
		assertEquals(addedPersons, personsToAdd.size(),
			"Incorrect number of new Person entries");
		assertTrue(eventsToAdd.size() >= addedEvents,
			"Incorrect number of new Event entries");
	}
	
	@Test
	void testGenerationsFromChild_parentsOlderThanChild() throws DataAccessException, IOException {
		Person child = setUpUserAndPerson();
		Pair<List<Person>, List<Event>> result =
			service.generationsFromChild(1, child, TEST_USER_ID);
		
		List<Person> parents = result.getFirst();
		List<Event> events = result.getSecond();
		
		parents.remove(child);
		List<Event> birthEvents = new ArrayList<>();
		for (Event event : events) {
			if (event.getEventType().equals("birth")) {
				birthEvents.add(event);
			}
		}
		
		assertNotNull(child.getFatherID());
		assertNotNull(child.getMotherID());
		
		Event dadBirth = ArrayHelpers.firstElementThatMatches(birthEvents,
			e -> e.getPersonID().equals(child.getFatherID()));
		Event momBirth = ArrayHelpers.firstElementThatMatches(birthEvents,
			e -> e.getPersonID().equals(child.getMotherID()));
		Event childBirth = ArrayHelpers.firstElementThatMatches(birthEvents,
			e -> e.getPersonID().equals(child.getId()));
		
		assertNotNull(dadBirth);
		assertNotNull(momBirth);
		assertNotNull(childBirth);
		
		int fatherAgeDiff = childBirth.getYear() - dadBirth.getYear();
		int motherAgeDiff = childBirth.getYear() - dadBirth.getYear();
		
		assertTrue(fatherAgeDiff >= 13);
		assertTrue(motherAgeDiff >= 13);
	}
	
	
	
	
	
	// ** Single Household
	
	@ParameterizedTest
	@NullSource
	void testParentalGenerationFromChild_throwsIfChildIsNull(Person child) {
		assertThrows(
			IllegalArgumentException.class,
			() -> service.parentalGenerationFromChild(child, TEST_USER_ID, 2020)
		);
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	void testParentalGenerationFromChild_throwsIfUsernameIsEmptyOrNull(String userName) throws DataAccessException {
		Person child = setUpUserAndPerson();
		
		assertThrows(
			IllegalArgumentException.class,
			() -> service.parentalGenerationFromChild(child, userName, 2020)
		);
	}
	
	@Test
	void testParentalGenerationFromChild_createsTwoParents() throws DataAccessException, IOException {
		Person child = setUpUserAndPerson();
		Pair<List<Person>, List<Event>> result =
			service.parentalGenerationFromChild(child, TEST_USER_ID, 2020);
		List<Person> people = result.getFirst();
		
		assertEquals(2, people.size());
		assertFalse(people.contains(child));
		
		assertNotNull(child.getMotherID());
		assertNotNull(child.getFatherID());
		assertNotEquals(child.getFatherID(), child.getMotherID());
		
		for (Person person : people) {
			assertTrue(
				child.getMotherID().equals(person.getId()) ||
					child.getFatherID().equals(person.getId())
			);
		}
	}
	
	@Test
	void testParentalGenerationFromChild_createsReasonableBirthYearsForParents() throws DataAccessException, IOException {
		int fathersBirthYear = 2020;
		
		Person child = setUpUserAndPerson();
		Pair<List<Person>, List<Event>> result =
			service.parentalGenerationFromChild(child, TEST_USER_ID, fathersBirthYear);
		List<Event> events = result.getSecond();
		
		// We get one birth per parent
		List<Event> birthEvents = new ArrayList<>();
		for (Event event : events) {
			if (event.getEventType().equals("birth")) {
				birthEvents.add(event);
			}
		}
		assertEquals(2, birthEvents.size());
		
		// Similar parental birth years
		for (Event event : birthEvents) {
			assertEquals(fathersBirthYear, event.getYear(), 2.5);
		}
	}
	
	@Test
	void testParentalGenerationFromChild_createsReasonableMarriageYearsForParents() throws DataAccessException, IOException {
		int fathersBirthYear = 2020;
		
		Person child = setUpUserAndPerson();
		Pair<List<Person>, List<Event>> result =
			service.parentalGenerationFromChild(child, TEST_USER_ID, fathersBirthYear);
		List<Event> events = result.getSecond();
		
		// We get one marriage per parent
		List<Event> marriageEvents = new ArrayList<>();
		for (Event event : events) {
			if (event.getEventType().equals("marriage")) {
				marriageEvents.add(event);
			}
		}
		assertEquals(2, marriageEvents.size());
		
		// Identical marriage years
		Event marriage1 = marriageEvents.get(0);
		Event marriage2 = marriageEvents.get(0);
		assertEquals(marriage1.getYear(), marriage2.getYear());
		
		// Married over 18
		int marriageAge = marriage1.getYear() - fathersBirthYear;
		assertTrue(marriageAge >= 18,
			"Marriage age should have been over 18, but was " + marriageAge);
	}
}
