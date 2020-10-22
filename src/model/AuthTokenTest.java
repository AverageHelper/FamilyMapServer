package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AuthTokenTest {
	
	private AuthToken authToken;
	
	@BeforeEach
	void setUp() {
		authToken = new AuthToken(
			"token_id",
			"token_user",
			new Date(42),
			true
		);
	}
	
	@Test
	void getId_shouldReturnTheInitialId() {
		assertEquals("token_id", authToken.getId());
	}
	
	@ParameterizedTest
	@NullSource
	@EmptySource
	void testInit_shouldFailToSetEmptyId(String newId) {
		assertThrows(
			IllegalArgumentException.class,
			() -> new AuthToken(
				newId,
				authToken.getAssociatedUsername(),
				authToken.getCreatedAt(),
				authToken.isValid()
			)
		);
	}
	
	@ParameterizedTest
	@NullSource
	@EmptySource
	void testInit_shouldFailToSetEmptyUserId(String newId) {
		assertThrows(
			IllegalArgumentException.class,
			() -> new AuthToken(
				authToken.getId(),
				newId,
				authToken.getCreatedAt(),
				authToken.isValid()
			)
		);
	}
	
	@Test
	void getUserId_shouldReturnTheInitialAssocUsername() {
		assertEquals("token_user", authToken.getAssociatedUsername());
	}
	
	@Test
	void getCreatedAt_shouldReturnTheInitialCreationTimestamp() {
		assertEquals(42, authToken.getCreatedAt().getTime());
	}
	
	@Test
	void isValid_shouldReturnTheInitialValidState() {
		assertTrue(authToken.isValid());
	}
}