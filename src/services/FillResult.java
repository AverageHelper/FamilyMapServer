package services;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utilities.Pair;

/**
 * The result of a successful fill operation.
 */
public class FillResult {
	private final @Nullable Pair<Integer, Integer> counts;
	private final @Nullable FillFailureReason failureReason;
	
	/**
	 * Creates a new <code>FillResult</code> object.
	 * @param personCount The number of new <code>Person</code> entries created.
	 * @param eventCount The number of new <code>Event</code> entries created.
	 */
	public FillResult(int personCount, int eventCount) {
		this.counts = new Pair<>(personCount, eventCount);
		this.failureReason = null;
	}
	
	/**
	 * Creates a new <code>FillResult</code> object.
	 * @param reason The reason the fill operation failed.
	 */
	public FillResult(@NotNull FillFailureReason reason) {
		this.counts = null;
		this.failureReason = reason;
	}
	
	public @Nullable Integer getPersonCount() {
		if (counts == null) {
			return null;
		}
		return counts.getFirst();
	}
	
	public @Nullable Integer getEventCount() {
		if (counts == null) {
			return null;
		}
		return counts.getSecond();
	}
	
	public @Nullable FillFailureReason getFailureReason() {
		return failureReason;
	}
}
