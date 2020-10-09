package handlers;

import services.ClearFailureException;
import services.ClearResult;
import services.ClearService;

/**
 * An object that handles multiple clear-database handlers.
 */
public class ClearHandler {
	/**
	 * Attempts to clear the database.
	 * @throws ClearFailureException If the operation fails.
	 */
	public void clear() throws ClearFailureException {
		ClearService service = new ClearService();
		ClearResult result = service.clear();
		
		if (result.getFailureReason() != null) {
			throw new ClearFailureException(result.getFailureReason());
		}
	}
}
