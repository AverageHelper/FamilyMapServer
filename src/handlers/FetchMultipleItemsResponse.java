package handlers;

import transport.MissingKeyException;
import model.ModelData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FetchMultipleItemsResponse<T extends ModelData> extends FetchDataResponse {
	private final @NotNull List<T> data;
	
	public FetchMultipleItemsResponse(@NotNull List<T> data) {
		this.data = data;
	}
	
	public @NotNull List<T> getData() {
		return data;
	}
	
	@SuppressWarnings("ConstantConditions")
	@Override
	public void assertCorrectDeserialization() throws MissingKeyException {
		if (this.data == null) {
			throw new MissingKeyException("data");
		}
	}
}
