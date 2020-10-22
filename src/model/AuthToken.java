package model;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

/**
 * An authorization token. A valid authorization token grants access to a user's data.
 */
public class AuthToken implements ModelData {
	private final @NotNull String id;
	private final @NotNull String associatedUsername;
	private final @NotNull Date createdAt;
	private final boolean isValid;
	
	/**
	 * Creates an <code>AuthToken</code> object.
	 * @param id The token string.
	 * @param associatedUsername The unique ID of the user for whom this token permits access.
	 * @param createdAt The time of the token's creation.
	 * @param isValid Whether the token is valid.
	 */
	public AuthToken(
		@NotNull String id,
		@NotNull String associatedUsername,
		@NotNull Date createdAt,
		boolean isValid
	) {
		if (id.isEmpty()) {
			throw new IllegalArgumentException("'id' parameter must not be empty");
		}
		if (associatedUsername.isEmpty()) {
			throw new IllegalArgumentException("userId parameter must not be empty");
		}
		this.id = id;
		this.associatedUsername = associatedUsername;
		this.createdAt = createdAt;
		this.isValid = isValid;
	}
	
	@Override
	public @NotNull String getId() {
		return id;
	}
	
	public @NotNull String getAssociatedUsername() {
		return associatedUsername;
	}
	
	public @NotNull Date getCreatedAt() {
		return createdAt;
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AuthToken authToken = (AuthToken) o;
		return getCreatedAt().equals(authToken.getCreatedAt()) &&
			isValid() == authToken.isValid() &&
			getId().equals(authToken.getId()) &&
			getAssociatedUsername().equals(authToken.getAssociatedUsername());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getId(), getAssociatedUsername(), getCreatedAt(), isValid());
	}
	
	@Override
	public String toString() {
		return "AuthToken{" +
			"id='" + id + '\'' +
			", associatedUsername='" + associatedUsername + '\'' +
			", createdAt=" + createdAt +
			", isValid=" + isValid +
			'}';
	}
}
