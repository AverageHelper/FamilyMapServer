package dao;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TableNameTest {
	private static Stream<Arguments> provideCasesForMatchingStringValues() {
		return Stream.of(
			Arguments.of(TableName.AUTH_TOKEN, "AuthToken"),
			Arguments.of(TableName.EVENT, "Event"),
			Arguments.of(TableName.PERSON, "Person"),
			Arguments.of(TableName.USER, "User")
		);
	}
	
	@ParameterizedTest
	@MethodSource("provideCasesForMatchingStringValues")
	void case_hasMatchingStringValue(TableName event, String value) {
		assertEquals(event.getValue(), value);
	}
}