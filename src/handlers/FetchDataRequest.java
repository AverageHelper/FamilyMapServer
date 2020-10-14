package handlers;

import dao.TableName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Data formatted to request a single data object from the server.
 */
public class FetchDataRequest extends JSONConvertible {
	private final @NotNull TableName tableName;
	private @Nullable String id;
	
	// TODO: Add more fields to describe the data to be fetched, how it ought to be sorted, etc.
	
	/**
	 * Creates a <code>FetchDataRequest</code> object.
	 * @param tableName The name of the type of data to fetch from the database.
	 */
	public FetchDataRequest(@NotNull TableName tableName) {
		this.tableName = tableName;
	}
	
	/**
	 * @return The type of data to be fetched.
	 */
	public @NotNull TableName getTableName() {
		return tableName;
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
