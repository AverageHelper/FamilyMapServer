package services;

import database.DataAccessException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Indicates a problem logging in.
 */
public class LoginFailureException extends Exception {
	private final @NotNull LoginFailureReason reason;
	
	public LoginFailureException(
		@NotNull LoginFailureReason reason,
		@Nullable DataAccessException databaseException
	) {
		this.reason = reason;
		if (databaseException != null) {
			this.initCause(databaseException);
		}
	}
	
	public @NotNull LoginFailureReason getReason() {
		return reason;
	}
}
