package chessbot;

import java.util.*;

//Class to hold board variables and methods
public class Board {

	/**
	 * Note about board: the 7th and 8th rows are for the Player, the 1st and
	 * 2nd rows are for the computer.
	 **/

	// Two-dimensional array to hold the locations of all of the pieces
	Piece[] locations = new Piece[64];

	// One-dimensional array to hold simple list of pieces
	List<Piece> pieceList = new ArrayList<Piece>();
	
	//Boolean that stores whether it is the player's move
	boolean playerMove;
	
	//Variables determining whether castling is still valid
	boolean playerKSideCastle;
	boolean playerQSideCastle;
	boolean botKSideCastle;
	boolean botQSideCastle;
	
	Point enPassantTarget = null;
	
	
	int halfmoveClock = 0;
	int fullMoveCounter = 0;

	// Override java.lang.Object.toString method to create easier to read output
	// in the form of a table
	@Override
	public String toString() {

		// String variable to eventually return		
		String aString = "FEN: " + Utils.boardToFEN(this) + " Board Zobrist Hash: " + Zobrist.getZobristHash(this) + "\n";
		//aString += "Piece List:"+pieceList +"\n";
		
		aString += " |-----------------|\n";
		// Nested loops getting values
		for (int y = 7; y >= -1; y--) {
			if (y != -1) {
				aString += (y + 1) + "|";
				for (int x = 0; x < 8; x++) {
					aString += " " + locations[x + y*8].toString();
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
	
	public boolean canCastleKSide(Piece king){
		
		if((king.player && playerKSideCastle) || (!king.player && botKSideCastle)){
				
			//Check to make sure there is not a piece there...
			if(king.getX() != 4 || !isEmptySquare(new Point(king.getX() + 1, king.getY())) || !isEmptySquare(new Point(king.getX() + 2, king.getY()))){
				return false;
			}
			
			Move testMove = new Move(this, new Point(king.getX() +1, king.getY()), king, null);
			testMove.execute();
			if(isCheck(king.player)){
				testMove.reverse();
				return false;
			}
			testMove.reverse();
			
			Move testMove2 = new Move(this, new Point(king.getX() +2, king.getY()), king, null);
			testMove2.execute();
			if(isCheck(king.player)){
				testMove2.reverse();
				return false;
			}
			
			testMove2.reverse();
			
			return true;	
			
		}
		return false;
	}
	
	public boolean canCastleQSide(Piece king){
		
		if((king.player && playerQSideCastle) || (!king.player && botQSideCastle)){
				
			//Check to make sure there is not a piece there...
			if(king.getX() != 4 || !isEmptySquare(new Point(king.getX() - 1, king.getY())) || !isEmptySquare(new Point(king.getX() - 2, king.getY())) || !isEmptySquare(new Point(king.getX() - 3, king.getY()))){
				return false;
			}
			
			Move testMove = new Move(this, new Point(king.getX() - 1, king.getY()), king, null);
			testMove.execute();
			if(isCheck(king.player)){
				testMove.reverse();
				return false;
			}
			testMove.reverse();
			
			Move testMove2 = new Move(this, new Point(king.getX() - 2, king.getY()), king, null);
			testMove2.execute();
			if(isCheck(king.player)){
				testMove2.reverse();
				return false;
			}
			
			testMove2.reverse();
		
			return true;	
		}
		return false;
	}

	// Function that returns true if a certain square is empty
	public boolean isEmptySquare(Point p) {
		if (p.squareExists()){
			// Load the piece into variable spot
			Piece spot = getPiece(p);
	
			// If the piece has a value of 0, it has to be empty
			if (spot.getWorth() == 0) {
				return true;
			} else {
				return false;
			}
		}else{
			System.out.println("ERROR: isEmptySquare(Point p) called invalid square");
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
		if (pos.squareExists()){
			Piece p = getPiece(pos);
			return p.player;
		}else{
			System.out.println("ERROR: getTeam(Point pos) called invalid square");
			return false;
		}
	}
	
	public Piece getPiece(Point pos) {
		if (pos.squareExists()){
			return locations[pos.getIndex()];
		}else{
			System.out.println("ERROR: getPiece(Point pos) called invalid square");
			Piece empty = new Empty();
			empty.setPosition(pos.x, pos.y);
			return empty;
		}
	}
	
	public void setSquare(Point pos, Piece piece) {
		if (pos.squareExists()){
			locations[pos.getIndex()] = piece;
			piece.position = pos;
		}else{
			System.out.println("ERROR: setSquare(Point pos, Piece piece) called invalid square");

		}
	}

	// Constructor for the Board class - when given a list of pieces
	public Board(List<Piece> list, boolean playerGoesFirst) {

		// Assign list to pieceList variable
		pieceList = list;

		// Fill the locations array with empty squares
		for (int i = 0; i < locations.length; i++) {
			Empty temp = new Empty();
			locations[i] = temp;
		}

		// Place the pieces passed in the list
		for (Piece piece : list) {
			locations[piece.position.getIndex()] = piece;
		}
		
		playerMove = playerGoesFirst;
	}
	
	//Find all possible capture moves
	public List<Move> captureMoves(boolean player){
		
		List<Move> captureMoves = new ArrayList<Move>();
		List<Move> allMoves = allMoves(player);
		
		for(Move m : allMoves){
			
			if(m.isLoud()){
				captureMoves.add(m);
			}
			
		}
		
		return captureMoves;
	}
	
	//Find all possible check moves
	public List<Move> checkMoves(boolean player){
		
		List<Move> checkMoves = new ArrayList<Move>();
		List<Move> allMoves = allMoves(player);
		
		for(Move m : allMoves){
			
			if(m.isCheck()){
				checkMoves.add(m);
			}
			
		}
		
		return checkMoves;
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

		// Loop through all pieces on the board starting from the middle
		for (int x = 0; x < 4; x++){
			for (int y = 0; y < 4; y++){
				Piece p1 = getPiece(new Point(3-x, 4+y));
				Piece p2 = getPiece(new Point(4+x, 4+y));
				Piece p3 = getPiece(new Point(4+x, 3-y));
				Piece p4 = getPiece(new Point(3-x, 3-y));
				if (p1.player == player) {
					List<Move> moves = p1.findMoves(this);
					rawMoves.addAll(moves);
				}
				if (p2.player == player) {
					List<Move> moves = p2.findMoves(this);
					rawMoves.addAll(moves);
				}
				if (p3.player == player) {
					List<Move> moves = p3.findMoves(this);
					rawMoves.addAll(moves);
				}
				if (p4.player == player) {
					List<Move> moves = p4.findMoves(this);
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

		// Base case: there is no check
		if (king != null){
			return Utils.isChecked(this, king);
		}else{
			System.out.println("ERROR: King was eaten!!!");
			System.out.println(this);
			System.exit(0);
		}
		return false;
	}

	// Function that finds the King's position on the board
	public Piece getKing(boolean player) {
		
		for (Piece p : pieceList) {
			if (p.player == player && p.symbol.equals("k") && p.alive == true) {
				// Return the King's position
				return p;
			}
		}
		
		// Default return
		return null;
	}

	// Function that identifies whether the game is over
	public boolean isGameOver() {
		if (allMoves(playerMove).size() == 0 || isCheck(!playerMove)) {
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
			score -= 1000000;
		}
		
		if(allMoves(true).size() == 0 && isCheck(true)){
			score += 1000000;
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
				score += p.getWorth()*100;
		}
		
		//Sum all of the moves 
		List<Move> moves = allMoves(player);
		int totalMoves = moves.size();
		score += totalMoves/3;

	
		return score;
	}
}
