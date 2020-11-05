package database;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class FileHelpers {
	
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
	
}
