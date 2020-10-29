package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A person's gender.
 */
public enum Gender {
	MALE("m"),
	FEMALE("f");
	
	private final @NotNull String value;
	
	Gender(@NotNull String value) {
		this.value = value;
	}
	
	/**
	 * @return A short code for the gender to store in the database.
	 */
	public @NotNull String getValue() {
		return value;
	}
	
	/**
	 * Attempts to create a <code>Gender</code> object from the given <code>value</code> string.
	 * @param value The short gender code. Either <code>"m"</code> or <code>"f"</code>.
	 * @return A new <code>Gender</code> instance, or <code>null</code> if no gender matched the given string.
	 */
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
