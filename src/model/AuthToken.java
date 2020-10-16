package model;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

/**
 * An authorization token. A valid authorization token grants access to a user's data.
 */
public class AuthToken implements ModelData {
	private @NotNull String id;
	private @NotNull String associatedUsername;
	private @NotNull Date createdAt;
	private boolean isValid;
	
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
		this.id = id;
		this.associatedUsername = associatedUsername;
		this.createdAt = createdAt;
		this.isValid = isValid;
	}
	
	@Override
	public @NotNull String getId() {
		return id;
	}
	
	/**
	 * Sets a new <code>id</code> on the auth token.
	 * @param id The new ID.
	 * @throws IllegalArgumentException If <code>id</code> is empty.
	 */
	public void setId(@NotNull String id) {
		if (id.isEmpty()) {
			throw new IllegalArgumentException("id parameter must not be empty");
		}
		this.id = id;
	}
	
	public @NotNull String getAssociatedUsername() {
		return associatedUsername;
	}
	
	/**
	 * Sets a new <code>userId</code> on the auth token.
	 * @param associatedUsername The new user ID.
	 * @throws IllegalArgumentException If <code>userId</code> is empty.
	 */
	public void setAssociatedUsername(@NotNull String associatedUsername) {
		if (associatedUsername.isEmpty()) {
			throw new IllegalArgumentException("userId parameter must not be empty");
		}
		this.associatedUsername = associatedUsername;
	}
	
	public @NotNull Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(@NotNull Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public void setValid(boolean valid) {
		isValid = valid;
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
