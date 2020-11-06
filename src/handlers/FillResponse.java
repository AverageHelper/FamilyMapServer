package handlers;

import responses.MessageResponse;

public class FillResponse extends MessageResponse {
	public FillResponse(int personCount, int eventCount) {
		super("Successfully added " +
			personCount +
			" persons and " +
			eventCount +
			" events to the database.");
	}
}
