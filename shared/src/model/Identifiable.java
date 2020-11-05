package model;

import org.jetbrains.annotations.NotNull;

/**
 * A class of types whose instances hold the value of an entity with stable identity.
 * @param <ID> A type representing the stable identity of the entity associated with an instance.
 */
public interface Identifiable<ID> {
	/**
	 * @return The stable identity of the entity associated with this instance.
	 */
	public @NotNull ID getId();
}
