package dao;

import org.jetbrains.annotations.Nullable;

import java.sql.*;

/**
 * A proxy to create and manage low-level interactions with the database.
 */
public class Database {
	private @Nullable Connection conn;
	
	/**
	 * Opens a new database connection. If there is already an open connection, an exception is thrown.
	 *
	 * Whenever we want to make a change to our database we will have to open a connection and use
	 * Statements created by that connection to initiate transactions
	 *
	 * @return A new database connection to use to perform access-related tasks.
	 * @throws DataAccessException An exception if there was an issue opening a connection.
	 */
	public Connection openConnection() throws DataAccessException {
		if (conn != null) {
			throw new DataAccessException("There is already an active connection. Close it first.");
		}
		try {
			//The Structure for this Connection is driver:language:path
			//The path assumes you start in the root of your project unless given a non-relative path
			final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";
			
			// Open a database connection to the file given in the path
			conn = DriverManager.getConnection(CONNECTION_URL);
			
			// Start a transaction
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("Unable to open connection to database: " + e.getMessage());
		}
		
		return conn;
	}
	
	/**
	 * Gets and returns the active database connection, or creates one if there is no active connection yet.
	 * @return A database connection to use to perform access-related tasks.
	 * @throws DataAccessException An exception if there was an issue opening a connection.
	 */
	public Connection getConnection() throws DataAccessException {
		if (conn == null) {
			return openConnection();
		} else {
			return conn;
		}
	}
	
	/**
	 * Closes the active database connection.
	 *
	 * When we are done manipulating the database it is important to close the connection. This will
	 * End the transaction and allow us to either commit our changes to the database or rollback any
	 * changes that were made before we encountered a potential error.
	 *
	 * @implNote IMPORTANT: IF YOU FAIL TO CLOSE A CONNECTION AND TRY TO REOPEN THE DATABASE THIS WILL
	 * CAUSE THE DATABASE TO LOCK. YOUR CODE MUST ALWAYS INCLUDE A CLOSURE OF THE DATABASE NO MATTER
	 * WHAT ERRORS OR PROBLEMS YOU ENCOUNTER.
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
			e.printStackTrace();
			throw new DataAccessException("Unable to close database connection: " + e.getMessage());
		}
	}
	
	/**
	 * Clears all database tables.
	 * @throws DataAccessException An exception if there was an issue clearing the database.
	 */
	public void clearTables() throws DataAccessException {
		if (conn == null) {
			throw new DataAccessException("There is no active database connection.");
		}
		DatabaseTable[] tables = DatabaseTable.values();
		
		for (DatabaseTable table : tables) {
			// It seems that PreparedStatement doesn't take table names.
			String sql = "DELETE FROM " + table.getName();
			
			try (Statement stmt = conn.createStatement()) {
				stmt.executeUpdate(sql);
				
			} catch (SQLException e) {
				throw new DataAccessException(
					"Error encountered while clearing table '" +
						table.getName() +
						"': "
						+ e.getMessage()
				);
			}
		}
	}
}

