package services;

public class LoadResult {
	private final int userCount;
	private final int personCount;
	private final int eventCount;
	
	public LoadResult(int userCount, int personCount, int eventCount) {
		this.userCount = userCount;
		this.personCount = personCount;
		this.eventCount = eventCount;
	}
	
	public int getUserCount() {
		return userCount;
	}
	
	public int getPersonCount() {
		return personCount;
	}
	
	public int getEventCount() {
		return eventCount;
	}
}
