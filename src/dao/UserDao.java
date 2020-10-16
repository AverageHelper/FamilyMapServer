package dao;

import model.Gender;
import model.User;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

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
		String sql = "INSERT INTO " +
			table().getName() +
			" (username, password, email, first_name, last_name, gender, person_id) VALUES(?,?,?,?,?,?,?)";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, record.getId());
			stmt.setString(2, record.getPassword());
			stmt.setString(3, record.getEmail());
			stmt.setString(4, record.getFirstName());
			stmt.setString(5, record.getLastName());
			stmt.setString(6, record.getGender().getValue());
			stmt.setString(7, record.getPersonId());
			
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException("Error encountered while inserting into the database: " + e.getMessage());
		}
	}
	
	@Override
	protected @NotNull User recordFromQueryResult(ResultSet rs) throws SQLException {
		String genderValue = getNotNullString(rs, "gender");
		Gender gender = Gender.fromValue(genderValue);
		if (gender == null) {
			throw new SQLIntegrityConstraintViolationException("No valid event_type found");
		}
		
		return new User(
			getNotNullString(rs, "username"),
			getNotNullString(rs, "password"),
			getNotNullString(rs, "email"),
			getNotNullString(rs, "first_name"),
			getNotNullString(rs, "last_name"),
			gender,
			rs.getString("person_id")
		);
	}
}
