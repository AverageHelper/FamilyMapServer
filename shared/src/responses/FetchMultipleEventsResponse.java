package responses;

import model.Event;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FetchMultipleEventsResponse extends FetchMultipleItemsResponse<Event> {
	public FetchMultipleEventsResponse(@NotNull List<Event> data) {
		super(data);
	}
}
