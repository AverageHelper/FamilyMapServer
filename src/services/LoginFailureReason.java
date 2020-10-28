package services;

public enum LoginFailureReason {
	/**
	 * The provided username is not one we know.
	 */
	USER_NOT_FOUND,
	
	/**
	 * The provided password was incorrect.
	 */
	INCORRECT_PASSWORD;
}
