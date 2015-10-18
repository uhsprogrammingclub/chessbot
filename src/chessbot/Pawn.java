package chessbot;

import java.util.ArrayList;

public class Pawn extends Piece {

	// Finds all moves for the Pawn piece
	// Looks directly in front, diagonally left, and diagonally right. Needs to include En Passant still.
	
	public ArrayList<Point> findMoves(Board b) {
		
		ArrayList<Point> moves = new ArrayList<Point>();
		
		//Create variable that is -1 if it is the player's piece, and 1 if it is the computer's piece
		int val = 1;
		if(isPlayer){
			val = -1;
		}
		
		
		// Checks the square directly in front of it
		moves.addAll(Utils.getVerticalMoves(b, this));

		// Check one diagonal
		/*if( b.squareExists(this.getX() + val, this.getY() + val)  && !b.isEmptySquare(this.getX() + val, this.getY() + val) && !b.getTeam(this.getX() + val, this.getY() + val)){
			Point move = new Point(this.getX() + val, this.getY() + val);
			moves.add(move);
		}
		
		// Check the other diagonal
		if( b.squareExists(this.getX() + val, this.getY() - val)  && !b.isEmptySquare(this.getX() + val, this.getY() - val) && !b.getTeam(this.getX() + val, this.getY() - val)){
			Point move = new Point(this.getX() + val, this.getY() - val);
			moves.add(move);
		}*/
		
		//Return moves
		return moves;
	}

	@Override
	// getWorth() method which returns the worth of the piece
	public int getWorth() {
		return worth;
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
