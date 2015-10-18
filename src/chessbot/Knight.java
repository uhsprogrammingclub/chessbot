package chessbot;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
	@Override
	//findMoves() method which identifies possible moves
	//The Knight Piece can move ...
	public List<Move> findMoves(Board b){
		
		List<Move> moves = new ArrayList<Move>();
		
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
