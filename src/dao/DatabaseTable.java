package dao;

import org.jetbrains.annotations.NotNull;

public enum DatabaseTable implements IDatabaseTable {
	// Defined by the database schema
	AUTH_TOKEN("AuthToken", "id"),
	EVENT("Event", "id"),
	PERSON("Person", "id"),
	USER("User", "username");
	
	private final @NotNull String name;
	private final @NotNull String primaryKey;
	
	DatabaseTable(@NotNull String name, @NotNull String primaryKey) {
		this.name = name;
		this.primaryKey = primaryKey;
	}
	
	@Override
	public @NotNull String getName() {
		return name;
	}
	
	@Override
	public @NotNull String getPrimaryKey() {
		return this.primaryKey;
	}
}
