package services;

public enum ClearFailureReason {
	/**
	 * An error occurred during some database operation.
	 */
	DATABASE,
	
	/**
	 * The feature is not yet implemented.
	 */
	UNIMPLEMENTED,
	
	/**
	 * No auth token was provided, or the provided token was invalid.
	 */
	UNAUTHENTICATED;
}
