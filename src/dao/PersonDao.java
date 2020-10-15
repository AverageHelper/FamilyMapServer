package dao;

import model.Person;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;

/**
 * An object that manages the reading and writing of <code>Person</code> records in the database.
 */
public class PersonDao extends Dao<Person> {
	/**
	 * Creates a <code>PersonDao</code> object.
	 *
	 * @param connection The database connection to use to perform access-related tasks.
	 */
	public PersonDao(Connection connection) {
		super(connection);
	}
	
	@Override
	protected @NotNull DatabaseTable table() {
		return DatabaseTable.PERSON;
	}
	
	@Override
	public void insert(@NotNull Person record) throws DataAccessException {
	
	}
	
	@Override
	public @Nullable Person find(@NotNull String id) throws DataAccessException {
		return null;
	}
}
