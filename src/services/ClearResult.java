package services;

import org.jetbrains.annotations.Nullable;

/**
 * The result of a clear-database operation.
 */
public class ClearResult {
	private final @Nullable ClearFailureReason failureReason;
	
	public ClearResult() {
		this(null);
	}
	
	public ClearResult(@Nullable ClearFailureReason failureReason) {
		this.failureReason = failureReason;
	}
	
	/**
	 * @return The reason the operation failed, or <code>null</code> if the request succeeded.
	 */
	public @Nullable ClearFailureReason getFailureReason() {
		return failureReason;
	}
}
