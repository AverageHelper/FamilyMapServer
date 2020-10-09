package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A type of important life event.
 */
public enum EventType {
	BIRTH("birth"),
	BAPTISM("baptism"),
	CHRISTENING("christening"),
	MARRIAGE("marriage"),
	DEATH("death");
	
	private final @NotNull String value;
	
	EventType(@NotNull String value) {
		this.value = value;
	}
	
	/**
	 * @return A string representation of the event type to store in the database.
	 */
	public @NotNull String getValue() {
		return value;
	}
	
	/**
	 * Attempts to create an <code>EventType</code> object from the given <code>value</code> string.
	 * @param value The name of a type of important life event.
	 * @return A new <code>EventType</code> instance, or <code>null</code> if no event type matched the given string.
	 */
	public static @Nullable EventType fromValue(@Nullable String value) {
		if (value == null) {
			return null;
		}
		switch (value.toLowerCase()) {
			case "birth": return BIRTH;
			case "baptism": return BAPTISM;
			case "christening": return CHRISTENING;
			case "marriage": return MARRIAGE;
			case "death": return DEATH;
			default: return null;
		}
	}
}
