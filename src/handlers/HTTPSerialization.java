package handlers;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;

/**
 * Objects that inherit from this type can be easily converted into a transportable string.
 */
public interface HTTPSerialization {
	/**
	 * @return A string representation of the receiver to be sent via HTTP.
	 */
	public @NotNull String serialize();
	
	/**
	 * @return The HTTP content type of the payload.
	 */
	public @NotNull String contentType();
	
	public default int httpResultCode() {
		return HttpURLConnection.HTTP_OK;
	}
}
