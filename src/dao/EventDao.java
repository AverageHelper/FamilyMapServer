package dao;

import model.Event;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.List;

/**
 * An object that manages the reading and writing of <code>Event</code> records in the database.
 */
public class EventDao extends Dao<Event> {
	/**
	 * Creates an <code>EventDao</code> object.
	 *
	 * @param connection The database connection to use to perform access-related tasks.
	 */
	public EventDao(@NotNull Connection connection) {
		super(connection);
	}
	
	@Override
	protected @NotNull DatabaseTable table() {
		return DatabaseTable.EVENT;
	}
	
	@Override
	public void insert(@NotNull Event record) throws DataAccessException {
		String sql = "INSERT INTO " +
			table().getName() +
			" (id, associated_username, person_id, latitude, longitude, country, city, event_type, year) VALUES(?,?,?,?,?,?,?,?,?)";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, record.getId());
			stmt.setString(2, record.getAssociatedUsername());
			stmt.setString(3, record.getPersonID());
			
			if (record.getLatitude() == null) {
				stmt.setNull(4, Types.REAL);
			} else {
				stmt.setDouble(4, record.getLatitude());
			}
			
			if (record.getLongitude() == null) {
				stmt.setNull(5, Types.REAL);
			} else {
				stmt.setDouble(5, record.getLongitude());
			}
			
			stmt.setString(6, record.getCountry());
			stmt.setString(7, record.getCity());
			stmt.setString(8, record.getEventType());
			stmt.setInt(9, record.getYear());
			
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException(e, "Error encountered while inserting into the database: " + e.getMessage());
		}
	}
	
	protected @NotNull Event recordFromQueryResult(ResultSet rs) throws SQLException {
		return new Event(
			getNotNullString(rs, "id"),
			getNotNullString(rs, "associated_username"),
			getNotNullString(rs, "person_id"),
			(Double) rs.getObject("latitude"),
			(Double) rs.getObject("longitude"),
			rs.getString("country"),
			rs.getString("city"),
			getNotNullString(rs, "event_type"),
			rs.getInt("year")
		);
	}
	
	/**
	 * Attempts to fetch from the database a list of events associated with a user with the given
	 * <code>username</code>.
	 *
	 * @param username The username of the user to which Event records should be associated to match the filter.
	 * @return A list of fully realized <code>Event</code> objects.
	 * @throws DataAccessException An exception if the read fails, or any of the objects could not be deserialized from the returned data.
	 */
	public @NotNull List<Event> findForUser(@NotNull String username) throws DataAccessException {
		return findMultiple("associated_username", username);
	}
	
	/**
	 * Attempts to fetch from the database a list of events associated with a <code>Person</code>
	 * with the given <code>id</code>.
	 *
	 * @param id The username of the user to which Event records should be associated to match the filter.
	 * @return A list of fully realized <code>Event</code> objects.
	 * @throws DataAccessException An exception if the read fails, or any of the objects could not be deserialized from the returned data.
	 */
	public @NotNull List<Event> findForPerson(@NotNull String id) throws DataAccessException {
		return findMultiple("person_id", id);
	}
}
