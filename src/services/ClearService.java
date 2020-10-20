package services;

import dao.DataAccessException;
import dao.Database;
import org.jetbrains.annotations.NotNull;

/**
 * An object that serves a single database-clear request.
 */
public class ClearService {
	/**
	 * Attempts to clear the database.
	 *
	 * @param database The database whose tables to clear.
	 * @return A new <code>ClearResult</code> that describes the result of the operation.
	 */
	public ClearResult clear(@NotNull Database db) {
		try {
			db.getConnection();
			db.clearTables();
			db.closeConnection(true);
			return new ClearResult();
		} catch (DataAccessException e) {
			return new ClearResult(ClearFailureReason.DATABASE);
		}
	}
}
