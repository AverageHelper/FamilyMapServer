package model;

import org.jetbrains.annotations.NotNull;

/**
 * An authorization token. A valid authorization token grants access to a user's data.
 */
public class AuthToken {
	private @NotNull String id;
	private @NotNull String userId;
	private int createdAt;
	private boolean isValid;
	
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
	
	public void setId(@NotNull String id) {
		this.id=id;
	}
	
	public @NotNull String getUserId() {
		return userId;
	}
	
	public void setUserId(@NotNull String userId) {
		this.userId=userId;
	}
	
	public int getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(int createdAt) {
		this.createdAt=createdAt;
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public void setValid(boolean valid) {
		isValid=valid;
	}
}
