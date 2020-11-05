package services;

import dao.*;
import database.DataAccessException;
import database.Database;
import handlers.FetchDataRequest;
import model.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FetchDataServiceTest {
	
	private Database<DatabaseTable> db;
	private FetchDataService service;
	private static final String TEST_USER_ID = "bob";
	private static final String TEST_PERSON_ID = "test_person";
	private static final String TEST_EVENT_ID = "test_event";
	private static final String TEST_TOKEN_ID = "test_token";
	
	@BeforeEach
	void setUp() throws DataAccessException {
		db = new Database<>(Database.TEST_DATABASE_NAME, DatabaseTable.values());
		service = new FetchDataService(db);
		db.clearTables();
	}
	
	private @NotNull User setUpUser() throws DataAccessException {
		User user = new User(
			TEST_USER_ID,
			"password",
			"bsmiley@example.com",
			"Bob",
			"Smiley",
			Gender.MALE,
			null
		);
		db.runTransaction(conn -> {
			UserDao dao = new UserDao(conn);
			dao.insert(user);
			return true;
		});
		
		return user;
	}
	
	private @NotNull Person setUpPerson(@NotNull String id) throws DataAccessException {
		Person person = new Person(
			id,
			TEST_USER_ID,
			"Bob",
			"Smiley",
			Gender.MALE,
			null,
			null,
			null
		);
		db.runTransaction(conn -> {
			PersonDao dao = new PersonDao(conn);
			dao.insert(person);
			return true;
		});
		
		return person;
	}
	
	private @NotNull Event setUpEvent(@NotNull String id) throws DataAccessException {
		Event event = new Event(
			id,
			TEST_USER_ID,
			TEST_PERSON_ID,
			null,
			null,
			null,
			null,
			"something",
			2020
		);
		db.runTransaction(conn -> {
			EventDao dao = new EventDao(conn);
			dao.insert(event);
			return true;
		});
		
		return event;
	}
	
	private @NotNull AuthToken setUpAuthToken(@NotNull String id) throws DataAccessException {
		AuthToken token = new AuthToken(
			id,
			TEST_USER_ID,
			new Date(),
			true
		);
		db.runTransaction(conn -> {
			AuthTokenDao dao = new AuthTokenDao(conn);
			dao.insert(token);
			return true;
		});
		
		return token;
	}
	
	// ** Empty Database
	
	@ParameterizedTest
	@EnumSource(DatabaseTable.class)
	void testFetch_resultsEmptyWithEmptyDB(DatabaseTable table) throws DataAccessException {
		FetchDataResult<ModelData> result =
			service.fetch(new FetchDataRequest(table, TEST_USER_ID));
		assertNotNull(result);
		assertNull(result.getFailureReason());
		assertNotNull(result.getData());
		assertTrue(result.getData().isEmpty());
	}
	
	private static Stream<Arguments> provideCasesForFetchByID() {
		return Stream.of(
			Arguments.of(DatabaseTable.USER, TEST_USER_ID),
			Arguments.of(DatabaseTable.PERSON, TEST_PERSON_ID),
			Arguments.of(DatabaseTable.EVENT, TEST_EVENT_ID),
			Arguments.of(DatabaseTable.AUTH_TOKEN, TEST_TOKEN_ID)
		);
	}
	
	@ParameterizedTest
	@MethodSource("provideCasesForFetchByID")
	void testFetch_returnsIDNotFoundWithEmptyDB(DatabaseTable table, String id) throws DataAccessException {
		FetchDataRequest req = new FetchDataRequest(table, TEST_USER_ID);
		req.setId(id);
		FetchDataResult<ModelData> result = service.fetch(req);
		assertNotNull(result);
		assertNull(result.getData());
		assertNotNull(result.getFailureReason());
		assertEquals(FetchDataFailureReason.NOT_FOUND, result.getFailureReason());
	}
	
	
	
	// ** List Results for User
	
	@Test
	void testFetch_listsOneUserByUsername() throws DataAccessException {
		User keyObject = setUpUser();
		FetchDataResult<User> result =
			service.fetch(new FetchDataRequest(DatabaseTable.USER, TEST_USER_ID));
		assertNotNull(result);
		assertNull(result.getFailureReason());
		assertNotNull(result.getData());
		assertEquals(1, result.getData().size());
		assertEquals(keyObject, result.getData().get(0));
	}
	
	@Test
	void testFetch_listsOnePersonByUsername() throws DataAccessException {
		Person keyObject = setUpPerson(TEST_PERSON_ID);
		FetchDataResult<Person> result =
			service.fetch(new FetchDataRequest(DatabaseTable.PERSON, TEST_USER_ID));
		assertNotNull(result);
		assertNull(result.getFailureReason());
		assertNotNull(result.getData());
		assertEquals(1, result.getData().size());
		assertEquals(keyObject, result.getData().get(0));
	}
	
	@Test
	void testFetch_listsOneEventByUsername() throws DataAccessException {
		Event keyObject = setUpEvent(TEST_EVENT_ID);
		FetchDataResult<Event> result =
			service.fetch(new FetchDataRequest(DatabaseTable.EVENT, TEST_USER_ID));
		assertNotNull(result);
		assertNull(result.getFailureReason());
		assertNotNull(result.getData());
		assertEquals(1, result.getData().size());
		assertEquals(keyObject, result.getData().get(0));
	}
	
	@Test
	void testFetch_listsOneAuthTokenByUsername() throws DataAccessException {
		AuthToken keyObject = setUpAuthToken(TEST_TOKEN_ID);
		FetchDataResult<AuthToken> result =
			service.fetch(new FetchDataRequest(DatabaseTable.AUTH_TOKEN, TEST_USER_ID));
		assertNotNull(result);
		assertNull(result.getFailureReason());
		assertNotNull(result.getData());
		assertEquals(1, result.getData().size());
		assertEquals(keyObject, result.getData().get(0));
	}
	
	@Test
	void testFetch_listsTwoPersonsByUsername() throws DataAccessException {
		setUpPerson(TEST_PERSON_ID);
		setUpPerson(TEST_PERSON_ID + "2");
		FetchDataResult<Person> result =
			service.fetch(new FetchDataRequest(DatabaseTable.PERSON, TEST_USER_ID));
		assertNotNull(result);
		assertNull(result.getFailureReason());
		assertNotNull(result.getData());
		assertEquals(2, result.getData().size());
	}
	
	@Test
	void testFetch_listsTwoEventsByUsername() throws DataAccessException {
		setUpEvent(TEST_EVENT_ID);
		setUpEvent(TEST_EVENT_ID + "2");
		FetchDataResult<Event> result =
			service.fetch(new FetchDataRequest(DatabaseTable.EVENT, TEST_USER_ID));
		assertNotNull(result);
		assertNull(result.getFailureReason());
		assertNotNull(result.getData());
		assertEquals(2, result.getData().size());
	}
	
	@Test
	void testFetch_listsTwoAuthTokensByUsername() throws DataAccessException {
		setUpAuthToken(TEST_TOKEN_ID);
		setUpAuthToken(TEST_TOKEN_ID + "2");
		FetchDataResult<AuthToken> result =
			service.fetch(new FetchDataRequest(DatabaseTable.AUTH_TOKEN, TEST_USER_ID));
		assertNotNull(result);
		assertNull(result.getFailureReason());
		assertNotNull(result.getData());
		assertEquals(2, result.getData().size());
	}
	
	
	
	// ** Return One Result with ID
	
	@Test
	void testFetch_findOneUserByID() throws DataAccessException {
		User keyObject = setUpUser();
		FetchDataRequest req = new FetchDataRequest(DatabaseTable.USER, TEST_USER_ID);
		req.setId(TEST_USER_ID);
		FetchDataResult<User> result = service.fetch(req);
		assertNotNull(result);
		assertNull(result.getFailureReason());
		assertNotNull(result.getData());
		assertEquals(1, result.getData().size());
		assertEquals(keyObject, result.getData().get(0));
	}
	
	@Test
	void testFetch_findOnePersonByID() throws DataAccessException {
		Person keyObject = setUpPerson(TEST_PERSON_ID);
		FetchDataRequest req = new FetchDataRequest(DatabaseTable.PERSON, TEST_USER_ID);
		req.setId(TEST_PERSON_ID);
		FetchDataResult<Person> result = service.fetch(req);
		assertNotNull(result);
		assertNull(result.getFailureReason());
		assertNotNull(result.getData());
		assertEquals(1, result.getData().size());
		assertEquals(keyObject, result.getData().get(0));
	}
	
	@Test
	void testFetch_findOneEventByID() throws DataAccessException {
		Event keyObject = setUpEvent(TEST_EVENT_ID);
		FetchDataRequest req = new FetchDataRequest(DatabaseTable.EVENT, TEST_USER_ID);
		req.setId(TEST_EVENT_ID);
		FetchDataResult<Event> result = service.fetch(req);
		assertNotNull(result);
		assertNull(result.getFailureReason());
		assertNotNull(result.getData());
		assertEquals(1, result.getData().size());
		assertEquals(keyObject, result.getData().get(0));
	}
	
	@Test
	void testFetch_findOneAuthTokenByID() throws DataAccessException {
		AuthToken keyObject = setUpAuthToken(TEST_TOKEN_ID);
		FetchDataRequest req = new FetchDataRequest(DatabaseTable.AUTH_TOKEN, TEST_USER_ID);
		req.setId(TEST_TOKEN_ID);
		FetchDataResult<AuthToken> result = service.fetch(req);
		assertNotNull(result);
		assertNull(result.getFailureReason());
		assertNotNull(result.getData());
		assertEquals(1, result.getData().size());
		assertEquals(keyObject, result.getData().get(0));
	}
	
}
