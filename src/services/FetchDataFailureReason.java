package services;

public enum FetchDataFailureReason {
	/**
	 * No data was found matching the specified criteria.
	 */
	NOT_FOUND,
	
	/**
	 * The calling user doesn't have permission to access the requested data.
	 */
	UNAUTHORIZED;
}
