package responses;

import transport.JSONSerialization;
import transport.MissingKeyException;
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
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public void assertCorrectDeserialization() throws MissingKeyException {
		if (this.message == null) {
			throw new MissingKeyException("message");
		}
	}
}
