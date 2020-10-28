package handlers;

import com.google.gson.JsonParseException;
import org.jetbrains.annotations.NotNull;
import services.LoginFailureReason;
import services.RegisterFailureReason;

public class HandlingFailureException extends Exception {
	private final @NotNull HandlingFailureReason reason;
	
	public HandlingFailureException(@NotNull HandlingFailureReason reason) {
		super(reason.name());
		this.reason = reason;
	}
	
	public HandlingFailureException(@NotNull HandlingFailureReason reason, Exception e) {
		super(reason.name() + ": " + e.getMessage(), e);
		this.reason = reason;
	}
	
	public HandlingFailureException(@NotNull HandlingFailureReason reason, @NotNull String message) {
		super(reason.name() + ": " + message);
		this.reason = reason;
	}
	
	public HandlingFailureException(@NotNull HandlingFailureReason reason, @NotNull String message, Exception e) {
		super(reason.name() + ": " + message, e);
		this.reason = reason;
	}
	
	public HandlingFailureException(@NotNull JsonParseException exception) {
		super("Failed to parse JSON: " + exception.getMessage(), exception);
		this.reason = HandlingFailureReason.JSON_PARSE;
	}
	
	public static @NotNull HandlingFailureException from(@NotNull RegisterFailureReason reason) {
		switch (reason) {
			case DUPLICATE_USERNAME:
				return new HandlingFailureException(HandlingFailureReason.DUPLICATE_USERNAME);
			default:
				throw new IllegalStateException("Unknown failure reason " + reason.toString());
		}
	}
	
	public static @NotNull HandlingFailureException from(@NotNull LoginFailureReason reason) {
		switch (reason) {
			case INCORRECT_PASSWORD:
				return new HandlingFailureException(HandlingFailureReason.INCORRECT_PASSWORD);
			case USER_NOT_FOUND:
				return new HandlingFailureException(HandlingFailureReason.USER_NOT_FOUND);
			default:
				throw new IllegalStateException("Unknown failure reason " + reason.toString());
		}
	}
	
	public @NotNull HandlingFailureReason getReason() {
		return reason;
	}
}
