package chessbot;

import java.util.ArrayList;

public class Pawn extends Piece {

	// Finds all moves for the Pawn piece
	// Looks directly in front, diagonally left, and diagonally right. Needs to include En Passant still.
	
	public ArrayList<int[]> findMoves(Board b) {
		
		ArrayList<int[]> moves = new ArrayList<int[]>();
		
		//Create variable that is -1 if it is the player's piece, and 1 if it is the computer's piece
		int val = 1;
		if(isPlayer){
			val = -1;
		}
		
		
		// Checks the square directly in front of it
		if( b.squareExists(this.x + val, this.y) && b.isEmptySquare(this.x + val, this.y)){
			int[] move = {this.x + val, this.y};
			moves.add(move);
		}

		// Check one diagonal
		if( b.squareExists(this.x + val, this.y + val)  && !b.isEmptySquare(this.x + val, this.y + val) && !b.getTeam(this.x + val, this.y + val)){
			int[] move = {this.x + val, this.y + val};
			moves.add(move);
		}
		
		// Check the other diagonal
		if( b.squareExists(this.x + val, this.y - val)  && !b.isEmptySquare(this.x + val, this.y - val) && !b.getTeam(this.x + val, this.y - val)){
			int[] move = {this.x + val, this.y - val};
			moves.add(move);
		}
		
		//Return moves
		return moves;
	}

	@Override
	// getWorth() method which returns the worth of the piece
	public int getWorth() {
		return worth;
	}

	@Override
	// getSymbol() method which returns the symbol for the piece
	public String getSymbol() {
		return symbol;
	}

	// Constructor
	public Pawn(int x, int y, boolean player) {

		// Setting base values for the Pawn piece
		worth = 1;
		isPlayer = player;
		
		//Draw distinctions between players
		if(player){
			symbol = "P";
		}else{
			symbol = "p";
		}

		// Using accessory method for clarity; not strictly necessary
		setPosition(x, y);
	}
}
