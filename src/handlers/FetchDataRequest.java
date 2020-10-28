package handlers;

import dao.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Data formatted to request a single data object from the server.
 */
public class FetchDataRequest extends JSONSerialization {
	private final @NotNull DatabaseTable databaseTable;
	private @Nullable String id;
	
	/**
	 * Creates a <code>FetchDataRequest</code> object.
	 * @param databaseTable The name of the type of data to fetch from the database.
	 */
	public FetchDataRequest(@NotNull DatabaseTable databaseTable) {
		this.databaseTable = databaseTable;
	}
	
	/**
	 * @return The type of data to be fetched.
	 */
	public @NotNull DatabaseTable getTable() {
		return databaseTable;
	}
	
	/**
	 * @return The identifier of the object to return.
	 */
	public @Nullable String getId() {
		return id;
	}
	
	/**
	 * Set the identifier of the object to return. If this field is set, a successful fetch result contains exactly one object.
	 * @param id The identifier of the object to return.
	 */
	public void setId(@Nullable String id) {
		this.id = id;
	}
}
