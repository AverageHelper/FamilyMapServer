package handlers;

import org.jetbrains.annotations.NotNull;
import services.LoginFailureReason;
import services.RegisterFailureReason;

public class HandlingFailureException extends Exception {
	private final @NotNull HandlingFailureReason reason;
	
	public HandlingFailureException(@NotNull HandlingFailureReason reason) {
		this.reason = reason;
	}
	
	public HandlingFailureException(@NotNull RegisterFailureReason reason) {
		switch (reason) {
			case DUPLICATE_USERNAME:
				this.reason = HandlingFailureReason.DUPLICATE_USERNAME;
				break;
			default:
				throw new IllegalStateException("Unknown failure reason " + reason.toString());
		}
	}
	
	public HandlingFailureException(@NotNull LoginFailureReason reason) {
		switch (reason) {
			case INCORRECT_PASSWORD:
				this.reason = HandlingFailureReason.INCORRECT_PASSWORD;
				break;
			case USER_NOT_FOUND:
				this.reason = HandlingFailureReason.USER_NOT_FOUND;
				break;
			default:
				throw new IllegalStateException("Unknown failure reason " + reason.toString());
		}
	}
	
	public @NotNull HandlingFailureReason getReason() {
		return reason;
	}
}
