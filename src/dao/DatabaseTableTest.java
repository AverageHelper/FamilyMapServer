package dao;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTableTest {
	private static Stream<Arguments> provideCasesForMatchingStringValues() {
		return Stream.of(
			Arguments.of(DatabaseTable.AUTH_TOKEN, "AuthToken", "id"),
			Arguments.of(DatabaseTable.EVENT, "Event", "id"),
			Arguments.of(DatabaseTable.PERSON, "Person", "id"),
			Arguments.of(DatabaseTable.USER, "User", "userName")
		);
	}
	
	@ParameterizedTest
	@MethodSource("provideCasesForMatchingStringValues")
	void case_hasMatchingTitleValue(DatabaseTable event, String value) {
		assertEquals(value, event.getName());
	}
	
	@ParameterizedTest
	@MethodSource("provideCasesForMatchingStringValues")
	void case_hasMatchingPrimaryKeyValue(DatabaseTable event, String value, String primaryKey) {
		assertEquals(primaryKey, event.getPrimaryKey());
	}
}