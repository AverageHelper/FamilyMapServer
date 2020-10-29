package utilities;

import model.Gender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Random;

public class NameGenerator {
	public static @NotNull String randomString(int targetStringLength) {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int)
				(random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		
		return buffer.toString();
	}
	
	public static final int OBJECT_ID_LENGTH = 32;
	public static @NotNull String newObjectIdentifier() {
		return randomString(OBJECT_ID_LENGTH);
	}
	
	public static final String FIRST_NAMES_MALE_FILE = "FirstNamesMale.txt";
	public static final String FIRST_NAMES_FEMALE_FILE = "FirstNamesFemale.txt";
	
	public static @NotNull String randomFirstName(@NotNull Gender gender) throws IOException {
		switch (gender) {
			case MALE:
				// Pick a male name
				return FileHelpers.randomStringFromFile(FIRST_NAMES_MALE_FILE);
				
			case FEMALE:
				// Pick a female name
				return FileHelpers.randomStringFromFile(FIRST_NAMES_FEMALE_FILE);
				
			default:
				// Pick a random gender and return that
				Random random = new Random();
				if (random.nextBoolean()) {
					return FileHelpers.randomStringFromFile(FIRST_NAMES_MALE_FILE);
				} else {
					return FileHelpers.randomStringFromFile(FIRST_NAMES_FEMALE_FILE);
				}
		}
	}
	
	public static final String LAST_NAMES_FILE = "LastNames.txt";
	public static @NotNull String randomLastName() throws IOException {
		return FileHelpers.randomStringFromFile(LAST_NAMES_FILE);
	}
}
