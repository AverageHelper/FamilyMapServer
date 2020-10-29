package server;

import com.sun.net.httpserver.HttpServer;
import handlers.*;
import handlers.FileHandler;

import java.io.IOException;
import java.lang.reflect.MalformedParametersException;
import java.net.InetSocketAddress;
import java.util.logging.*;

/**
 * The Family Map server.
 */
public class Server {
	
	private static final int MAX_WAITING_CONNECTIONS = 12;
	public static Logger logger;
	
	static {
		try {
			initLog();
		} catch (IOException e) {
			System.out.println("Could not initialize log: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static void initLog() throws IOException {
		// TODO: Get this value from the command line. Default to INFO.
		Level logLevel = Level.FINEST;
		
		logger = Logger.getLogger("self.familymap");
		logger.setLevel(logLevel);
		logger.setUseParentHandlers(false);
		
		java.util.logging.Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(logLevel);
		consoleHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(consoleHandler);
		
		java.util.logging.FileHandler fileHandler = new java.util.logging.FileHandler(
			"log.txt",
			false
		);
		fileHandler.setLevel(logLevel);
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
	}
	
	private void run(String portNumber) {
		logger.info("Starting FamilyMap");
		
		logger.info("Initializing HTTP Server on port " + portNumber);
		HttpServer server;
		try {
			server = HttpServer.create(
				new InetSocketAddress(Integer.parseInt(portNumber)),
				MAX_WAITING_CONNECTIONS
			);
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, e.getMessage(), e);
			return;
		}
		
		// Use the default executor
		server.setExecutor(null);
		
		logger.info("Creating contexts");
		
		// Users
		server.createContext("/user/register", new RegisterHandler());
		server.createContext("/user/login", new LoginHandler());
		
		// Delete everything
		server.createContext("/clear", new ClearHandler());
		
		// Add entries
		server.createContext("/fill", new FillHandler());
		server.createContext("/load", new LoadHandler());
		
		// Fetch entries
//		server.createContext("/person/[personId]", new FetchDataHandler());
		server.createContext("/person", new FetchDataHandler());
//		server.createContext("/event/[eventId]", new FetchDataHandler());
		server.createContext("/event", new FetchDataHandler());
		
		// Normal file requests
		server.createContext("/", new FileHandler());
		
		logger.info("Starting HTTP server");
		server.start();
		
		logger.info("Server started");
	}
	
	public static void main(String[] args) {
		if (args.length < 1) {
			throw new MalformedParametersException("Expected a single parameter (the port number to run on)");
		}
		
		String portNumber = args[0];
		
		new Server().run(portNumber);
	}
}
