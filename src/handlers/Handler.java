package handlers;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.AuthTokenDao;
import dao.DataAccessException;
import dao.Database;
import dao.DatabaseTable;
import model.AuthToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import server.Server;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A type that extends this class handles HTTP responses.
 *
 * @param <Response> The type of serializable response that this handler can return to callers.
 */
public abstract class Handler<Response extends HTTPSerialization> implements HttpHandler {
	protected final @NotNull Database<DatabaseTable> database;
	
	public Handler() {
		this(new Database<>(DatabaseTable.values()));
	}
	
	public Handler(@NotNull Database<DatabaseTable> database) {
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
	 * @return The serializable response that should be sent back to the client.
	 * @throws DataAccessException An exception if there was a problem accessing the database.
	 * @throws HandlingFailureException An exception if there was a problem handling the request.
	 * @throws IOException An exception if there was a problem accessing the local disk.
	 */
	public abstract @NotNull Response run(@NotNull String path, @Nullable String userName, @NotNull String req) throws DataAccessException, HandlingFailureException, IOException;
	
	
	
	
	
	
	
	/**
	 * Tries to parse an object of a given type from a JSON string.
	 *
	 * @param json The JSON payload to parse.
	 * @param typeOfT The type of object to parse. Must implement <code>JSONSerialization</code>.
	 * @param <T> The type of object to parse.
	 * @return The fully-realized object.
	 * @throws HandlingFailureException An exception if there was a problem parsing the JSON.
	 */
	public <T extends JSONSerialization> @NotNull T parseJSON(
		@NotNull String json,
		@NotNull Class<T> typeOfT
	) throws HandlingFailureException {
		try {
			return JSONSerialization.fromJson(json, typeOfT);
		} catch (JsonParseException e) {
			throw new HandlingFailureException(e);
		} catch (MissingKeyException e) {
			throw new HandlingFailureException(HandlingFailureReason.BAD_INPUT, e);
		}
	}
	
	
	
	
	
	/**
	 * Checks that the given <code>value</code> is not <code>null</code> or an empty string. Throws an exception if it is, and returns the value otherwise.
	 *
	 * @param key The name of the key where the value is stored (for error reporting).
	 * @param value The value to check.
	 * @return The given value if it is neither <code>null</code> nor an empty string.
	 * @throws HandlingFailureException An exception if the value is <code>null</code> or empty.
	 */
	public @NotNull String notEmptyValue(@NotNull String key, @Nullable String value) throws HandlingFailureException {
		if (value == null || value.isEmpty()) {
			EmptyValueException e = new EmptyValueException(key);
			throw new HandlingFailureException(HandlingFailureReason.BAD_INPUT, e);
		}
		return value;
	}
	
	
	
	
	
	/**
	 * Checks the database for a user that matches the given auth token.
	 *
	 * @param authToken The token to check.
	 * @throws DataAccessException An exception if there was a problem accessing the database.
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
	 * @param <T> The type of object to be serialized into a transportable payload.
	 * @throws IOException An exception if there is an error sending the response.
	 */
	private <T extends HTTPSerialization> void closeWithResponse(
		@NotNull HttpExchange exchange,
		@NotNull T res,
		int code
	) throws IOException {
		String payload = res.serialize();
		Server.logger.fine("Closing with code " + code + ": " + payload);
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type", res.contentType() + "; charset=UTF-8");
		exchange.sendResponseHeaders(code, 0);
		
		OutputStreamWriter sw = new OutputStreamWriter(exchange.getResponseBody());
		sw.write(payload);
		sw.flush();
		exchange.getResponseBody().close();
	}
	
	
	
	
	
	
	/**
	 * Closes the given <code>exchange</code> with the given response object and a 200 (OK) code.
	 *
	 * @param exchange The HTTP exchange.
	 * @param res The response object to send.
	 * @param <T> The type of object to be serialized into a transportable payload.
	 * @throws IOException An exception if there is an error encoding the JSON payload or sending the response.
	 */
	private <T extends HTTPSerialization> void closeWithResponse(
		@NotNull HttpExchange exchange,
		@NotNull T res
	) throws IOException {
		this.closeWithResponse(exchange, res, res.httpResultCode());
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
				
				Server.logger.fine("[" + this.getClass().getName() + "] " +
					"Handling " + exchange.getRequestMethod().toUpperCase() + " request at path " +
					path + ": " +
					json
				);
				
				if (requiresAuthToken()) {
					// Check that the requester is signed in.
					
					Headers reqHeaders = exchange.getRequestHeaders();
					final String HEADER_KEY_AUTHORIZATION = "Authorization";
					
					// Ensure "Authorization" header is present
					if (!reqHeaders.containsKey(HEADER_KEY_AUTHORIZATION)) {
						this.closeWithError(exchange, HttpURLConnection.HTTP_BAD_REQUEST,
							"You must provide an auth token to access this resource."
						);
						return;
					}
					
					// Extract the provided auth token
					String authToken = reqHeaders.getFirst(HEADER_KEY_AUTHORIZATION);
					String userName = usernameForAuthToken(authToken);
					if (userName == null) {
						this.closeWithError(exchange, HttpURLConnection.HTTP_BAD_REQUEST,
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
				Server.logger.warning("DataAccessException: " + e.getMessage());
				e.printStackTrace();
				this.closeWithInternalError(exchange);
				return;
				
			} catch (HandlingFailureException e) {
				Server.logger.warning(
					"HandlingFailureException: " +
						e.getReason().name() +
						": " +
						e.getMessage()
				);
				this.closeWithError(exchange, HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage());
				return;
			}
			
			// Respond with success
			this.closeWithResponse(exchange, resp);
			
		} catch (Throwable e) {
			Server.logger.severe(e.getMessage());
			e.printStackTrace();
			this.closeWithInternalError(exchange);
		}
	}
	
}
