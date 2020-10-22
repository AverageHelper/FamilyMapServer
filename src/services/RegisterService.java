package services;

import dao.AuthTokenDao;
import dao.DataAccessException;
import dao.Database;
import dao.UserDao;
import handlers.RegisterRequest;
import model.AuthToken;
import model.User;
import org.jetbrains.annotations.NotNull;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;
import server.Server;

import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An object that serves a single user registration request.
 */
public class RegisterService {
	private final Database db;
	
	public RegisterService(@NotNull Database database) {
		this.db = database;
	}
	
	/**
	 * Registers a new user.
	 * @param request Information about the new user's account.
	 * @return The result of the request.
	 */
	public @NotNull RegisterResult register(RegisterRequest request) throws DataAccessException {
		User newUser = new User(
			request.getUsername(),
			request.getPassword(),
			request.getEmail(),
			request.getFirstName(),
			request.getLastName(),
			request.getGender(),
			null
		);
		AuthToken newToken = new AuthToken(
			Server.newObjectIdentifier(),
			request.getUsername(),
			new Date(),
			true
		);
		
		AtomicReference<RegisterResult> result = new AtomicReference<>(null);
		
		try {
			db.runTransaction(conn -> {
				UserDao userDao = new UserDao(conn);
				AuthTokenDao authTokenDao = new AuthTokenDao(conn);
				
				userDao.insert(newUser);
				authTokenDao.insert(newToken);
				
				result.set(new RegisterResult(newToken));
				return true;
			});
		} catch (DataAccessException e) {
			SQLiteErrorCode code = e.getErrorCode();
			String message = e.getMessage();
			if (code != SQLiteErrorCode.SQLITE_CONSTRAINT || !message.contains("User.username")) {
				throw e;
			}
			// Duplicate username
			result.set(new RegisterResult(RegisterFailureReason.DUPLICATE_USERNAME));
		}
		
		return result.get();
	}
}
