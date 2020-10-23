package services;

public enum RegisterFailureReason {
	/**
	 * A database-related issue occurred.
	 */
	DATABASE,
	
	/**
	 * There is already a user with that username.
	 */
	DUPLICATE_USERNAME;
}
