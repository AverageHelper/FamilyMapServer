package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * An object that handles multiple clear-then-load requests.
 */
public class LoadHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// Construct and return the HTTP response
	}
}
