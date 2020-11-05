package services;

import dao.DatabaseTable;
import database.DataAccessException;
import database.Database;
import org.jetbrains.annotations.NotNull;

/**
 * An object that serves a single database-clear request.
 */
public class ClearService {
	private final Database<DatabaseTable> db;
	
	public ClearService(@NotNull Database<DatabaseTable> database) {
		this.db = database;
	}
	
	/**
	 * Attempts to clear the database.
	 *
	 * @return A new <code>ClearResult</code> that describes the result of the operation.
	 * @throws DataAccessException An exception if there was a problem accessing the database.
	 */
	public boolean clear() throws DataAccessException {
		db.clearTables();
		return true;
	}
}
