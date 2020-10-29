package handlers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import server.Server;
import utilities.FileHelpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;

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
		return read(path);
	}
	
	public @NotNull FileResponse read(@NotNull String path) throws IOException {
		if (path.equals("/")) {
			path = "index.html";
		}
		
		// Read the file at the given path relative to /web
		File htmlRoot = new File("web/").getAbsoluteFile();
		File file = new File(htmlRoot, path).getAbsoluteFile();
		
		try {
			Server.logger.finer("Filling web request at path '" + file.getPath() + "'");
			if (!fileIsDescendantOfOther(htmlRoot, file)) {
				throw new FileNotFoundException("Callers cannot access files outside of the HTML root");
			}
			String content = FileHelpers.stringFromFile(file);
			return new FileResponse(content);
			
		} catch (FileNotFoundException e) {
			Server.logger.finer("File not found.");
			file = new File(htmlRoot, "HTML/404.html").getAbsoluteFile();
			String content = FileHelpers.stringFromFile(file);
			return new FileResponse(content, HttpURLConnection.HTTP_NOT_FOUND);
		}
	}
	
	private boolean fileIsDescendantOfOther(@NotNull File parent, @NotNull File child) {
		// Child must start with the parent path.
		return child.getPath().startsWith(parent.getPath());
	}
}
