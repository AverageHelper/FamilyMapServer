package dao;

import model.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	 * @return A <code>TableName</code> instance that represents records' table name.
	 */
	protected abstract @NotNull DatabaseTable table();
	
	
	
	/**
	 * Attempts to add a new record to the database.
	 *
	 * @param record The data to write.
	 * @throws DataAccessException An exception if the write fails.
	 */
	public abstract void insert(@NotNull T record) throws DataAccessException;
	
	
	
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
				@NotNull T result = buildRecordFromQueryResult(rs);
				results.add(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("Error encountered while finding record: " + e.getMessage());
			
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
	 * Builds an instance of the model type from the given SQL query result set.
	 * @param rs The result of a <code>SELECT</code> query.
	 * @return An instance of <code>T</code>.
	 * @throws SQLException An exception if the data is incorrectly structured.
	 */
	protected abstract @NotNull T buildRecordFromQueryResult(ResultSet rs) throws SQLException;
	
	
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
			throw new DataAccessException("Error encountered while deleting event: " + e.getMessage());
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
				"Error encountered while clearing table '" +
					table().getName() +
					"': "
					+ e.getMessage()
			);
		}
	}
}
