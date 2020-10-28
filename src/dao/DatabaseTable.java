package dao;

import org.jetbrains.annotations.NotNull;

public enum DatabaseTable {
	// Defined by the database schema
	AUTH_TOKEN("AuthToken", "id"),
	EVENT("Event", "id"),
	PERSON("Person", "id"),
	USER("User", "userName");
	
	private final @NotNull String name;
	private final @NotNull String primaryKey;
	
	DatabaseTable(@NotNull String name, @NotNull String primaryKey) {
		this.name = name;
		this.primaryKey = primaryKey;
	}
	
	/**
	 * @return The name of the database table.
	 */
	public @NotNull String getName() {
		return name;
	}
	
	/**
	 * @return The name of the table's primary key.
	 */
	public @NotNull String getPrimaryKey() {
		return this.primaryKey;
	}
}
