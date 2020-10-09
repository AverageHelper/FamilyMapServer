package dao;

import model.AuthToken;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

/**
 * An object that manages the reading and writing of AuthToken data in the database.
 */
public class AuthTokenDao {
	private final Connection connection;
	private static @NotNull TableName tableName() {
		return TableName.AUTH_TOKEN;
	}
	
	/**
	 * Creates an <code>AuthTokenDao</code> object.
	 * @param connection The database connection to use to perform access-related tasks.
	 */
	public AuthTokenDao(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Attempts to read from the database an Auth Token with the given <code>id</code>.
	 *
	 * @param id The token's unique identifier.
	 * @return A fully realized <code>AuthToken</code> object if the user was found.
	 * @throws DataAccessException An exception if the read fails.
	 */
	static @NotNull AuthToken readWithId(@NotNull String id) throws DataAccessException {
		return null;
	}
	
	/**
	 * Attempts to write the auth token to the database.
	 *
	 * @param token The event data to write.
	 * @throws Exception An exception if the write fails.
	 */
	static void write(@NotNull AuthToken token) throws Exception {
	
	}
	
	/**
	 * Attempts to remove from the database a given Auth Token object.
	 *
	 * @param authToken The auth token to delete.
	 * @throws Exception An exception if the write fails.
	 */
	static void delete(@NotNull AuthToken authToken) throws Exception {
	
	}
}
