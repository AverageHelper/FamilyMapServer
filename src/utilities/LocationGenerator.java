package utilities;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class LocationGenerator {
	
	private static final String LOCATION_FILE_NAME = "locations.json";
	
	private static @Nullable LocationsList locationsCache = null;
	
	public static @NotNull LocationsList sampleLocations() throws IOException {
		if (locationsCache == null) {
			String root = FileHelpers.UTILS_ROOT;
			String locationsJson = FileHelpers.stringFromFile(
				new File(root + LOCATION_FILE_NAME).getAbsoluteFile()
			);
			
			Gson gson=new Gson();
			locationsCache = gson.fromJson(locationsJson, LocationsList.class);
		}
		
		return locationsCache;
	}
	
	public static @NotNull Location randomLocation() throws IOException {
		return ArrayHelpers.randomElementFromList(sampleLocations().getData());
	}
}
