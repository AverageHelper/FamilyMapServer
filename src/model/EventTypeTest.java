package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class EventTypeTest {
	private static Stream<Arguments> provideCasesForMatchingStringValues() {
		return Stream.of(
			Arguments.of(EventType.BAPTISM, "baptism"),
			Arguments.of(EventType.BIRTH, "birth"),
			Arguments.of(EventType.CHRISTENING, "christening"),
			Arguments.of(EventType.MARRIAGE, "marriage"),
			Arguments.of(EventType.DEATH, "death")
		);
	}
	
	@ParameterizedTest
	@MethodSource("provideCasesForMatchingStringValues")
	void case_hasMatchingStringValue(EventType event, String value) {
		assertEquals(event.getValue(), value);
	}
	
	@ParameterizedTest
	@MethodSource("provideCasesForMatchingStringValues")
	void createsCasesFromStringValue(EventType expected, String value) {
		EventType result = EventType.fromValue(value);
		assertNotNull(result);
		assertEquals(result, expected);
	}
	
	@Test
	void returnsNullForInvalidStringValue() {
		EventType result = EventType.fromValue("nope");
		assertNull(result);
	}
}