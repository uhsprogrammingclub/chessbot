package chessbot;

import java.util.*;

public class Knight extends Piece {
	@Override
	// findMoves() method which identifies possible moves
	// The Knight Piece can move ...
	public List<Move> findMoves(Board b) {

		List<Move> moves = Utils.getKnightMoves(b, this);

		return moves;
	}

	// Constructor
	public Knight(int x, int y, boolean p) {

		// Setting base values for the Queen piece
		worth = 3;
		player = p;
		symbol = "n";

		// Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
