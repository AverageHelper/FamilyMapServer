package dao;

import org.jetbrains.annotations.NotNull;

public enum TableName {
	// Defined by the database schema
	AUTH_TOKEN("AuthToken"),
	EVENT("Event"),
	PERSON("Person"),
	USER("User");
	
	private final @NotNull String value;
	
	TableName(@NotNull String value) {
		this.value = value;
	}
	
	public @NotNull String getValue() {
		return value;
	}
}
