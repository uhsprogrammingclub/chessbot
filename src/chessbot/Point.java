package chessbot;

public class Point {
	int x;
	int y;
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	void setXY(int x, int y){
		// change point coordinates
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		//Convert to readable position
		String[] letters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		return letters[x] + "" + (y+1);
	}
	
}
