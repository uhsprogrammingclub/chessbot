package chessbot;

import java.util.*;

//Class to hold board variables and methods
public class Board {

	/**
	 * Note about board: the 7th and 8th rows are for the Player, the 1st and
	 * 2nd rows are for the computer.
	 **/

	// Two-dimensional array to hold the locations of all of the pieces
	Piece[][] locations = new Piece[8][8];

	// One-dimensional array to hold simple list of pieces
	List<Piece> pieceList = new ArrayList<Piece>();

	// Override java.lang.Object.toString method to create easier to read output
	// in the form of a table
	@Override
	public String toString() {

		// String variable to eventually return		
		String aString = "Board Zobrist Hash: " + Zobrist.getZobristHash(this) + "\n";
		
		aString += " |-----------------|\n";
		// Nested loops getting values
		for (int y = 7; y >= -1; y--) {
			if (y != -1) {
				aString += (y + 1) + "|";
				for (int x = 0; x < 8; x++) {
					aString += " " + locations[x][y].toString();
				}
				aString += " |";
			} else {
				aString += " |-----------------|\n";
				aString += "   A B C D E F G H";
			}

			// Create a new line
			aString += "\n";
		}

		return aString;

	}

	// Function that returns true if a certain square is empty
	public boolean isEmptySquare(Point p) {

		// Load the piece into variable spot
		Piece spot = locations[p.x][p.y];

		// If the piece has a value of 0, it has to be empty
		if (spot.getWorth() == 0) {
			return true;
		} else {
			return false;
		}
	}

	// Function that returns the team of the piece at the specified locations.
	// True = player; False = computer
	/**
	 * Function does not validate square, so it MUST EXIST OR WILL THROW ARRAY
	 * INDEX OUT OF BOUNDS
	 **/
	public boolean getTeam(Point pos) {
		Piece p = locations[pos.x][pos.y];
		return p.player;
	}

	// Constructor for the Board class - when given a list of pieces
	public Board(List<Piece> list) {

		// Assign list to pieceList variable
		pieceList = list;

		// Fill the locations array with empty squares
		for (int i = 0; i < locations.length; i++) {
			for (int j = 0; j < locations.length; j++) {
				Empty temp = new Empty();
				locations[i][j] = temp;
			}
		}

		// Place the pieces passed in the list
		for (Piece piece : list) {
			locations[piece.getX()][piece.getY()] = piece;
		}
	}

	// Find all possible moves
	public List<Move> allMoves(boolean player) {
		List<Move> rawMoves = rawMoves(player);
		// Remove moves that are illegal because of rules regarding check
		for (Iterator<Move> iterator = rawMoves.iterator(); iterator.hasNext();) {
			Move m = iterator.next();
			m.execute();
			if (this.isCheck(player)) {
				m.reverse();
				iterator.remove();
			} else {
				m.reverse();
			}
		}

		return rawMoves;

	}

	public List<Move> rawMoves(boolean player) {
		// Array list of raw moves - may include illegal ones
		List<Move> rawMoves = new ArrayList<Move>();

		// Loop through all pieces on the board
		for (Piece[] l : locations) {
			for (Piece p : l) {
				// Add all possibilities of each piece's move to the raw list
				if (p.player == player) {
					List<Move> moves = p.findMoves(this);
					rawMoves.addAll(moves);
				}
			}
		}
		return rawMoves;
	}

	// Function that identifies whether the king is directly threatened
	public boolean isCheck(boolean player) {

		// Load the King's position
		Piece king = getKing(player);

		// Load all possible moves in this (hypothetical) board
		List<Move> rawMoves = rawMoves(!player);

		for (Move m : rawMoves) {
			// If a check exists...
			if (m.destinationPc.equals(king)) {
				return true;
			}
		}

		// Base case: there is no check
		return false;
	}

	// Function that finds the King's position on the board
	public Piece getKing(boolean player) {
		for (Piece[] l : locations) {
			for (Piece p : l) {
				if (p.player == player && p.symbol.equals("k")) {
					// Return the King's position
					return p;
				}
			}
		}
		// Default return
		return null;
	}

	// Function that identifies whether the game is over
	public boolean isGameOver( boolean playerTurn ) {
		boolean[] players = { true, false };
		for (boolean p : players) {
			if (allMoves(p).size() == 0 && (isCheck(p) || playerTurn == p)) {
				return true;
			}
		}
		if( scoreBoard(true) == 10000 && scoreBoard(false) == 10000){
			return true;
		}
		// Base case
		return false;
	}

	// Function that retrieves numeric value assigned to position. High values
	// are good for the player, low values good for the computer
	public int evaluateBoard() {
		
		int score = scoreBoard(false) - scoreBoard(true);
		
		if(allMoves(false).size() == 0 && isCheck(false)){
			score -= 10000;
		}
		
		if(allMoves(true).size() == 0 && isCheck(true)){
			score += 10000;
		}
		
		return score;
	}

	// Analyze the board, and assign a numeric value to it for the position
	// based on how favorable it is for the designated player.
	public int scoreBoard(boolean player) {

		// Default the score to zero
		int score = 0;

		// Sum the worth of all of the pieces to get a base score
		for (Piece p : pieceList) {
			if (p.player == player && p.alive)
				score += p.getWorth();
		}

		return score;
	}
}
