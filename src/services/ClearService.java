package services;

/**
 * An object that serves a single database-clear request.
 */
public class ClearService {
	/**
	 * Attempts to clear the database.
	 * @return A new <code>ClearResult</code> that describes the result of the operation.
	 */
	public ClearResult clear() {
		return new ClearResult(ClearFailureReason.UNIMPLEMENTED);
	}
}
