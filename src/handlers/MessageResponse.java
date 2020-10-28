package handlers;

import org.jetbrains.annotations.NotNull;

public class MessageResponse extends JSONSerialization {
	protected @NotNull String message;
	protected boolean success;
	
	public MessageResponse(@NotNull String message, boolean success) {
		this.message = message;
		this.success = success;
	}
	
	public MessageResponse(@NotNull String message) {
		this.message = message;
		this.success = true;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean isSuccessful() {
		return success;
	}
}
