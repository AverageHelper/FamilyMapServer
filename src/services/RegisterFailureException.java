package services;

import dao.DataAccessException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RegisterFailureException extends Exception {
	private final @NotNull RegisterFailureReason reason;
	
	public RegisterFailureException(
		@NotNull RegisterFailureReason reason,
		@Nullable DataAccessException databaseException
	) {
		this.reason = reason;
		if (databaseException != null) {
			this.initCause(databaseException);
		}
	}
	
	public @NotNull RegisterFailureReason getReason() {
		return reason;
	}
}
