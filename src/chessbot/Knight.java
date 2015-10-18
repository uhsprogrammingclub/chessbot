package chessbot;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
	@Override
	//findMoves() method which identifies possible moves
	//The Knight Piece can move ...
	public List<Point> findMoves(Board b){
		
		List<Point> moves = new ArrayList<Point>();
		
		return moves;
	}
	
	//Constructor
	public Knight(int x, int y, boolean player){
		
		//Setting base values for the Queen piece
		worth = 3;
		isPlayer = player;
		symbol = "n";
		
		//Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
