package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class AuthTokenTest {
	
	private AuthToken authToken;
	
	@BeforeEach
	void setUp() {
		authToken = new AuthToken("token_id", "token_userId", 42, true);
	}
	
	@Test
	void getId_shouldReturnTheInitialId() {
		assertEquals(authToken.getId(), "token_id");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"new_id", "token_id", "CAPS", "$@%*"})
	void setId_shouldChangeStoredID(String newId) {
		authToken.setId(newId);
		assertEquals(authToken.getId(), newId);
	}
	
	@ParameterizedTest
	@NullSource
	@EmptySource
	void setId_shouldFailToSetEmptyId(String newId) {
		assertThrows(IllegalArgumentException.class, () -> authToken.setId(newId));
	}
	
	@Test
	void getUserId_shouldReturnTheInitialUserId() {
		assertEquals(authToken.getUserId(), "token_userId");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"new_userId", "token_userId", "CAPS", "$@%*"})
	void setUserId_shouldChangeTheStoredUserId(String newId) {
		authToken.setUserId(newId);
		assertEquals(authToken.getUserId(), newId);
	}
	
	@ParameterizedTest
	@NullSource
	@EmptySource
	void setUserId_shouldFailToSetEmptyUserId(String newId) {
		assertThrows(IllegalArgumentException.class, () -> authToken.setUserId(newId));
	}
	
	@Test
	void getCreatedAt_shouldReturnTheInitialCreationTimestamp() {
		assertEquals(authToken.getCreatedAt(), 42);
	}
	
	@ParameterizedTest
	@ValueSource(ints = {0, -50, 120, Integer.MAX_VALUE, Integer.MIN_VALUE})
	void setCreatedAt_shouldChangeTheCreationTimestamp(int timestamp) {
		authToken.setCreatedAt(timestamp);
		assertEquals(authToken.getCreatedAt(), timestamp);
	}
	
	@Test
	void isValid_shouldReturnTheInitialValidState() {
		assertTrue(authToken.isValid());
	}
	
	@Test
	void setValid_shouldChangeTheStoredValidState() {
		authToken.setValid(false);
		assertFalse(authToken.isValid());
		authToken.setValid(true);
		assertTrue(authToken.isValid());
	}
}