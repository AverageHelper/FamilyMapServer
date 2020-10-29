package handlers;

import model.*;
import org.jetbrains.annotations.NotNull;
import com.google.gson.*;

/**
 * Objects that inherit from this type can be easily converted into a JSON string.
 *
 * This class also includes a static <code>fromJson</code> helper to instantiate objects of arbitrary types from JSON data.
 */
public abstract class JSONSerialization implements HTTPSerialization {
	private static @NotNull Gson getGson() {
		return new GsonBuilder()
			.registerTypeAdapter(Gender.class, new GenderCoder())
			.serializeNulls()
			.create();
	}
	
	/**
	 * @return A JSON string representing the object.
	 */
	public @NotNull String toJson() {
		Gson gson = getGson();
		return gson.toJson(this);
	}
	
	@Override
	public @NotNull String serialize() {
		return toJson();
	}
	
	@Override
	public @NotNull String contentType() {
		return "application/json; charset=UTF-8";
	}
	
	/**
	 * Creates an instance of <code>T</code> using the given JSON string.
	 *
	 * @param jsonString The JSON string to parse.
	 * @param typeOfT The type of object to instantiate.
	 * @param <T> The type of object that will be instantiated.
	 * @return A new instance of the given object type initialized with the values specified by the JSON fields.
	 */
	public static <T extends JSONSerialization> @NotNull T fromJson(
		@NotNull String jsonString,
		@NotNull Class<T> typeOfT
	) throws JsonParseException, MissingKeyException {
		if (jsonString.isEmpty()) {
			throw new JsonSyntaxException("Cannot parse an empty payload");
		}
		Gson gson = getGson();
		T result = gson.fromJson(jsonString, typeOfT);
		result.assertCorrectDeserialization();
		return result;
	}
	
	/**
	 * Called by <code>JSONSerialization</code> after the object is initialized by deserializing a
	 * JSON string. Throw an exception if any of the instance's values are <code>null</code> where
	 * they should not be.
	 */
	public abstract void assertCorrectDeserialization() throws MissingKeyException;
}
