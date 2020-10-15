package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GenderTest {
	private static Stream<Arguments> provideCasesForMatchingStringValues() {
		return Stream.of(
			Arguments.of(Gender.MALE, "m"),
			Arguments.of(Gender.FEMALE, "f")
		);
	}
	
	@ParameterizedTest
	@MethodSource("provideCasesForMatchingStringValues")
	void case_hasMatchingStringValue(Gender gender, String value) {
		assertEquals(gender.getValue(), value);
	}
	
	@ParameterizedTest
	@MethodSource("provideCasesForMatchingStringValues")
	void createsCasesFromStringValue(Gender expected, String value) {
		Gender result = Gender.fromValue(value);
		assertNotNull(result);
		assertEquals(result, expected);
	}
	
	@Test
	void returnsNullForInvalidStringValue() {
		Gender result = Gender.fromValue("nope");
		assertNull(result);
	}
}