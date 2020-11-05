package dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sqlite.SQLiteErrorCode;
import server.Server;
import utilities.FileHelpers;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;

/**
 * A proxy to create and manage low-level interactions with the database.
 */
public class Database<Table extends IDatabaseTable> {
	private static final String CREATE_TABLES_FILE = "CreateTables.txt";
	public static final String MAIN_DATABASE_NAME = "familymap.sqlite";
	public static final String TEST_DATABASE_NAME = "familymap_tests.sqlite";
	
	private final @NotNull String databaseName;
	private @Nullable Connection conn;
	private final Table[] databaseTables;
	
	public Database(Table[] databaseTables) {
		this(MAIN_DATABASE_NAME, databaseTables);
	}
	
	public Database(@NotNull String databaseName, Table[] databaseTables) {
		this.databaseName = databaseName;
		this.databaseTables = databaseTables;
		
		try (Connection conn = DriverManager.getConnection(this.databaseUrl())) {
			// Create tables if they don't exist
			String sql = FileHelpers.stringFromFile(new File(CREATE_TABLES_FILE).getAbsoluteFile());
			
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
		} catch (IOException e) {
			e.printStackTrace();
			Server.logger.log(Level.SEVERE, "Error while reading CreateTables file: " + e.getMessage(), e);
		} catch (SQLException e) {
			e.printStackTrace();
			Server.logger.log(Level.SEVERE, "Error while preparing database tables: " + e.getMessage(), e);
		}
	}
	
	private @NotNull String databaseUrl() {
		return "jdbc:sqlite:" + this.databaseName;
	}
	
	/**
	 * Opens a new database connection. If there is already an open connection, an exception is thrown.
	 *
	 * Whenever we want to make a change to our database we will have to open a connection and use
	 * Statements created by that connection to initiate transactions
	 *
	 * @return A new database connection to use to perform access-related tasks.
	 * @throws DataAccessException An exception if there was an issue opening a connection.
	 */
	public @NotNull Connection openConnection() throws DataAccessException {
		if (conn != null) {
			throw new DataAccessException(
				SQLiteErrorCode.SQLITE_BUSY,
				"There is already an active connection. Close it first."
			);
		}
		try {
			//The Structure for this Connection is driver:language:path
			//The path assumes you start in the root of your project unless given a non-relative path
			final String CONNECTION_URL = this.databaseUrl();
			
			// Open a database connection to the file given in the path
			conn = DriverManager.getConnection(CONNECTION_URL);
			
			// Start a transaction
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException(e, "Unable to open connection to database: " + e.getMessage());
		}
		
		return conn;
	}
	
	/**
	 * Gets and returns the active database connection, or creates one if there is no active connection yet.
	 * @return A database connection to use to perform access-related tasks.
	 * @throws DataAccessException An exception if there was an issue opening a connection.
	 */
	public @NotNull Connection getConnection() throws DataAccessException {
		if (conn == null) {
			return openConnection();
		} else {
			return conn;
		}
	}
	
	/**
	 * Closes the active database connection.
	 *
	 * When we are done manipulating the database it is important to close the connection. This
	 * will End the transaction and allow us to either commit our changes to the database or
	 * rollback any changes that were made before we encountered a potential error.
	 *
	 * IMPORTANT: IF YOU FAIL TO CLOSE A CONNECTION AND TRY TO REOPEN THE DATABASE THIS WILL
	 * CAUSE THE DATABASE TO LOCK. YOUR CODE MUST ALWAYS INCLUDE A CLOSURE OF THE DATABASE NO
	 * MATTER WHAT ERRORS OR PROBLEMS YOU ENCOUNTER.
	 *
	 * @param commit Whether changes made during this connection should be saved.
	 * @throws DataAccessException An exception if there was an issue closing the connection.
	 */
	public void closeConnection(boolean commit) throws DataAccessException {
		if (conn == null) {
			// No connection? We must already have been closed.
			return;
		}
		try {
			if (commit) {
				// Commit the changes to the database
				conn.commit();
			} else {
				// If we find out something went wrong, callers pass `false`.
				// Rollback any changes we made during this connection.
				conn.rollback();
			}
			
			conn.close();
			conn = null;
		} catch (SQLException e) {
			throw new DataAccessException(e, "Unable to close database connection: " + e.getMessage());
		}
	}
	
	/**
	 * Runs the given function in a discrete database connection. Return `true` in that expression
	 * to commit changes to the database, and `false` to close the connection without committing.
	 *
	 * If any exceptions are thrown during the transaction, then the connection is automatically
	 * closed without committing changes.
	 *
	 * @param transaction The database transaction. This runnable receives a database connection
	 *                    that is open for the duration of the runtime of
	 *                    <code>runTransaction</code>. Return <code>true</code> to commit changes
	 *                    to the database, or <code>false</code> to close the connection without
	 *                    saving.
	 *
	 * @throws DataAccessException An exception if a database error occurs in the transaction body.
	 */
	public void runTransaction(@NotNull DatabaseTransaction transaction) throws DataAccessException {
		Connection conn = this.openConnection();
		boolean commit = false;
		try {
			commit = transaction.run(conn);
		} finally {
			this.closeConnection(commit);
		}
	}
	
	private void _clearTables(@NotNull Connection conn) throws DataAccessException {
		for (Table table : databaseTables) {
			// It seems that PreparedStatement doesn't take table names.
			String sql = "DELETE FROM " + table.getName();
			
			try (Statement stmt = conn.createStatement()) {
				stmt.executeUpdate(sql);
				
			} catch (SQLException e) {
				throw new DataAccessException(
					e,
					"Error encountered while clearing table '" +
						table.getName() +
						"': "
						+ e.getMessage()
				);
			}
		}
	}
	
	/**
	 * Clears all database tables.
	 * @throws DataAccessException An exception if there was an issue clearing the database.
	 */
	public void clearTables() throws DataAccessException {
		runTransaction(conn -> {
			this._clearTables(conn);
			return true;
		});
	}
}

