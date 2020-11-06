package services;

import dao.DatabaseTable;
import database.DataAccessException;
import database.Database;
import requests.LoginRequest;
import requests.RegisterRequest;
import model.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {
	private Database<DatabaseTable> db;
	private LoginService service;
	private final String testUsername = "test_user";
	private final String testPassword = "Pa$$w0rd";
	
	@BeforeEach
	void setUp() throws DataAccessException {
		db = new Database<>(Database.TEST_DATABASE_NAME, DatabaseTable.values());
		service = new LoginService(db);
		db.clearTables();
	}
	
	private void registerTestUser() throws DataAccessException, IOException {
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
	void testLogin_failsWithWrongPassword() throws DataAccessException, IOException {
		registerTestUser();
		LoginRequest req = new LoginRequest(testUsername, "this should fail");
		LoginResult res = service.login(req);
		assertNull(res.getToken());
		assertNotNull(res.getFailureReason());
		assertEquals(LoginFailureReason.INCORRECT_PASSWORD, res.getFailureReason());
	}
	
	@Test
	void testLogin_succeedsWithCorrectCredentials() throws DataAccessException, IOException {
		registerTestUser();
		LoginRequest req = new LoginRequest(testUsername, testPassword);
		LoginResult res = service.login(req);
		assertNull(res.getFailureReason());
		assertNotNull(res.getToken());
		assertEquals(testUsername, res.getToken().getAssociatedUsername());
	}
}
