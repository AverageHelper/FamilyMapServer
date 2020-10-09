package services;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The result of a data fetch request. If the request was successful, then the result contains a list of objects of the provided type. If the request failed, then the result contains information about the failure.
 * @param <T> The model type of the data to fetch.
 */
public class FetchDataResult<T> {
	private final @Nullable List<T> data;
	private final @Nullable FetchDataFailureReason failureReason;
	
	/**
	 * Creates a successful <code>FetchDataResult</code>.
	 * @param data The fetched data.
	 */
	public FetchDataResult(@NotNull List<T> data) {
		this.data = data;
		this.failureReason = null;
	}
	
	/**
	 * Creates a failed <code>FetchDataResult</code>.
	 * @param failureReason The reason the operation failed.
	 */
	public FetchDataResult(@Nullable FetchDataFailureReason failureReason) {
		this.data = null;
		this.failureReason = failureReason;
	}
	
	/**
	 * @return The stored data, or <code>null</code> if the request failed (i.e. if <code>getFailureReason()</code> returns a non-<code>null</code> value).
	 */
	public @Nullable List<T> getData() {
		return data;
	}
	
	/**
	 * @return The reason the request failed, or <code>null</code> if the request succeeded (i.e. if <code>getToken()</code> returns a non-<code>null</code> value).
	 */
	public @Nullable FetchDataFailureReason getFailureReason() {
		return failureReason;
	}
}
