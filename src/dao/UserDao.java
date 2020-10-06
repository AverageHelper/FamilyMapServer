package dao;

import model.User;
import org.jetbrains.annotations.NotNull;

/**
 * An object that manages the reading and writing of User data in the database.
 */
public class UserDao {
	private static @NotNull TableName tableName() {
		return TableName.USER;
	}
	
	/**
	 * Attempts to read from the database a User record with the given <code>id</code>.
	 *
	 * @param id The user's unique identifier.
	 * @return A fully realized <code>User</code> object if the user was found.
	 * @throws Exception An exception if the read fails.
	 */
	static @NotNull User readWithId(@NotNull String id) throws Exception {
		return null;
	}
	
	/**
	 * Attempts to write the user record to the database.
	 *
	 * @param user The user data to write.
	 * @throws Exception An exception if the write fails.
	 */
	static void write(@NotNull User user) throws Exception {
	
	}
	
	/**
	 * Attempts to remove from the database a given User object.
	 *
	 * @param user The user to delete.
	 * @throws Exception An exception if the write fails.
	 */
	static void delete(@NotNull User user) throws Exception {
	
	}
}
