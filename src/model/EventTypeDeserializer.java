package model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class EventTypeDeserializer implements JsonDeserializer<EventType> {
	@Override
	public EventType deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		String value = jsonElement.getAsString();
		return EventType.fromValue(value);
	}
}
