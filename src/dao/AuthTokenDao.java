package dao;

import model.AuthToken;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

/**
 * An object that manages the reading and writing of <code>AuthToken</code> records in the database.
 */
public class AuthTokenDao extends Dao<AuthToken> {
	/**
	 * Creates an <code>AuthTokenDao</code> object.
	 *
	 * @param connection The database connection to use to perform access-related tasks.
	 */
	public AuthTokenDao(Connection connection) {
		super(connection);
	}
	
	@Override
	protected @NotNull DatabaseTable table() {
		return DatabaseTable.AUTH_TOKEN;
	}
	
	@Override
	public void insert(@NotNull AuthToken record) throws DataAccessException {
		String sql = "INSERT INTO " +
			table().getName() +
			" (id, associated_username, created_at, is_valid) VALUES(?,?,?,?)";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, record.getId());
			stmt.setString(2, record.getAssociatedUsername());
			stmt.setInt(3, record.getCreatedAt());
			stmt.setBoolean(4, record.isValid());
			
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException("Error encountered while inserting into the database: " + e.getMessage());
		}
	}
	
	@Override
	protected @NotNull AuthToken buildRecordFromQueryResult(ResultSet rs) throws SQLException {
		return new AuthToken(
			rs.getString("id"),
			rs.getString("associated_username"),
			rs.getInt("created_at"),
			rs.getBoolean("is_valid")
		);
	}
}
