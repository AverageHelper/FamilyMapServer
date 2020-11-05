package server;

import com.sun.net.httpserver.HttpServer;
import dao.DatabaseTable;
import database.Database;
import handlers.*;
import handlers.FileHandler;
import org.jetbrains.annotations.Nullable;

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
			initLog(null);
		} catch (Throwable e) {
			System.out.println("Failed to initialize logger: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static void initLog(@Nullable Level logLevel) throws IOException {
		if (logLevel == null) {
			logLevel = Level.INFO;
		}
		System.out.println("Logging with level " + logLevel.getName());
		
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
	
	public static void main(String[] args) {
		if (args.length < 1) {
			throw new MalformedParametersException("Usage: java -jar Server.jar <port> [log level]");
		}
		
		Level logLevel = null;
		if (args.length > 1) {
			String levelString = args[1].toUpperCase();
			try {
				logLevel = Level.parse(levelString);
			} catch (Exception e) {
				System.out.println("Could not parse log level '" + levelString + "': " + e.getMessage());
			}
		}
		try {
			initLog(logLevel);
		} catch (IOException e) {
			System.out.println("Could not initialize log: " + e.getMessage());
			e.printStackTrace();
		}
		
		String portNumber = args[0];
		
		new Server().run(portNumber);
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
		
		logger.info("Starting database");
		Database<DatabaseTable> db = new Database<>(DatabaseTable.values());
		db.setLogger(logger);
		
		logger.info("Creating contexts");
		
		// Users
		server.createContext("/user/register", new RegisterHandler(db));
		server.createContext("/user/login", new LoginHandler(db));
		
		// Delete everything
		server.createContext("/clear", new ClearHandler(db));
		
		// Add entries
		server.createContext("/fill", new FillHandler(db));
		server.createContext("/load", new LoadHandler(db));
		
		// Fetch entries
		server.createContext("/person", new FetchDataHandler(db));
		server.createContext("/event", new FetchDataHandler(db));
		
		// Normal file requests
		server.createContext("/", new FileHandler());
		
		logger.info("Starting HTTP server");
		server.start();
		
		logger.info("Server started");
	}
}
