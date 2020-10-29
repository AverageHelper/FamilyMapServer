package utilities;

import java.util.List;

public class ArrayHelpers {
	/**
	 * @param list The list in which to search.
	 * @param key The identifiable object to check against.
	 * @param <S> The identifier type.
	 * @param <T> The identifiable object.
	 * @return <code>true</code> if a semantically similar object appears in the <code>list</code>.
	 */
	public static <S, T extends Identifiable<S>> boolean containsObjectWithSameId(
		final List<T> list,
		final T key
	) {
		return list.stream().anyMatch(o -> o.getId().equals(key.getId()));
	}
}
