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
	
	public @NotNull String getValue() {
		return value;
	}
	
	public static @Nullable EventType fromValue(@Nullable String value) {
		if (value == null) {
			return null;
		}
		switch (value) {
			case "birth": return BIRTH;
			case "baptism": return BAPTISM;
			case "christening": return CHRISTENING;
			case "marriage": return MARRIAGE;
			case "death": return DEATH;
			default: return null;
		}
	}
}
