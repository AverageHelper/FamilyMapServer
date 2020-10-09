package server;

import handlers.*;

/**
 * The Family Map server.
 */
public class Server {
	public static void main(String[] args) {
		// Instantiate handlers
		RegisterHandler registerHandler = new RegisterHandler();
		LoginHandler loginHandler = new LoginHandler();
		ClearHandler clearHandler = new ClearHandler();
		LoadHandler loadHandler = new LoadHandler();
		FillHandler fillHandler = new FillHandler();
		FetchDataHandler fetchDataHandler = new FetchDataHandler();
		
		// HTTP stuff?
		
		// Get the request path
		// Figure out the appropriate handler
		// Make sure we've got the right parameters
	}
}
