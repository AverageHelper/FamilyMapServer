package handlers;

import org.jetbrains.annotations.NotNull;

public class FileResponse implements HTTPSerialization {
	private final @NotNull String content;
	
	public FileResponse(@NotNull String content) {
		this.content = content;
	}
	
	@Override
	public @NotNull String serialize() {
		return content;
	}
	
	@Override
	public @NotNull String contentType() {
		return "text/html";
	}
}
