package services;

import org.jetbrains.annotations.NotNull;

/**
 * Indicates a problem logging in.
 */
public class LoginFailureException extends Exception {
	private final @NotNull LoginFailureReason reason;
	
	public LoginFailureException(@NotNull LoginFailureReason reason) {
		this.reason = reason;
	}
	
	public @NotNull LoginFailureReason getReason() {
		return reason;
	}
}
