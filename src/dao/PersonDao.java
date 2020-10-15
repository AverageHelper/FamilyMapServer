package dao;

import model.Gender;
import model.Person;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

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
	protected @NotNull Person buildRecordFromQueryResult(ResultSet rs) throws SQLException {
		String genderValue = rs.getString("gender");
		Gender gender = Gender.fromValue(genderValue);
		if (gender == null) {
			throw new SQLIntegrityConstraintViolationException("No valid event_type found");
		}
		
		return new Person(
			rs.getString("id"),
			rs.getString("associated_username"),
			rs.getString("first_name"),
			rs.getString("last_name"),
			gender,
			rs.getString("father_id"),
			rs.getString("mother_id"),
			rs.getString("spouse_id")
		);
	}
}
