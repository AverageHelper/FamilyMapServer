package handlers;

public enum HandlingFailureReason {
	/**
	 * There is already a user with that username.
	 */
	DUPLICATE_USERNAME,
	
	/**
	 * The provided username is not one we know.
	 */
	USER_NOT_FOUND,
	
	/**
	 * The provided password was incorrect.
	 */
	INCORRECT_PASSWORD,
	
	/**
	 * There were too few path arguments provided.
	 */
	TOO_FEW_PATH_COMPONENTS,
	
	/**
	 * One of the path arguments was not the right type.
	 */
	MISTYPED_PATH_COMPONENT,
	
	/**
	 * An error occurred while parsing a JSON payload.
	 */
	JSON_PARSE;
}
