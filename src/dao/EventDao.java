package dao;

import model.Event;
import org.jetbrains.annotations.NotNull;

/**
 * An object that manages the reading and writing of Event data in the database.
 */
public class EventDao {
	private static @NotNull TableName tableName() {
		return TableName.EVENT;
	}
	
	/**
	 * Attempts to read from the database an Event record with the given <code>id</code>.
	 *
	 * @param id The event's unique identifier.
	 * @return A fully realized <code>Event</code> object if the user was found.
	 * @throws Exception An exception if the read fails.
	 */
	static @NotNull Event readWithId(@NotNull String id) throws Exception {
		return null;
	}
	
	/**
	 * Attempts to write the event record to the database.
	 *
	 * @param event The event data to write.
	 * @throws Exception An exception if the write fails.
	 */
	static void write(@NotNull Event event) throws Exception {
	
	}
	
	/**
	 * Attempts to remove from the database a given Event object.
	 *
	 * @param event The event to delete.
	 * @throws Exception An exception if the write fails.
	 */
	static void delete(@NotNull Event event) throws Exception {
	
	}
}
