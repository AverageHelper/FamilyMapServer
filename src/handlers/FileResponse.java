package handlers;

import transport.HTTPSerialization;
import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;

public class FileResponse implements HTTPSerialization {
	private final @NotNull String content;
	private final int httpCode;
	
	public FileResponse(@NotNull String content) {
		this(content, HttpURLConnection.HTTP_OK);
	}
	
	public FileResponse(@NotNull String content, int httpCode) {
		this.content = content;
		this.httpCode = httpCode;
	}
	
	@Override
	public @NotNull String serialize() {
		return content;
	}
	
	@Override
	public @NotNull String contentType() {
		return "text/html";
	}
	
	@Override
	public int httpResultCode() {
		return this.httpCode;
	}
}
