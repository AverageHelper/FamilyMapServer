package transport;

import org.jetbrains.annotations.NotNull;

public class MissingKeyException extends Exception {
	public MissingKeyException(@NotNull String keyName) {
		super("Missing value for key '" + keyName + "'");
	}
}
