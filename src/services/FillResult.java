package services;

/**
 * The result of a successful fill operation.
 */
public class FillResult {
	private final int personCount;
	private final int eventCount;
	
	/**
	 * Creates a new <code>FillResult</code> object.
	 * @param personCount The number of new <code>Person</code> entries created.
	 * @param eventCount The number of new <code>Event</code> entries created.
	 */
	public FillResult(int personCount, int eventCount) {
		this.personCount = personCount;
		this.eventCount = eventCount;
	}
	
	public int getPersonCount() {
		return personCount;
	}
	
	public int getEventCount() {
		return eventCount;
	}
}
