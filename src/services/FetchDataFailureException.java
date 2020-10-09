package services;

import org.jetbrains.annotations.NotNull;

/**
 * Indicates a problem while trying to fetch data from the database.
 */
public class FetchDataFailureException extends Exception {
	private final @NotNull FetchDataFailureReason reason;
	
	public FetchDataFailureException(@NotNull FetchDataFailureReason reason) {
		this.reason = reason;
	}
	
	public @NotNull FetchDataFailureReason getReason() {
		return reason;
	}
}
