package services;

import dao.*;
import handlers.RegisterRequest;
import model.AuthToken;
import model.Person;
import model.User;
import org.jetbrains.annotations.NotNull;
import org.sqlite.SQLiteErrorCode;
import utilities.NameGenerator;

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
	public @NotNull RegisterResult register(RegisterRequest request) throws DataAccessException {
		Person newPerson = new Person(
			NameGenerator.newObjectIdentifier(),
			request.getUserName(),
			request.getFirstName(),
			request.getLastName(),
			request.getGender(),
			null,
			null,
			null
		);
		User newUser = new User(
			request.getUserName(),
			request.getPassword(),
			request.getEmail(),
			request.getFirstName(),
			request.getLastName(),
			request.getGender(),
			newPerson.getId()
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
				PersonDao personDao = new PersonDao(conn);
				AuthTokenDao authTokenDao = new AuthTokenDao(conn);
				
				userDao.insert(newUser);
				personDao.insert(newPerson);
				authTokenDao.insert(newToken);
				
				// TODO: Generate 4 generations of ancestor data for this person
				
				result.set(new RegisterResult(newToken, newPerson.getId()));
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
