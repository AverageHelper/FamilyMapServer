package utilities;

/**
 * An object that holds exactly two objects.
 */
public class Pair<First, Second> {
	private First first;
	private Second second;
	
	public Pair() {
		this.first = null;
		this.second = null;
	}
	
	public Pair(First first, Second second) {
		this.first = first;
		this.second = second;
	}
	
	public First getFirst() {
		return first;
	}
	
	public void setFirst(First first) {
		this.first = first;
	}
	
	public Second getSecond() {
		return second;
	}
	
	public void setSecond(Second second) {
		this.second = second;
	}
}
