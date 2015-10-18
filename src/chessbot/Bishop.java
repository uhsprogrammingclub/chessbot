package chessbot;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

	@Override
	//findMoves() method which identifies possible moves
	//The Bishop Piece can move diagonally as far as it can
	public List<Move> findMoves(Board b){
		
		List<Move> moves = new ArrayList<Move>();
		
		//Get Diagonal moves
		moves.addAll(Utils.getDiagonalMoves(b, this));
		
		return moves;
	}
	
	//Constructor
	public Bishop(int x, int y, boolean p){
		
		//Setting base values for the Queen piece
		worth = 3;
		player = p;
		symbol = "b";
		
		//Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
