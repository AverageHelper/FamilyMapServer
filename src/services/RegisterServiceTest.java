package services;

import dao.AuthTokenDao;
import dao.DataAccessException;
import dao.Database;
import handlers.RegisterRequest;
import model.AuthToken;
import model.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sqlite.SQLiteErrorCode;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {
	
	Database db;
	RegisterService service;
	final String testUsername = "test_user";
	
	@BeforeEach
	void setUp() throws DataAccessException {
		db = new Database(Database.TEST_DATABASE_NAME);
		service = new RegisterService(db);
		db.clearTables();
	}
	
	@Test
	void testRegister_newUser() throws DataAccessException {
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
		assertEquals(token.getAssociatedUsername(), testUsername);
		assertTrue(token.isValid());
		assertNotNull(token.getCreatedAt());
		assertNotNull(token.getId());
		assertEquals(token.getId().length(), Server.OBJECT_ID_LENGTH);
		
		db.runTransaction(conn -> {
			AuthTokenDao authTokenDao = new AuthTokenDao(conn);
			assertEquals(
				authTokenDao.count(),
				1,
				"No auth tokens were added to the database."
			);
			return false;
		});
	}
	
	@Test
	void testRegister_duplicateUsername() throws DataAccessException {
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
		assertEquals(res.getFailureReason(), RegisterFailureReason.DUPLICATE_USERNAME);
		
		db.runTransaction(conn -> {
			AuthTokenDao authTokenDao = new AuthTokenDao(conn);
			assertEquals(
				authTokenDao.count(),
				1,
				"A token had been added to the database, but should not have been."
			);
			return false;
		});
	}
}