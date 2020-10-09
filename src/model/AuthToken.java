package model;

import org.jetbrains.annotations.NotNull;

/**
 * An authorization token. A valid authorization token grants access to a user's data.
 */
public class AuthToken implements ModelData {
	private @NotNull String id;
	private @NotNull String userId;
	private int createdAt;
	private boolean isValid;
	
	/**
	 * Creates an <code>AuthToken</code> object.
	 * @param id The token string.
	 * @param userId The unique ID of the user for whom this token permits access.
	 * @param createdAt The time of the token's creation in seconds from the reference date.
	 * @param isValid Whether the token is valid.
	 */
	public AuthToken(
		@NotNull String id,
		@NotNull String userId,
		int createdAt,
		boolean isValid
	) {
		this.id = id;
		this.userId = userId;
		this.createdAt = createdAt;
		this.isValid = isValid;
	}
	
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
	
	public @NotNull String getUserId() {
		return userId;
	}
	
	/**
	 * Sets a new <code>userId</code> on the auth token.
	 * @param userId The new user ID.
	 * @throws IllegalArgumentException If <code>userId</code> is empty.
	 */
	public void setUserId(@NotNull String userId) {
		if (userId.isEmpty()) {
			throw new IllegalArgumentException("userId parameter must not be empty");
		}
		this.userId = userId;
	}
	
	public int getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(int createdAt) {
		this.createdAt = createdAt;
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public void setValid(boolean valid) {
		isValid = valid;
	}
}
