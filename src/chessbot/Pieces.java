package chessbot;

//General
public abstract class Pieces {
	
	//Variables to determine position on the board
	int x;
	int y;
	
	//Variable indicates whether or not the piece belongs to the player
	Boolean player;
	
	//Abstract methods
	public abstract int[][] findMoves();
	public abstract int getWorth();
	public abstract String getSymbol();
	
	//Common methods
	public void setPosition(int newX, int newY){
		x = newX;
		y = newY;
	}
	
	public int[] getPosition(){
		int[] position = {x, y};
		return position;
	}
	
}
