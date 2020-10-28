package services;

import dao.DataAccessException;
import dao.Database;
import handlers.LoginRequest;
import handlers.RegisterRequest;
import model.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {
	private Database db;
	private LoginService service;
	private final String testUsername = "test_user";
	private final String testPassword = "Pa$$w0rd";
	
	@BeforeEach
	void setUp() throws DataAccessException {
		db = new Database(Database.TEST_DATABASE_NAME);
		service = new LoginService(db);
		db.clearTables();
	}
	
	private void registerTestUser() throws DataAccessException {
		RegisterRequest req = new RegisterRequest(
			testUsername,
			testPassword,
			"test@example.com",
			"Test",
			"Person",
			Gender.MALE
		);
		RegisterService registerService = new RegisterService(db);
		registerService.register(req);
	}
	
	@Test
	void testLogin_failsWithUnknownUser() throws DataAccessException {
		LoginRequest req = new LoginRequest(testUsername, testPassword);
		LoginResult res = service.login(req);
		assertNull(res.getToken());
		assertNotNull(res.getFailureReason());
		assertEquals(LoginFailureReason.USER_NOT_FOUND, res.getFailureReason());
	}
	
	@Test
	void testLogin_failsWithWrongPassword() throws DataAccessException {
		registerTestUser();
		LoginRequest req = new LoginRequest(testUsername, "this should fail");
		LoginResult res = service.login(req);
		assertNull(res.getToken());
		assertNotNull(res.getFailureReason());
		assertEquals(LoginFailureReason.INCORRECT_PASSWORD, res.getFailureReason());
	}
	
	@Test
	void testLogin_succeedsWithCorrectCredentials() throws DataAccessException {
		registerTestUser();
		LoginRequest req = new LoginRequest(testUsername, testPassword);
		LoginResult res = service.login(req);
		assertNull(res.getFailureReason());
		assertNotNull(res.getToken());
		assertEquals(testUsername, res.getToken().getAssociatedUsername());
	}
}