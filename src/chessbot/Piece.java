package chessbot;

import java.util.*;

//General
public abstract class Piece {
	
	//Variables to determine position on the board
	Point position = new Point(0,0);
	
	//Value of the piece; can potentially change depending on context
	int worth;
	String symbol;
	
	//Variable indicates whether or not the piece belongs to the player
	Boolean player = false;
	
	//Abstract methods
	public abstract List<Move> findMoves(Board b);
	
	public int getWorth(){
		return worth;
	}
	
	//Common methods
	public void setPosition(int x, int y){
		position.setXY(x, y);
	}
	
	public int getX(){
		return position.x;
	}
	
	public int getY(){
		return position.y;
	}
	
	@Override
	public String toString() {
		return player ? symbol.toUpperCase() : symbol; 
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (getClass() != obj.getClass()) {
	        return false;
	    }
	    final Piece other = (Piece) obj;
	    if (this.position.equals(other.position) && this.symbol.equals(other.symbol) && this.player == other.player){
	    	return true;
	    }else{
	    	return false;
	    }
	}
	
}
