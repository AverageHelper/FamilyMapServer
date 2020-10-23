package services;

import dao.DataAccessException;
import dao.Database;
import org.jetbrains.annotations.NotNull;

/**
 * An object that serves a single database-clear request.
 */
public class ClearService {
	private final Database db;
	
	public ClearService(@NotNull Database database) {
		this.db = database;
	}
	
	/**
	 * Attempts to clear the database.
	 *
	 * @return A new <code>ClearResult</code> that describes the result of the operation.
	 */
	public boolean clear() throws DataAccessException {
		db.clearTables();
		return true;
	}
}
