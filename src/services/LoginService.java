package services;

import dao.AuthTokenDao;
import dao.DataAccessException;
import dao.Database;
import dao.UserDao;
import handlers.LoginRequest;
import model.AuthToken;
import model.User;
import org.jetbrains.annotations.NotNull;
import utilities.NameGenerator;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An object that serves a single user login request.
 */
public class LoginService {
	private final @NotNull Database db;
	
	public LoginService(@NotNull Database database) {
		this.db = database;
	}
	
	/**
	 * Performs a login request.
	 *
	 * @param request The login request.
	 * @throws DataAccessException An exception if there was a problem accessing the database.
	 * @return The result of the login operation.
	 */
	public @NotNull LoginResult login(LoginRequest request) throws DataAccessException {
		AtomicReference<LoginResult> result = new AtomicReference<>();
		
		db.runTransaction(conn -> {
			UserDao userDao = new UserDao(conn);
			AuthTokenDao authTokenDao = new AuthTokenDao(conn);
			
			User user = userDao.find(request.getUserName());
			if (user == null) {
				result.set(new LoginResult(LoginFailureReason.USER_NOT_FOUND));
				return false;
			}
			
			if (!user.getPassword().equals(request.getPassword())) {
				result.set(new LoginResult(LoginFailureReason.INCORRECT_PASSWORD));
				return false;
			}
			
			AuthToken newToken = new AuthToken(
				NameGenerator.newObjectIdentifier(),
				user.getId(),
				new Date(),
				true
			);
			authTokenDao.insert(newToken);
			result.set(new LoginResult(newToken, user.getPersonID()));
			return true;
		});
		
		return result.get();
	}
}
