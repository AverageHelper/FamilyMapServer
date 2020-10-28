package model;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GenderDeserializer implements JsonDeserializer<Gender> {
	@Override
	public Gender deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		String value = jsonElement.getAsString();
		return Gender.fromValue(value);
	}
}
