package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum Gender {
	MALE("m"),
	FEMALE("f");
	
	private final @NotNull String value;
	
	Gender(@NotNull String value) {
		this.value = value;
	}
	
	public @NotNull String getValue() {
		return value;
	}
	
	public static @Nullable Gender fromValue(@Nullable String value) {
		if (value == null) {
			return null;
		}
		switch (value) {
			case "m": return MALE;
			case "f": return FEMALE;
			default: return null;
		}
	}
}
