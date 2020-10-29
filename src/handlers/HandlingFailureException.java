package handlers;

import com.google.gson.JsonParseException;
import org.jetbrains.annotations.NotNull;
import server.Server;
import services.FetchDataFailureReason;
import services.FillFailureReason;
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
		this.reason = HandlingFailureReason.BAD_INPUT;
	}
	
	public static @NotNull HandlingFailureException from(@NotNull RegisterFailureReason reason) {
		switch (reason) {
			case DUPLICATE_USERNAME:
				return new HandlingFailureException(HandlingFailureReason.DUPLICATE_USERNAME);
			default:
				throw unknownFailureReason("registration", reason);
		}
	}
	
	public static @NotNull HandlingFailureException from(@NotNull LoginFailureReason reason) {
		switch (reason) {
			case INCORRECT_PASSWORD:
				return new HandlingFailureException(HandlingFailureReason.INCORRECT_PASSWORD);
			case USER_NOT_FOUND:
				return new HandlingFailureException(HandlingFailureReason.USER_NOT_FOUND);
			default:
				throw unknownFailureReason("login", reason);
		}
	}
	
	public static @NotNull HandlingFailureException from(@NotNull FetchDataFailureReason reason) {
		switch (reason) {
			case NOT_FOUND:
				return new HandlingFailureException(HandlingFailureReason.OBJECT_NOT_FOUND);
			default:
				throw unknownFailureReason("fetch", reason);
		}
	}
	
	public static HandlingFailureException from(@NotNull FillFailureReason reason) {
		switch (reason) {
			case DUPLICATE_OBJECT_ID:
				return new HandlingFailureException(HandlingFailureReason.DUPLICATE_OBJECT_ID);
			default:
				throw unknownFailureReason("fill", reason);
		}
	}
	
	private static <T extends Enum<T>> IllegalStateException unknownFailureReason(
		@NotNull String type,
		T reason
	) {
		IllegalStateException e = new IllegalStateException(
			"Unknown " + type + " failure reason " + reason.name()
		);
		e.printStackTrace();
		Server.logger.severe(e.getMessage());
		return e;
	}
	
	public @NotNull HandlingFailureReason getReason() {
		return reason;
	}
}
