package handlers;

import org.jetbrains.annotations.NotNull;
import responses.MessageResponse;

/**
 * A response to be sent to HTTP clients when something goes wrong.
 */
public class ErrorResponse extends MessageResponse {
	public ErrorResponse(@NotNull String message) {
		super("Error: " + message, false);
	}
}
