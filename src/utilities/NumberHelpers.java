package utilities;

import java.util.Random;

public class NumberHelpers {
	/**
	 * Returns a number that lies on a range somewhere between <code>min</code> and
	 * <code>max</code> inclusive.
	 * @param min The upper bound of the range.
	 * @param max The lower bound of the range.
	 * @return A number that lies on a range somewhere between <code>min</code> and <code>max</code> inclusive.
	 */
	public static int randomNumberInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	/**
	 * Returns a number that lies on a range somewhere between <code>root - delta</code> and
	 * <code>root + delta</code> inclusive.
	 *
	 * @param root The center of the number range.
	 * @param delta The radius of the range.
	 * @return A number that lies on a range somewhere between <code>root - delta</code> and <code>root + delta</code>.
	 */
	public static int randomNumberAround(int root, int delta) {
		return randomNumberInRange(root - delta, root + delta);
	}
}
