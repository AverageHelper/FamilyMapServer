package dao;

import model.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sqlite.SQLiteErrorCode;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Objects that extend this class manage the reading and writing of records in the database.
 */
public abstract class Dao<T extends ModelData> {
	protected @NotNull Connection connection;
	
	public Dao(@NotNull Connection connection) {
		this.connection = connection;
	}
	
	
	
	
	/**
	 * The name of the database table associated with records managed by this DAO.
	 *
	 * @return A <code>DatabaseTable</code> instance that represents records' table name.
	 */
	protected abstract @NotNull DatabaseTable table();
	
	
	
	
	/**
	 * Adds a new record to the database.
	 *
	 * @param record The data to write.
	 * @throws DataAccessException An exception if the write fails.
	 */
	public abstract void insert(@NotNull T record) throws DataAccessException;
	
	
	
	
	
	
	/**
	 * Attempts to add a new record to the database. If SQL reports that the new record would
	 * duplicate another record's ID, then nothing changes.
	 *
	 * @param record The data to write.
	 * @throws DataAccessException An exception if the write fails.
	 */
	public void insertIfNotExists(@NotNull T record) throws DataAccessException {
		try {
			insert(record);
		} catch (DataAccessException e) {
			SQLiteErrorCode code = e.getErrorCode();
			String message = e.getMessage();
			
			if (code != SQLiteErrorCode.SQLITE_CONSTRAINT ||
				!message.contains("." + table().getPrimaryKey())
			) {
				throw e;
			}
		}
	}
	
	
	/**
	 * Removes the stored record, and replaces it with the given record.
	 *
	 * @param record The record to update.
	 * @throws DataAccessException An exception if the write fails.
	 */
	public void update(@NotNull T record) throws DataAccessException {
		delete(record.getId());
		insert(record);
	}
	
	
	
	
	/**
	 * Attempts to fetch from the database a list of records which have the given <code>value</code>
	 * for the given <code>key</code>.
	 *
	 * @param column The table column by which to filter results.
	 * @param value The value which the record should have at the specified <code>column</code> to match the filter.
	 * @return A list of fully realized objects of type <code>T</code>.
	 * @throws DataAccessException An exception if the read fails, or any of the objects could not be deserialized from the returned data.
	 */
	protected @NotNull List<T> findMultiple(
		@NotNull String column,
		@NotNull String value
	) throws DataAccessException {
		List<T> results = new ArrayList<>();
		
		ResultSet rs = null;
		String sql = "SELECT * FROM " +
			table().getName() +
			" WHERE " +
			column +
			" = ?;";
		
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, value);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				@NotNull T result = recordFromQueryResult(rs);
				results.add(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException(e, "Error encountered while finding record: " + e.getMessage());
			
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return results;
	}
	
	
	
	/**
	 * Attempts to fetch from the database a record with the given <code>id</code>.
	 *
	 * @param id The value of the record's primary key.
	 * @return A fully realized object of type <code>T</code>, or <code>null</code> if the record could not be found.
	 * @throws DataAccessException An exception if the read fails, or the object could not be deserialized from the returned data.
	 */
	public @Nullable T find(@NotNull String id) throws DataAccessException {
		List<T> results = findMultiple(table().getPrimaryKey(), id);
		if (results.isEmpty()) {
			return null;
		}
		return results.get(0);
	}
	
	
	/**
	 * @return The number of records in the database table.
	 * @throws DataAccessException An exception if the read fails.
	 */
	public int count() throws DataAccessException {
		String sql = "SELECT COUNT(DISTINCT " +
			table().getPrimaryKey() +
			") FROM " +
			table().getName();
		
		try (Statement stmt = connection.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			return rs.getInt(1);
			
		} catch (SQLException e) {
			throw new DataAccessException(
				e,
				"Error encountered while reading table '" +
					table().getName() +
					"': "
					+ e.getMessage()
			);
		}
	}
	
	
	
	
	/**
	 * Builds an instance of the model type from the given SQL query result set.
	 *
	 * @param rs The result of a <code>SELECT</code> query.
	 * @return An instance of <code>T</code>.
	 * @throws SQLException An exception if the data is incorrectly structured.
	 */
	protected abstract @NotNull T recordFromQueryResult(ResultSet rs) throws SQLException;
	
	
	
	
	
	/**
	 * Gets a string from the given result set. If the returned value is <code>null</code>,
	 * an <code>SQLIntegrityConstraintViolationException</code> is thrown.
	 *
	 * @param rs The SQL result set.
	 * @param columnLabel The column label where the string is stored.
	 * @return The non-<code>null</code> value at the given column.
	 * @throws SQLException An exception if there was a problem retrieving the value from the result set, or if the returned value was <code>null</code>.
	 */
	protected @NotNull String getNotNullString(
		@NotNull ResultSet rs,
		@NotNull String columnLabel
	) throws SQLException {
		String value = rs.getString(columnLabel);
		if (value == null) {
			throw new SQLIntegrityConstraintViolationException(
				"No valid string for " +
					columnLabel +
					" found"
			);
		}
		return value;
	}
	
	
	
	
	/**
	 * Attempts to delete from the database a record with the given <code>id</code>.
	 *
	 * @param id The value of the record's primary key.
	 * @throws DataAccessException An exception if the write fails.
	 */
	public void delete(@NotNull String id) throws DataAccessException {
		String sql = "DELETE FROM " +
			table().getName() +
			" WHERE " +
			table().getPrimaryKey() +
			" = ?;";
		
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, id);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException(e, "Error encountered while deleting event: " + e.getMessage());
		}
	}
	
	
	
	/**
	 * Attempts to clear all data from the table.
	 *
	 * @throws DataAccessException An exception if the write fails.
	 */
	public void clearAll() throws DataAccessException {
		String sql = "DELETE FROM " + table().getName();
		
		try (Statement stmt = connection.createStatement()) {
			stmt.executeUpdate(sql);
			
		} catch (SQLException e) {
			throw new DataAccessException(
				e,
				"Error encountered while clearing table '" +
					table().getName() +
					"': "
					+ e.getMessage()
			);
		}
	}
}
