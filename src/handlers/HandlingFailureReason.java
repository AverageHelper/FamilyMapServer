package handlers;

public enum HandlingFailureReason {
	/**
	 * There is already a user with that username.
	 */
	DUPLICATE_USERNAME,
	
	/**
	 * There is already an object in the database with that ID.
	 */
	DUPLICATE_OBJECT_ID,
	
	/**
	 * The provided username is not one we know.
	 */
	USER_NOT_FOUND,
	
	/**
	 * The requested object was not found.
	 */
	OBJECT_NOT_FOUND,
	
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
	 * The given input was incorrect.
	 */
	BAD_INPUT;
}
