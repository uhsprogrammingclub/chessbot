package chessbot;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

	@Override
	//findMoves() method which identifies possible moves
	//The Bishop Piece can move diagonally as far as it can
	public List<Point> findMoves(Board b){
		
		List<Point> moves = new ArrayList<Point>();
		
		//Get Diagonal moves
		moves.addAll(Utils.getDiagonalMoves(b, this));
		
		return moves;
	}
	
	//Constructor
	public Bishop(int x, int y, boolean player){
		
		//Setting base values for the Queen piece
		worth = 3;
		isPlayer = player;
		symbol = "b";
		
		//Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
