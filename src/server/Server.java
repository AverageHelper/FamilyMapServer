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
		
		// Set up run loop
	}
}
