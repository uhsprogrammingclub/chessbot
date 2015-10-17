package chessbot;

import java.util.ArrayList;

//General
public abstract class Pieces {
	
	//Variables to determine position on the board
	int x;
	int y;
	
	//Value of the piece; can potentially change depending on context
	int worth;
	String symbol;
	
	//Variable indicates whether or not the piece belongs to the player
	Boolean isPlayer;
	
	//Abstract methods
	public abstract ArrayList<int[]> findMoves(Board b);
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
