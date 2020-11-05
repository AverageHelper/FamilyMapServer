package services;

import dao.AuthTokenDao;
import dao.DatabaseTable;
import database.DataAccessException;
import database.Database;
import handlers.RegisterRequest;
import model.AuthToken;
import model.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.NameGenerator;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {
	private Database<DatabaseTable> db;
	private RegisterService service;
	private final String testUsername = "test_user";
	
	@BeforeEach
	void setUp() throws DataAccessException {
		db = new Database<>(Database.TEST_DATABASE_NAME, DatabaseTable.values());
		service = new RegisterService(db);
		db.clearTables();
	}
	
	private int getNumberOfAuthTokens() throws DataAccessException {
		AtomicInteger count = new AtomicInteger(0);
		
		db.runTransaction(conn -> {
			AuthTokenDao authTokenDao = new AuthTokenDao(conn);
			count.set(authTokenDao.count());
			return false;
		});
		
		return count.get();
	}
	
	@Test
	void testRegister_succeedsWithNewUser() throws DataAccessException, IOException {
		RegisterRequest req = new RegisterRequest(
			testUsername,
			"Pa$$w0rd",
			"test@example.com",
			"Test",
			"Person",
			Gender.MALE
		);
		RegisterResult res = service.register(req);
		assertNull(res.getFailureReason());
		assertNotNull(res.getToken());
		
		AuthToken token = res.getToken();
		assertEquals(testUsername, token.getAssociatedUsername());
		assertTrue(token.isValid());
		assertNotNull(token.getCreatedAt());
		assertNotNull(token.getId());
		assertEquals(NameGenerator.OBJECT_ID_LENGTH, token.getId().length());
		
		int count = getNumberOfAuthTokens();
		assertEquals(
			1, count,
			"No auth tokens were added to the database."
		);
	}
	
	@Test
	void testRegister_failsWithDuplicateUsername() throws DataAccessException, IOException {
		RegisterRequest req = new RegisterRequest(
			testUsername,
			"Pa$$w0rd",
			"test@example.com",
			"Test",
			"Person",
			Gender.MALE
		);
		RegisterResult res = service.register(req);
		assertNull(res.getFailureReason());
		
		res = service.register(req);
		assertNull(res.getToken());
		assertEquals(RegisterFailureReason.DUPLICATE_USERNAME, res.getFailureReason());
		
		int count = getNumberOfAuthTokens();
		assertEquals(
			1, count,
			"A token had been added to the database, but should not have been."
		);
	}
}
