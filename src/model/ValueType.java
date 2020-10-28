package model;

import org.jetbrains.annotations.NotNull;

public interface ValueType<T> {
	@NotNull T getValue();
}
