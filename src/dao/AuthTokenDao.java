package dao;

import model.AuthToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;

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
	
	}
	
	@Override
	public @Nullable AuthToken find(@NotNull String id) throws DataAccessException {
		return null;
	}
}
