package chessbot;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

	@Override
	// findMoves() method which identifies possible moves
	// The King Piece can move vertically, horizontally, or diagonally 1 square
	public List<Move> findMoves(Board b) {

		List<Move> moves = new ArrayList<Move>();

		// Get castling moves
		if(b.canCastleKSide(this)){
			moves.add(new Move(b, new Point(this.getX() + 2, this.getY()), this, null));
		}
		if(b.canCastleQSide(this)){
			moves.add(new Move(b, new Point(this.getX() - 2, this.getY()), this, null));
		}
		// Gets vertical moves
		moves.addAll(Utils.getVerticalMoves(b, this));
		// Get Horizontal moves
		moves.addAll(Utils.getHorizontalMoves(b, this));
		// Get Diagonal moves
		moves.addAll(Utils.getDiagonalMoves(b, this));
		

		return moves;
	}

	// Constructor
	public King(int x, int y, boolean p) {

		// Setting base values for the Queen piece
		worth = 10000;
		player = p;
		symbol = "k";

		// Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
