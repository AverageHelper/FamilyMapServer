package model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import server.Server;

import java.lang.reflect.Type;

public class EventTypeDeserializer implements JsonDeserializer<EventType> {
	@Override
	public EventType deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		String value = jsonElement.getAsString();
		EventType res = EventType.fromValue(value);
		if (res == null) {
			Server.logger.warning("Got incorrect value '" + value + "'");
		} else {
			Server.logger.finest("Got value '" + value + "'");
		}
		return res;
	}
}
