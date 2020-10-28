package handlers;

import model.ModelData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FetchMultipleItemsRequest<T extends ModelData> extends JSONSerialization {
	private final @NotNull List<T> data;
	private final boolean success;
	
	public FetchMultipleItemsRequest(@NotNull List<T> data) {
		this.data = data;
		this.success = true;
	}
	
	public @NotNull List<T> getData() {
		return data;
	}
	
	public boolean isSuccessful() {
		return success;
	}
}
