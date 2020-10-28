package model;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ValueSerializer implements JsonSerializer<ValueType<String>> {
	@Override
	public JsonElement serialize(ValueType<String> stringValueType, Type type, JsonSerializationContext jsonSerializationContext) {
		return new JsonPrimitive(stringValueType.getValue());
	}
}
