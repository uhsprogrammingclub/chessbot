package chessbot;

import java.util.*;

public class Pawn extends Piece {

	// Finds all moves for the Pawn piece
	// Looks directly in front, diagonally left, and diagonally right. Needs to include En Passant still.
	
	public List<Move> findMoves(Board b) {
		
		List<Move> moves = new ArrayList<Move>();

		
		// Checks the square directly in front of it
		moves.addAll(Utils.getVerticalMoves(b, this));
		
		//Check diagonal
		moves.addAll(Utils.getDiagonalMoves(b, this));

		//Return moves
		return moves;
	}

	// Constructor
	public Pawn(int x, int y, boolean player) {

		// Setting base values for the Pawn piece
		worth = 1;
		isPlayer = player;
		symbol = "p";

		// Using accessory method for clarity; not strictly necessary
		setPosition(x, y);
	}
}
