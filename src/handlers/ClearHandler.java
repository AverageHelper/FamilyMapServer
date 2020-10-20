package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import dao.DataAccessException;
import dao.Database;
import org.jetbrains.annotations.NotNull;
import services.ClearFailureException;
import services.ClearFailureReason;
import services.ClearResult;
import services.ClearService;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * An object that handles multiple clear-database handlers.
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
					Headers reqHeaders = exchange.getRequestHeaders();
					
					// Ensure "Authorization" header is present
					final String HEADER_KEY_AUTHORIZATION = "Authorization";
					if (reqHeaders.containsKey(HEADER_KEY_AUTHORIZATION)) {
						// Extract the provided auth token
						String authToken = reqHeaders.getFirst(HEADER_KEY_AUTHORIZATION);
						
						// Ensure the token is valid
						if (!authTokenIsValid(authToken)) {
							throw new ClearFailureException(ClearFailureReason.UNAUTHENTICATED);
						}
						
						this.clear();
						success = true;
					}
				}
			} catch (ClearFailureException e) {
				// handle normal failures
				switch (e.getReason()) {
					case UNIMPLEMENTED:
						exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAVAILABLE, 0);
						
					case UNAUTHENTICATED:
						exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
						
					case DATABASE:
						exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
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
	 * @throws ClearFailureException If the operation fails.
	 */
	public void clear() throws ClearFailureException {
		ClearService service = new ClearService();
		ClearResult result = service.clear(database);
		
		if (result.getFailureReason() != null) {
			throw new ClearFailureException(result.getFailureReason());
		}
	}
}
