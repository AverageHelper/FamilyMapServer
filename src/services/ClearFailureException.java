package services;

import org.jetbrains.annotations.NotNull;

/**
 * Indicates a problem while trying to clear the database.
 */
public class ClearFailureException extends Exception {
	private final @NotNull ClearFailureReason reason;
	
	public ClearFailureException(@NotNull ClearFailureReason reason) {
		this.reason = reason;
	}
	
	public @NotNull ClearFailureReason getReason() {
		return reason;
	}
}
