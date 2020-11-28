package responses;

import model.Person;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FetchMultiplePersonsResponse extends FetchMultipleItemsResponse<Person> {
	public FetchMultiplePersonsResponse(@NotNull List<Person> data) {
		super(data);
	}
}
