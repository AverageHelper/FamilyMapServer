package dao;

import model.Gender;
import model.Person;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.List;

/**
 * An object that manages the reading and writing of <code>Person</code> records in the database.
 */
public class PersonDao extends Dao<Person> {
	/**
	 * Creates a <code>PersonDao</code> object.
	 *
	 * @param connection The database connection to use to perform access-related tasks.
	 */
	public PersonDao(Connection connection) {
		super(connection);
	}
	
	@Override
	protected @NotNull DatabaseTable table() {
		return DatabaseTable.PERSON;
	}
	
	@Override
	public void insert(@NotNull Person record) throws DataAccessException {
		String sql = "INSERT INTO " +
			table().getName() +
			" (id, associated_username, first_name, last_name, gender, father_id, mother_id, spouse_id) VALUES(?,?,?,?,?,?,?,?)";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, record.getId());
			stmt.setString(2, record.getAssociatedUsername());
			stmt.setString(3, record.getFirstName());
			stmt.setString(4, record.getLastName());
			stmt.setString(5, record.getGender().getValue());
			stmt.setString(6, record.getFatherId());
			stmt.setString(7, record.getMotherId());
			stmt.setString(8, record.getSpouseId());
			
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException("Error encountered while inserting into the database: " + e.getMessage());
		}
	}
	
	@Override
	protected @NotNull Person recordFromQueryResult(ResultSet rs) throws SQLException {
		String genderValue = getNotNullString(rs, "gender");
		Gender gender = Gender.fromValue(genderValue);
		if (gender == null) {
			throw new SQLIntegrityConstraintViolationException("No valid event_type found");
		}
		
		return new Person(
			getNotNullString(rs, "id"),
			getNotNullString(rs, "associated_username"),
			getNotNullString(rs, "first_name"),
			getNotNullString(rs, "last_name"),
			gender,
			rs.getString("father_id"),
			rs.getString("mother_id"),
			rs.getString("spouse_id")
		);
	}
	
	/**
	 * Attempts to fetch from the database a list of person records associated with a user with the given <code>username</code>.
	 *
	 * @param username The username of the user to which Person records should be associated to match the filter.
	 * @return A list of fully realized <code>Person</code> objects.
	 * @throws DataAccessException An exception if the read fails, or any of the objects could not be deserialized from the returned data.
	 */
	public @NotNull List<Person> findForUser(@NotNull String username) throws DataAccessException {
		return findMultiple("associated_username", username);
	}
}
