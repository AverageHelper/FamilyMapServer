package services;

import dao.AuthTokenDao;
import dao.DataAccessException;
import dao.Database;
import handlers.RegisterRequest;
import model.AuthToken;
import model.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.NameGenerator;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {
	private Database db;
	private RegisterService service;
	private final String testUsername = "test_user";
	
	@BeforeEach
	void setUp() throws DataAccessException {
		db = new Database(Database.TEST_DATABASE_NAME);
		service = new RegisterService(db);
		db.clearTables();
	}
	
	@Test
	void testRegister_succeedsWithNewUser() throws DataAccessException {
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
		
		db.runTransaction(conn -> {
			AuthTokenDao authTokenDao = new AuthTokenDao(conn);
			assertEquals(
				1,
				authTokenDao.count(),
				"No auth tokens were added to the database."
			);
			return false;
		});
	}
	
	@Test
	void testRegister_failsWithDuplicateUsername() throws DataAccessException {
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
		
		db.runTransaction(conn -> {
			AuthTokenDao authTokenDao = new AuthTokenDao(conn);
			assertEquals(
				1,
				authTokenDao.count(),
				"A token had been added to the database, but should not have been."
			);
			return false;
		});
	}
}