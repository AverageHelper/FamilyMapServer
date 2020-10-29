package model;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GenderCoder implements JsonDeserializer<Gender>, JsonSerializer<Gender> {
	
	@Override
	public Gender deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		String value = jsonElement.getAsString();
		return Gender.fromValue(value);
	}
	
	@Override
	public JsonElement serialize(Gender gender, Type type, JsonSerializationContext jsonSerializationContext) {
		return new JsonPrimitive(gender.getValue());
	}
	
}
