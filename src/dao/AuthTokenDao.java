package dao;

import model.AuthToken;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.List;

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
			stmt.setLong(3, record.getCreatedAt().getTime());
			stmt.setBoolean(4, record.isValid());
			
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e, "Error encountered while inserting into the database: " + e.getMessage());
		}
	}
	
	@Override
	protected @NotNull AuthToken recordFromQueryResult(ResultSet rs) throws SQLException {
		long timestamp = rs.getLong("created_at");
		java.util.Date createdAt = new java.util.Date(timestamp);
		return new AuthToken(
			getNotNullString(rs, "id"),
			getNotNullString(rs, "associated_username"),
			createdAt,
			rs.getBoolean("is_valid")
		);
	}
	
	/**
	 * Attempts to fetch from the database a list of auth tokens associated with a user with the given
	 * <code>username</code>.
	 *
	 * @param username The username of the user to which Event records should be associated to match the filter.
	 * @return A list of fully realized <code>AuthToken</code> objects.
	 * @throws DataAccessException An exception if the read fails, or any of the objects could not be deserialized from the returned data.
	 */
	public @NotNull List<AuthToken> findForUser(@NotNull String username) throws DataAccessException {
		return findMultiple("associated_username", username);
	}
}
