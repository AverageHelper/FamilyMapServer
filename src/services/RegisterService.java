package services;

import dao.*;
import handlers.RegisterRequest;
import model.AuthToken;
import model.User;
import org.jetbrains.annotations.NotNull;
import org.sqlite.SQLiteErrorCode;
import utilities.NameGenerator;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An object that serves a single user registration request.
 */
public class RegisterService {
	private final @NotNull Database db;
	
	public RegisterService(@NotNull Database database) {
		this.db = database;
	}
	
	/**
	 * Registers a new user.
	 *
	 * @param request Information about the new user's account.
	 * @throws DataAccessException An exception if there was a problem accessing the database.
	 * @return The result of the request.
	 */
	public @NotNull RegisterResult register(RegisterRequest request) throws DataAccessException, IOException {
		User newUser = new User(
			request.getUserName(),
			request.getPassword(),
			request.getEmail(),
			request.getFirstName(),
			request.getLastName(),
			request.getGender(),
			null
		);
		AuthToken newToken = new AuthToken(
			NameGenerator.newObjectIdentifier(),
			request.getUserName(),
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
				
				return true;
			});
			
			FillService fillService = new FillService(db);
			fillService.fill(request.getUserName(), 4);
			
			db.runTransaction(conn -> {
				UserDao userDao = new UserDao(conn);
				
				User user = userDao.find(request.getUserName());
				if (user != null && user.getPersonID() != null) {
					result.set(new RegisterResult(newToken, user.getPersonID()));
				} else {
					throw new DataAccessException(SQLiteErrorCode.SQLITE_NOTFOUND, "The user or its person was found null.");
				}
				
				return false;
			});
			
		} catch (DataAccessException e) {
			SQLiteErrorCode code = e.getErrorCode();
			String message = e.getMessage();
			if (code != SQLiteErrorCode.SQLITE_CONSTRAINT ||
				!message.contains("." + DatabaseTable.USER.getPrimaryKey())) {
				throw e;
			}
			// Duplicate username
			result.set(new RegisterResult(RegisterFailureReason.DUPLICATE_USERNAME));
		}
		
		return result.get();
	}
}
