package chessbot;

public class Point {

	int x = 8; // out of bounds
	int y = 8; // out of bounds

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(String loc) {
		if (loc.length() != 2)
			return;

		char letter = loc.toUpperCase().charAt(0);
		char number = loc.charAt(1);

		x = (int) letter - 65;
		y = Character.getNumericValue(number) - 1;
	}

	void setXY(int x, int y) {
		// change point coordinates
		this.x = x;
		this.y = y;
	}

	// Function that determines whether a square exists
	boolean squareExists() {

		if (x >= 0 && x < 8 && y >= 0 && y < 8) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public String toString() {
		// Convert to readable position
		String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z" };
		return letters[x] + "" + (y + 1);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Point other = (Point) obj;
		if (this.x == other.x && this.y == other.y) {
			return true;
		} else {
			return false;
		}
	}

}
