package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import dao.DataAccessException;
import dao.Database;
import org.jetbrains.annotations.NotNull;
import services.ClearService;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * An object that handles clear-database requests.
 */
public class ClearHandler extends Handler {
	
	public ClearHandler() {
		super();
	}
	
	public ClearHandler(@NotNull Database database) {
		super(database);
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// Construct and return the HTTP response
		boolean success = false;
		
		try {
			try {
				// Determine the request type (GET/POST)
				
				// Only allow POST requests for this operation
				if (exchange.getRequestMethod().toLowerCase().equals("post")) {
					// Get the HTTP request headers
//					Headers reqHeaders = exchange.getRequestHeaders();
					
					// Ensure "Authorization" header is present
//					final String HEADER_KEY_AUTHORIZATION = "Authorization";
//					if (reqHeaders.containsKey(HEADER_KEY_AUTHORIZATION)) {
					// Extract the provided auth token
//						String authToken = reqHeaders.getFirst(HEADER_KEY_AUTHORIZATION);
					
					// Ensure the token is valid
//						if (!authTokenIsValid(authToken)) {
//							throw new ClearFailureException(ClearFailureReason.UNAUTHENTICATED);
//						}
					
					this.clear();
					success=true;
//					}
				}
			} catch (DataAccessException e) {
				e.printStackTrace();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
			}
			
			// We made it?
			if (success) {
				// We made it!
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			}
		} catch (IOException exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
		}
		
		exchange.getResponseBody().close();
	}
	
	/**
	 * Attempts to clear the database.
	 * @throws DataAccessException If the operation fails.
	 */
	public void clear() throws DataAccessException {
		ClearService service = new ClearService(database);
		service.clear();
	}
}
