package services;

import model.AuthToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The result of a login request. If the request was successful, then the result contains a valid Authorization token to return to the user. If the request failed, then the result contains information about the failure.
 */
public class LoginResult {
	private final @Nullable AuthToken token;
	private final @Nullable LoginFailureReason failureReason;
	
	/**
	 * Creates a successful <code>LoginResult</code>.
	 * @param token An <code>AuthToken</code> that the user may use to access data.
	 */
	public LoginResult(@NotNull AuthToken token) {
		this.token = token;
		this.failureReason = null;
	}
	
	/**
	 * Creates a failed <code>LoginResult</code>.
	 * @param failureReason The reason the operation failed.
	 */
	public LoginResult(@NotNull LoginFailureReason failureReason) {
		this.token = null;
		this.failureReason = failureReason;
	}
	
	/**
	 * @return The new authorization token, or <code>null</code> if the request failed (i.e. if <code>getFailureReason()</code> returns a non-<code>null</code> value).
	 */
	public @Nullable AuthToken getToken() {
		return token;
	}
	
	/**
	 * @return The reason the authorization failed, or <code>null</code> if the request succeeded (i.e. if <code>getToken()</code> returns a non-<code>null</code> value).
	 */
	public @Nullable LoginFailureReason getFailureReason() {
		return failureReason;
	}
}
