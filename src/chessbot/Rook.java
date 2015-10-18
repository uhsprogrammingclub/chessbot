package chessbot;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece{

	@Override
	//findMoves() method which identifies possible moves
	//The Rook Piece can move vertically, or horizontally as far as it can
	public List<Point> findMoves(Board b){
		
		List<Point> moves = new ArrayList<Point>();
		
		// Gets vertical moves
		moves.addAll(Utils.getVerticalMoves(b, this));
		//Get Horizontal moves
		moves.addAll(Utils.getHorizontalMoves(b, this));
		
		return moves;
	}
	
	//Constructor
	public Rook(int x, int y, boolean player){
		
		//Setting base values for the Queen piece
		worth = 5;
		isPlayer = player;
		symbol = "r";
		
		//Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
