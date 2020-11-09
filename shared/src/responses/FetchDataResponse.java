package responses;

import transport.JSONSerialization;
import transport.MissingKeyException;

public class FetchDataResponse extends JSONSerialization {
	private final boolean success = true;
	
	public boolean isSuccessful() {
		return success;
	}
	
	@Override
	public void assertCorrectDeserialization() throws MissingKeyException {}
}
