package chessbot;

import java.util.*;

//Methods for the Queen Piece
public class Queen extends Piece {

	@Override
	// findMoves() method which identifies possible moves
	// The Queen Piece can move vertically, horizontally, or diagonally as far
	// as it can

	public List<Move> findMoves(Board b) {

		List<Move> moves = new ArrayList<Move>();

		// Gets vertical moves
		moves.addAll(Utils.getVerticalMoves(b, this));
		// Get Horizontal moves
		moves.addAll(Utils.getHorizontalMoves(b, this));
		// Get Diagonal moves
		moves.addAll(Utils.getDiagonalMoves(b, this));

		return moves;
	}

	// Constructor
	public Queen(int x, int y, boolean p) {

		// Setting base values for the Queen piece
		worth = 900;
		player = p;
		symbol = "q";

		// Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
