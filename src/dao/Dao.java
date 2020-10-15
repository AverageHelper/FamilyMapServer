package dao;

import model.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
	 * Attempts to fetch from the database a record with the given <code>id</code>.
	 *
	 * @param id The value of the record's primary key.
	 * @return A fully realized object of type <code>T</code>, or <code>null</code> if the record could not be found.
	 * @throws DataAccessException An exception if the read fails.
	 */
	public abstract @Nullable T find(@NotNull String id) throws DataAccessException;
	
	
	
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
