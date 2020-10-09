package dao;

import model.Person;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

/**
 * An object that manages the reading and writing of Person data in the database.
 */
public class PersonDao {
	private final Connection connection;
	private static @NotNull TableName tableName() {
		return TableName.PERSON;
	}
	
	/**
	 * Creates a <code>PersonDao</code> object.
	 * @param connection The database connection to use to perform access-related tasks.
	 */
	public PersonDao(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Attempts to read from the database a Person record with the given <code>id</code>.
	 *
	 * @param id The person's unique identifier.
	 * @return A fully realized <code>Person</code> object if the user was found.
	 * @throws DataAccessException An exception if the read fails.
	 */
	static @NotNull Person readWithId(@NotNull String id) throws DataAccessException {
		return null;
	}
	
	/**
	 * Attempts to write the person record to the database.
	 *
	 * @param person The person data to write.
	 * @throws Exception An exception if the write fails.
	 */
	static void write(@NotNull Person person) throws Exception {
	
	}
	
	/**
	 * Attempts to remove from the database a given Person object.
	 *
	 * @param person The person to delete.
	 * @throws Exception An exception if the write fails.
	 */
	static void delete(@NotNull Person person) throws Exception {
	
	}
}
