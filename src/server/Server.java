package server;

import handlers.ClearHandler;
import handlers.FillHandler;
import handlers.LoginHandler;
import handlers.RegisterHandler;

/**
 * The Family Map server.
 */
public class Server {
	public static void main(String[] args) {
		// Instantiate handlers
		LoginHandler loginHandler = new LoginHandler();
		ClearHandler clearHandler = new ClearHandler();
		FillHandler fillHandler = new FillHandler();
		RegisterHandler registerHandler = new RegisterHandler();
		
		// Set up run loop
	}
}
