package handlers;

import responses.MessageResponse;

public class LoadResponse extends MessageResponse {
	public LoadResponse(int userCount, int personCount, int eventCount) {
		// Successfully added X users, Y persons, and Z events to the database.
		super("Successfully added " +
			userCount +
			" users, " +
			personCount +
			" persons, and " +
			eventCount +
			" events to the database.");
	}
}
