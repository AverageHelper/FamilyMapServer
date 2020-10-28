package handlers;

import org.jetbrains.annotations.NotNull;

public class EmptyValueException extends Exception {
	public EmptyValueException(@NotNull String keyName) {
		super("Empty value for key '" + keyName + "'");
	}
}
