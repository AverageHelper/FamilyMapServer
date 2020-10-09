package services;

import org.jetbrains.annotations.NotNull;

public class RegisterFailureException extends Exception {
	private final @NotNull RegisterFailureReason reason;
	
	public RegisterFailureException(@NotNull RegisterFailureReason reason) {
		this.reason = reason;
	}
	
	public @NotNull RegisterFailureReason getReason() {
		return reason;
	}
}
