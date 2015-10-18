package chessbot;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece{

	@Override
	//findMoves() method which identifies possible moves
	//The King Piece can move vertically, horizontally, or diagonally 1 square
	public List<Move> findMoves(Board b){
		
		List<Move> moves = new ArrayList<Move>();
		
		// Gets vertical moves
		moves.addAll(Utils.getVerticalMoves(b, this));
		//Get Horizontal moves
		moves.addAll(Utils.getHorizontalMoves(b, this));
		//Get Diagonal moves
		moves.addAll(Utils.getDiagonalMoves(b, this));
		
		return moves;
	}
	
	//Constructor
	public King(int x, int y, boolean player){
		
		//Setting base values for the Queen piece
		worth = 10000;
		isPlayer = player;
		symbol = "k";
		
		//Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
