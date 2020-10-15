package dao;

import model.AuthToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;

/**
 * An object that manages the reading and writing of <code>AuthToken</code> records in the database.
 */
public class AuthTokenDao extends Dao<AuthToken> {
	private static @NotNull TableName tableName() {
		return TableName.AUTH_TOKEN;
	}
	
	/**
	 * Creates an <code>AuthTokenDao</code> object.
	 *
	 * @param connection The database connection to use to perform access-related tasks.
	 */
	public AuthTokenDao(Connection connection) {
		super(connection);
	}
	
	@Override
	public void insert(@NotNull AuthToken record) throws DataAccessException {
	
	}
	
	@Override
	public @Nullable AuthToken find(@NotNull String id) throws DataAccessException {
		return null;
	}
	
	@Override
	public void delete(@NotNull String id) throws DataAccessException {
	
	}
}
