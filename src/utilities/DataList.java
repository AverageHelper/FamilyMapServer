package utilities;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DataList<T> {
	private final @NotNull List<T> data;
	
	public DataList(@NotNull List<T> data) {
		this.data = data;
	}
	
	public @NotNull List<T> getData() {
		return data;
	}
}
