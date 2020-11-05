package handlers;

import transport.JSONSerialization;
import transport.MissingKeyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utilities.FileHelpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoadRequestTest {
	@Test
	void testSerialization_inputSameAsOutput() throws IOException {
		try {
			String rawRequest = FileHelpers.stringFromFile(
				new File("test/driver/passoffFiles/LoadData.json").getAbsoluteFile()
			);
			LoadRequest loadRequest = JSONSerialization.fromJson(rawRequest, LoadRequest.class);
			
			assertNotNull(loadRequest.getUsers());
			assertNotNull(loadRequest.getPersons());
			assertNotNull(loadRequest.getEvents());
			
			// I counted these up from the JSON file.
			assertEquals(2, loadRequest.getUsers().size());
			assertEquals(11, loadRequest.getPersons().size());
			assertEquals(19, loadRequest.getEvents().size());
			
			String serializedRequest = loadRequest.serialize();
			LoadRequest deserializedRequest = JSONSerialization.fromJson(serializedRequest, LoadRequest.class);
			assertEquals(loadRequest, deserializedRequest);
			
		} catch (FileNotFoundException fileNotFoundException) {
			Assertions.fail("passoffFiles/LoadData.json not found in project root directory");
		} catch (MissingKeyException e) {
			Assertions.fail("passoffFiles.LoadData.json was not suitable for testing");
		}
	}
}
