package utilities;

import model.Identifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

public class ArrayHelpers {
	
	/**
	 * Returns a random element from the provided list.
	 *
	 * @param list The list of elements.
	 * @param <T> The type of list elements.
	 * @return A random element of the list.
	 */
	public static <T> T randomElementFromList(@NotNull List<T> list) {
		Random random = new Random();
		return list.get(
			random.nextInt(list.size())
		);
	}
	
	
	
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
	
	
	/**
	 * Finds the first element that matches the provided predicate in the provided list.
	 *
	 * @param list The list in which to search.
	 * @param condition The predicate which should be fulfilled.
	 * @param <T> The type of elements in the list.
	 * @return The first matching element, or <code>null</code> if no elements match.
	 */
	public static <T> @Nullable T firstElementThatMatches(
		@NotNull List<T> list,
		@NotNull Predicate<T> condition
	) {
		Optional<T> optional = list.stream()
			.filter(condition)
			.findFirst();
		
		return optional.orElse(null);
	}
	
	
	/**
	 * Finds the index of the first element that matches the provided predicate in the
	 * provided list.
	 *
	 * @param list The list in which to search.
	 * @param condition The predicate which should be fulfilled.
	 * @param <T> The type of elements in the list.
	 * @return The index of the first matching element, or <code>null</code> if no elements match.
	 */
	public static <T> @Nullable Integer firstIndexOfElementThatMatches(
		@NotNull List<T> list,
		@NotNull Predicate<T> condition
	) {
		Optional<T> optional = list.stream()
			.filter(condition)
			.findFirst();
		
		if (optional.isPresent()) {
			return list.indexOf(optional.get());
		}
		return null;
	}
	
	
	/**
	 * If an object with the same ID as the provided element exists in the provided list, then
	 * the version in the list is removed and replaced with the provided object. If no such
	 * object exists, then the given element is appended to the list.
	 *
	 * @param list The list to modify.
	 * @param element The element to insert.
	 * @param <S> The type of identifier that each element uses.
	 * @param <T> The type of element.
	 */
	public static <S, T extends Identifiable<S>> void updateElementInList(@NotNull List<T> list, @NotNull final T element) {
		Integer index = firstIndexOfElementThatMatches(list, e -> e.getId().equals(element.getId()));
		if (index == null) {
			list.add(element);
			return;
		}
		list.set(index, element);
	}
}
