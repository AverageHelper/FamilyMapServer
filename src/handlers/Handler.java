package handlers;

import com.sun.net.httpserver.HttpHandler;
import dao.AuthTokenDao;
import dao.DataAccessException;
import dao.Database;
import model.AuthToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A class that extends this class has access to the main database.
 */
public abstract class Handler implements HttpHandler {
	
	protected final @NotNull Database database;
	
	public Handler() {
		this(new Database());
	}
	
	public Handler(@NotNull Database database) {
		this.database = database;
	}
	
	/**
	 * Gets the active database connection for the instance. Creates a new one if no connection has
	 * yet been opened.
	 *
	 * @return An active database connection.
	 * @throws DataAccessException An exception if there was an issue opening a connection.
	 */
	protected @NotNull Connection getDatabaseConnection() throws DataAccessException {
		return this.database.getConnection();
	}
	
	/**
	 * Closes the database connection, optionally and writing changes to desk.
	 *
	 * @param commit Whether this connection's changes should be written to disk.
	 * @throws DataAccessException An exception if there was an issue closing the connection.
	 */
	protected void closeConnection(boolean commit) throws DataAccessException {
		this.database.closeConnection(commit);
	}
	
	/**
	 * Closes the database connection and writes changes to desk.
	 *
	 * @throws DataAccessException An exception if there was an issue closing the connection.
	 */
	protected void commitChanges() throws DataAccessException {
		this.closeConnection(true);
	}
	
	/**
	 * Closes the database connection without writing changes to desk.
	 *
	 * @throws DataAccessException An exception if there was an issue closing the connection.
	 */
	protected void closeWithoutSaving() throws DataAccessException {
		this.closeConnection(false);
	}
	
	/**
	 * Checks the database for a user that matches the given auth token.
	 *
	 * @param authToken The token to check.
	 * @return The username associated with the auth token if the token is not <code>null</code> and is valid.
	 */
	public @Nullable String usernameForAuthToken(@Nullable String authToken) throws DataAccessException {
		if (authToken == null) {
			return null;
		}
		AtomicReference<AuthToken> token = new AtomicReference<>(null);
		database.runTransaction(conn -> {
			AuthTokenDao dao = new AuthTokenDao(getDatabaseConnection());
			token.set(dao.find(authToken));
			return false;
		});
		if (token.get() != null && token.get().isValid()) {
			return token.get().getAssociatedUsername();
		}
		return null;
	}
	
	/**
	 * Checks the database for the given auth token.
	 *
	 * @param authToken The token to check.
	 * @return <code>true</code> if the given auth token is non-<code>null</code>, exists in the database, and is a valid login token.
	 */
	public boolean authTokenIsValid(@Nullable String authToken) throws DataAccessException {
		return usernameForAuthToken(authToken) != null;
	}
	
}
