package utilities;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHelpers {
	
	public static final String UTILS_ROOT = "src/utilities/";
	
	public static @NotNull String stringFromFile(@NotNull File file) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null)
		{
			sb.append(line);
		}
		return sb.toString();
	}
	
	public static @NotNull List<String> stringsFromFile(@NotNull File file) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		List<String> list = new ArrayList<>();
		String line;
		while ((line = bufferedReader.readLine()) != null)
		{
			if (!line.isEmpty()) {
				list.add(line);
			}
		}
		return list;
	}
	
	public static @NotNull String randomStringFromFile(@NotNull String filename) throws IOException {
		List<String> names = stringsFromFile(new File(UTILS_ROOT + filename).getAbsoluteFile());
		return ArrayHelpers.randomElementFromList(names);
	}
}
