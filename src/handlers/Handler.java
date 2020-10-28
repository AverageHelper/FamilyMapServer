package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.AuthTokenDao;
import dao.DataAccessException;
import dao.Database;
import model.AuthToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A class that extends this class has access to the main database.
 */
public abstract class Handler<Response extends HTTPSerialization> implements HttpHandler {
	protected final @NotNull Database database;
	
	public Handler() {
		this(new Database());
	}
	
	public Handler(@NotNull Database database) {
		this.database = database;
	}
	
	
	
	
	
	/**
	 * Called by the default handler to determine the expected HTTP method.
	 * @return The expected HTTP method, or <code>null</code> if any method is acceptable.
	 */
	public abstract @Nullable String expectedHTTPMethod();
	
	
	
	/**
	 * Called by the default handler to determine whether requesters' auth token should be
	 * validated against the database.
	 * @return A value that indicates whether requesters' auth token should be validated against the database.
	 */
	public abstract boolean requiresAuthToken();
	
	
	
	/**
	 * Called by the default handler after appropriate checks have completed to indicate that
	 * the requester may perform the handler's primary operation.
	 *
	 *
	 * @param path The request's path string.
	 * @param userName The ID of the signed-in user, or <code>null</code> if the user is not signed in.
	 * @param req The JSON payload of the request.
	 * @throws DataAccessException An exception if there was a problem accessing the database.
	 */
	public abstract @NotNull Response run(@NotNull String path, @Nullable String userName, @NotNull String req) throws DataAccessException, HandlingFailureException, IOException;
	
	
	
	
	
	/**
	 * Checks the database for a user that matches the given auth token.
	 *
	 * @param authToken The token to check.
	 * @return The username associated with the auth token if the token is not <code>null</code> and is valid.
	 */
	private @Nullable String usernameForAuthToken(
		@Nullable String authToken
	) throws DataAccessException {
		if (authToken == null) {
			return null;
		}
		AtomicReference<AuthToken> token = new AtomicReference<>(null);
		database.runTransaction(conn -> {
			AuthTokenDao dao = new AuthTokenDao(conn);
			token.set(dao.find(authToken));
			return false;
		});
		if (token.get() != null && token.get().isValid()) {
			return token.get().getAssociatedUsername();
		}
		return null;
	}
	
	
	
	
	
	/**
	 * Closes the given <code>exchange</code> with the given response payload.
	 *
	 * @param exchange The HTTP exchange.
	 * @param res The response object to send.
	 * @param code The HTTP response code to send. Defaults to <code>OK</code> (200).
	 * @throws IOException An exception if there is an error sending the response.
	 */
	private <T extends HTTPSerialization> void closeWithResponse(
		@NotNull HttpExchange exchange,
		@NotNull T res,
		int code
	) throws IOException {
		String payload = res.serialize();
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type", res.contentType());
		exchange.sendResponseHeaders(code, payload.length());
		
		exchange.getResponseBody().write(payload.getBytes(StandardCharsets.UTF_8));
		exchange.getResponseBody().close();
	}
	
	
	
	
	
	
	/**
	 * Closes the given <code>exchange</code> with the given response object and a 200 (OK) code.
	 *
	 * @param exchange The HTTP exchange.
	 * @param res The response object to send.
	 * @throws IOException An exception if there is an error encoding the JSON payload or sending the response.
	 */
	private <T extends HTTPSerialization> void closeWithResponse(
		@NotNull HttpExchange exchange,
		@NotNull T res
	) throws IOException {
		this.closeWithResponse(exchange, res, HttpURLConnection.HTTP_OK);
	}
	
	
	
	
	
	/**
	 * Closes the given <code>exchange</code> with the given error code and optional message.
	 * @param exchange The HTTP exchange.
	 * @param code The HTTP response code to send.
	 * @param message The message to send. Defaults to <code>"Internal server error"</code>.
	 * @throws IOException An exception if there is an error encoding the JSON payload or sending the response.
	 */
	private void closeWithError(
		@NotNull HttpExchange exchange,
		int code,
		@NotNull String message
	) throws IOException {
		ErrorResponse error = new ErrorResponse(message);
		this.closeWithResponse(exchange, error, code);
	}
	
	
	
	
	
	
	/**
	 * Closes the given <code>exchange</code> with the given error code.
	 * @param exchange The HTTP exchange.
	 * @throws IOException An exception if there is an error encoding the JSON payload or sending the response.
	 */
	private void closeWithInternalError(@NotNull HttpExchange exchange) throws IOException {
		this.closeWithError(
			exchange,
			HttpURLConnection.HTTP_INTERNAL_ERROR,
			"Internal server error"
		);
	}
	
	
	
	
	
	/**
	 * Gets the request body as a string.
	 *
	 * @param exchange The HTTP exchange to query.
	 * @return The request body.
	 * @throws IOException An exception if there was a problem reading from the response body.
	 */
	private @NotNull String getContent(@NotNull HttpExchange exchange) throws IOException {
		BufferedReader httpInput = new BufferedReader(new InputStreamReader(
			exchange.getRequestBody(),
			StandardCharsets.UTF_8
		));
		StringBuilder in = new StringBuilder();
		String input;
		while ((input = httpInput.readLine()) != null) {
			in.append(input).append("\n");
		}
		httpInput.close();
		return in.toString().trim();
	}
	
	
	
	
	
	@Override
	public final void handle(HttpExchange exchange) throws IOException {
		// Construct and return the HTTP response
		
		try { // catch IO errors
			Response resp;
			
			try { // catch Database errors
				
				// If we've got the right method...
				String expectedMethod = this.expectedHTTPMethod();
				if (expectedMethod != null) {
					String expectation = expectedMethod.toLowerCase();
					String reality = exchange.getRequestMethod().toLowerCase();
					if (!reality.equals(expectation)) {
						this.closeWithError(exchange, HttpURLConnection.HTTP_BAD_METHOD,
							"Expected the " +
								expectation.toUpperCase() +
								" method. Got " +
								reality.toUpperCase()
						);
						return;
					}
				}
				
				String path = exchange.getRequestURI().getPath();
				String json = getContent(exchange);
				
				if (requiresAuthToken()) {
					// Check that the requester is signed in.
					
					Headers reqHeaders = exchange.getRequestHeaders();
					final String HEADER_KEY_AUTHORIZATION = "Authorization";
					
					// Ensure "Authorization" header is present
					if (!reqHeaders.containsKey(HEADER_KEY_AUTHORIZATION)) {
						this.closeWithError(exchange, HttpURLConnection.HTTP_UNAUTHORIZED,
							"You are not permitted to access this resource."
						);
						return;
					}
					
					// Extract the provided auth token
					String authToken = reqHeaders.getFirst(HEADER_KEY_AUTHORIZATION);
					String userName = usernameForAuthToken(authToken);
					if (userName == null) {
						this.closeWithError(exchange, HttpURLConnection.HTTP_UNAUTHORIZED,
							"You are not permitted to access this resource."
						);
						return;
					}
					
					// Got a valid token. Do the thing!
					resp = this.run(path, userName, json);
				} else {
					// No token needed. Do the thing!
					resp = this.run(path, null, json);
				}
				
			} catch (DataAccessException e) {
				Server.logger.severe(e.getMessage());
				e.printStackTrace();
				this.closeWithInternalError(exchange);
				return;
				
			} catch (HandlingFailureException e) {
				Server.logger.severe(e.getMessage());
				this.closeWithError(exchange, HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage());
				return;
			}
			
			// Respond with success
			this.closeWithResponse(exchange, resp);
		} catch (IOException e) {
			Server.logger.severe(e.getMessage());
			e.printStackTrace();
			this.closeWithInternalError(exchange);
		}
	}
	
}
