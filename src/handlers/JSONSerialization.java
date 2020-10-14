package handlers;

import org.jetbrains.annotations.NotNull;
import com.google.gson.*;

/**
 * Objects that inherit from this type can be easily converted into a JSON string.
 *
 * This class also includes a static <code>fromJson</code> helper to instantiate objects of arbitrary types from JSON data.
 */
public class JSONSerialization {
	/**
	 * @return A JSON string representing the object.
	 */
	public @NotNull String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	/**
	 * Creates an instance of <code>T</code> using the given JSON string.
	 *
	 * @param jsonString The JSON string to parse.
	 * @param typeOfT The type of object to instantiate.
	 * @param <T> The type of object that will be instantiated.
	 * @return A new instance of the given object type initialized with the values specified by the JSON fields.
	 */
	public static <T> @NotNull T fromJson(@NotNull String jsonString, @NotNull Class<T> typeOfT) throws JsonSyntaxException {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, typeOfT);
	}
}
