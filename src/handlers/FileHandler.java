package handlers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utilities.NameGenerator;

import java.io.File;
import java.io.IOException;

/**
 * An object that handles file requests.
 */
public class FileHandler extends Handler<FileResponse> {
	public FileHandler() {
		super();
	}
	
	@Override
	public @NotNull String expectedHTTPMethod() {
		return "GET";
	}
	
	@Override
	public boolean requiresAuthToken() {
		return false;
	}
	
	@Override
	public @NotNull FileResponse run(
		@NotNull String path,
		@Nullable String userName,
		@NotNull String req
	) throws IOException {
		String content = read(path);
		return new FileResponse(content);
	}
	
	public @NotNull String read(@NotNull String path) throws IOException {
		// Read the file at the given path relative to /web
		File file = new File("/web" + path).getAbsoluteFile();
		return NameGenerator.stringFromFile(file);
	}
}
