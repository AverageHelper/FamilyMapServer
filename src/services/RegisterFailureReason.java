package services;

public enum RegisterFailureReason {
	/**
	 * An error occurred during some database operation.
	 */
	DATABASE,
	
	/**
	 * There is already a user with that username.
	 */
	DUPLICATE_USERNAME,
	
	/**
	 * The feature is not yet implemented.
	 */
	UNIMPLEMENTED;
}
