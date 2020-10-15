package dao;

import model.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;

/**
 * Objects that extend this class manage the reading and writing of records in the database.
 */
public abstract class Dao<T extends ModelData> {
	protected @NotNull Connection connection;
	
	public Dao(@NotNull Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Attempts to add a new record to the database.
	 *
	 * @param record The data to write.
	 * @throws DataAccessException An exception if the write fails.
	 */
	public abstract void insert(@NotNull T record) throws DataAccessException;
	
	/**
	 * Attempts to fetch from the database a record with the given <code>id</code>.
	 *
	 * @param id The value of the record's primary key.
	 * @return A fully realized object of type <code>T</code>, or <code>null</code> if the record could not be found.
	 * @throws DataAccessException An exception if the read fails.
	 */
	public abstract @Nullable T find(@NotNull String id) throws DataAccessException;
	
	/**
	 * Attempts to delete from the database a record with the given <code>id</code>.
	 *
	 * @param id The value of the record's primary key.
	 * @throws DataAccessException An exception if the write fails.
	 */
	public abstract void delete(@NotNull String id) throws DataAccessException;
}
