package dao;

import model.User;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An object that manages the reading and writing of <code>User</code> records in the database.
 */
public class UserDao extends Dao<User> {
	/**
	 * Creates a <code>UserDao</code> object.
	 *
	 * @param connection The database connection to use to perform access-related tasks.
	 */
	public UserDao(@NotNull Connection connection) {
		super(connection);
	}
	
	@Override
	protected @NotNull DatabaseTable table() {
		return DatabaseTable.USER;
	}
	
	@Override
	public void insert(@NotNull User record) throws DataAccessException {
	
	}
	
	@Override
	protected @NotNull User buildRecordFromQueryResult(ResultSet rs) throws SQLException {
		return null;
	}
}
