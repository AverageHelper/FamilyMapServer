package services;

import handlers.FetchDataRequest;

/**
 * An object that serves a single data-fetch request.
 */
public class FetchDataService {
	/**
	 * Attempts to fetch data of a given type from the server.
	 * @param request Information about the sort of data to retrieve.
	 * @param <T> The model type of the data to fetch.
	 * @return The result of the fetch operation.
	 */
	public <T> FetchDataResult<T> fetch(FetchDataRequest request) {
		return new FetchDataResult<T>(FetchDataFailureReason.UNIMPLEMENTED);
	}
}
