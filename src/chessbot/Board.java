package chessbot;

import java.util.*;

//Class to hold board variables and methods
public class Board {

	/**
	 * Note about board: the 7th and 8th rows are for the Player, the 1st and
	 * 2nd rows are for the computer.
	 **/
	
	public enum CastleState { CASTLED, CASTLE_RUINED, CAN_CASTLE };
	public enum Value { 
		ISOLATED_PAWN, 
		DOUBLED_PAWN,
		HALF_OPEN_FILE,
		PAWN_CHAIN,
		HOLE
	}
	
	final static int CHECKMATE = 1000000;

	// One-dimensional array to hold the locations of all of the pieces
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
	
	CastleState playerCastle = CastleState.CAN_CASTLE; 
	CastleState computerCastle = CastleState.CAN_CASTLE;
	
	Point enPassantTarget = null;
	
	List<String> moveHistory = new ArrayList<String>();
	
	int halfmoveClock = 0;
	int fullMoveCounter = 0;
	
	int isolatedPawnValue = 29;
	int doubledPawnValue = 50;
	int halfOpenFileValue = 25;
	int pawnChainValue = 25;
	int holeValue = 25;

	// Override java.lang.Object.toString method to create easier to read output
	// in the form of a table
	@Override
	public String toString() {

		// String variable to eventually return		
		String aString = "FEN: " + Utils.boardToFEN(this) + " Board Zobrist Hash: " + Zobrist.getZobristHash(this) + "\n";
		//aString += "Piece List: "+pieceList +"\n";
		//aString += "Move History: "+moveHistory +"\n";
		
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
	public List<Move> captureMoves(){
		
		List<Move> captureMoves = new ArrayList<Move>();
		List<Move> allMoves = allMoves();
		
		for(Move m : allMoves){
			
			if(m.isLoud()){
				captureMoves.add(m);
			}
			
		}
		
		return captureMoves;
	}
	
	//Find all possible check moves
	public List<Move> checkMoves(){
		
		List<Move> checkMoves = new ArrayList<Move>();
		List<Move> allMoves = allMoves();
		
		for(Move m : allMoves){
			
			if(m.isCheck()){
				checkMoves.add(m);
			}
			
		}
		
		return checkMoves;
	}

	// Find all possible moves
	public List<Move> allMoves() {
		List<Move> rawMoves = rawMoves();
		// Remove moves that are illegal because of rules regarding check
		for (Iterator<Move> iterator = rawMoves.iterator(); iterator.hasNext();) {
			if (!isLegal(iterator.next())) {
				iterator.remove();
			}
		}
		return rawMoves;

	}
	
	// Find all possible moves
	public boolean isLegal(Move m) {
		m.execute();
		if (this.isCheck(!playerMove)) {
			m.reverse();
			return false;
		} else {
			m.reverse();
			return true;
		}

	}

	public List<Move> rawMoves() {
		// Array list of raw moves - may include illegal ones
		List<Move> rawMoves = new ArrayList<Move>();

		// Loop through all pieces on the board starting from the middle
		/*for (int x = 0; x < 4; x++){
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
		}*/
		for (Piece p: pieceList){
			if (p.alive && p.player == playerMove){
				rawMoves.addAll(p.findMoves(this));
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
		if (isCheck(!playerMove)){
			System.out.println("ERROR: Executed illegal move!");
		}
		if (allMoves().size() == 0) {
			return true;
		}
		// Base case
		return false;
	}

	// Function that retrieves numeric value assigned to position. High values
	// are good for the player, low values good for the computer
	public int evaluateBoard() {
		int score = scoreBoard(false) - scoreBoard(true);
		score += evaluatePawnStructure();
		score += evaluateCastling();
		
		if( isGameOver() ){
			if (isCheck(playerMove)){
				score = CHECKMATE * (playerMove ? 1 : -1);
			}else{
				score = 0;
			}
		}
		
		return score;
	}
	
	public int evaluateCastling(){
		int score = 0;
		if(this.playerCastle == CastleState.CASTLED) score -= 45;
		if(this.playerCastle == CastleState.CASTLE_RUINED) score += 15;
		if(this.computerCastle == CastleState.CASTLED) score += 45;
		if(this.computerCastle == CastleState.CASTLE_RUINED) score -= 15;
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
				score += p.getWorth() + getPieceSquare(p);
		}
	
		return score;
	}
	
	// Function that retrieves numeric value assigned to pawn structure. High values
	// are good for the player, low values good for the computer
	public int evaluatePawnStructure() {
		
		int pawnEvaluation = 0;
	
		//Get hash of current board
		long pHash = Zobrist.getPawnZobristHash(this);
		int index = Zobrist.getIndex(pHash, TranspositionTable.hashSize);
		
		int[][] filledSquares = new int[8][8];
				
		//find entry with same index
		StructureHashEntry oldEntry = StructureTable.pawns.get(index);
		if(oldEntry != null && pHash == oldEntry.pawnZobrist){
			pawnEvaluation = oldEntry.eval;
		}else{

			for (Piece p : pieceList) {
				if (p.alive && p.symbol.equals("p")){
					
					//Add the current position of the pawn to the filled array
					if((p.player && p.getY() < 4) || (!p.player && p.getY() > 3)) filledSquares[p.getY()][p.getX()] = 1;
				
					//Add the squares the pawn can attack to the filled array
					if( (p.player && p.getY() < 3) || (!p.player && p.getY() > 4) ){
						
						int direction = p.player ? 1 : -1;
						if(p.getX() > 0 && p.getY() < 7 && p.getY() > 0) filledSquares[p.getY() + direction][p.getX()-1] = 1;
						if(p.getX() < 7 && p.getY() < 7 && p.getY() > 0) filledSquares[p.getY() + direction][p.getX()+1] = 1;
					
					}		
					
					int pawnScore = 0;
					
					if (isIsolatedPawn(p)) pawnScore -= isolatedPawnValue;
					if (isDoubledPawn(p)) pawnScore -= doubledPawnValue;
					if (isHalfOpenFile(p)) pawnScore -= halfOpenFileValue;
					if (isInPawnChain(p)) pawnScore += pawnChainValue;

					/*** Implementation for pawn structure analysis goes here ***/
					
					//Implementation of Backwards Pawns

					//Implementation of Holes
							
					pawnScore = pawnScore*(p.player ? -1 : 1);
					pawnEvaluation += pawnScore;
				}
			}
			
			//Account for holes
			int playerHoles = 0;
			int computerHoles = 0;

			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){
					
					//If the square is filled...
					if(filledSquares[i][j] == 1) continue;
					
					//If it is a square of interest in the middle
					if(i > 1 && i < 6 && j > 1 && j < 6){
						//If it is on the player's side
						if(i < 4) playerHoles++; else computerHoles++;
						
					}

					//Create negative impacts for holes in front of castled king - may potentially become out-dated if king moves around a lot
					if(this.getKing(true).getX() > 4 && this.getKing(true).getY() < 2 && j > 4 && (i == 2 || i == 1)) playerHoles++;	
					else if(this.getKing(true).getX() < 3 && this.getKing(true).getY() < 2 && j < 3 && (i == 2 || i == 1)) playerHoles++;
					else if(this.getKing(false).getX() > 4 && this.getKing(false).getY() > 5 && j > 4 && (i == 5 || i == 6)) computerHoles++;
					else if(this.getKing(false).getX() < 3 && this.getKing(false).getY() > 5 && j < 3 && (i == 5 || i == 6)) computerHoles++;
				}
			}
			//System.out.println("player holes: " + playerHoles + "computer holes: " + computerHoles);
			//Adjust the evaluation accordingly
			pawnEvaluation += (playerHoles - computerHoles) * holeValue;
		}
	
		//Add the entry to the hash table
		StructureTable.addEntry(new StructureHashEntry(pHash, pawnEvaluation));
			
		return pawnEvaluation;
	}
	
	//evaluation for isolated pawns
	public boolean isIsolatedPawn(Piece pawn)
	{
		for(Piece p: pieceList)
		{
			if(p.symbol.equals("p") && p.player == pawn.player && (p.getX() == pawn.getX()+1 || p.getX() == pawn.getX()-1))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isDoubledPawn(Piece pawn)
	{
		for(Piece p: pieceList)
		{
			if(p.symbol.equals("p") && p.player == pawn.player && p.getX() == pawn.getX())
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isHalfOpenFile(Piece pawn)
	{
		for(Piece p: pieceList)
		{
			if(p.symbol.equals("p") && p.player != pawn.player && p.getX() == pawn.getX())
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean isInPawnChain(Piece pawn)
	{
		for(Piece p: pieceList)
		{
			int dir = p.player ? 1 : -1; //make sure it doesn't include base of pawn chain.
			if(p.symbol.equals("p") && p.player == pawn.player && 
			(
					(p.getX()-1 == pawn.getX() && p.getY()+dir == pawn.getY()) ||
					(p.getX()+1 == pawn.getX() && p.getY()+dir == pawn.getY())
				))
			{
				return true;
			}
		}
		return false;
	}
	
	public void setValues(Map<Value, Integer> valuesMap){
		
		for (Map.Entry<Value, Integer> entry : valuesMap.entrySet())
		{
			if (entry.getKey() == Value.DOUBLED_PAWN){
				doubledPawnValue = entry.getValue();
			}else if (entry.getKey() == Value.HALF_OPEN_FILE){
				halfOpenFileValue = entry.getValue();
			}else if (entry.getKey() == Value.ISOLATED_PAWN){
				isolatedPawnValue = entry.getValue();
			}else if (entry.getKey() == Value.PAWN_CHAIN){
				pawnChainValue = entry.getValue();
			}else if (entry.getKey() == Value.HOLE){
				holeValue = entry.getValue();
			}
		}
	}
	
	public int[][] gridFromPerspective(int[][] grid, boolean player){
		
		if(!player){
			return grid;
		}else{
			
			int[][] flippedGrid = new int[8][8];
			
			for(int i = 0; i < grid.length; i++){
				for(int j = 0; j < grid[i].length; j++){
					flippedGrid[i][j] = grid[7-i][j];
				}
			}
			
			return flippedGrid;
		}
	}
	
	public int getPieceSquare(Piece p){
		
		int[][] pawnPieceSquares = {
				
			{ 0,  0,  0,  0,  0,  0,  0,  0 },
			{50, 50, 50, 50, 50, 50, 50, 50 },
			{10, 10, 20, 30, 30, 20, 10, 10 },
			{ 5,  5, 10, 25, 25, 10,  5,  5 },
			{ 0,  0,  0, 20, 20,  0,  0,  0 },
			{ 5, -5,-10,  0,  0,-10, -5,  5 },
			{ 5, 10, 10,-20,-20, 10, 10,  5 },
			{ 0,  0,  0,  0,  0,  0,  0,  0 }
				  
		};
		
		int[][] knightPieceSquares = {
			
			{ -50,-40,-20,-15,-15,-20,-40,-50 },
			{ -30,-15, -5, -5, -5, -5,-15,-30 },
			{ -20,  0, 10, 15, 15, 10,  0,-20 },
			{ -20,  0, 15, 25, 25, 15,  0,-20 },
			{ -20,  5, 20, 25, 25, 20,  5,-20 },
			{ -20, 10, 15, 20, 20, 15, 10,-20 },
			{ -30,-15,  5, 10, 10,  5,-15,-30 },
			{ -50,-40,-20,-15,-15,-20,-40,-50 }
			
		};
		
		int[][] bishopPieceSquares = {
				
			{ -20,-10,-10,-10,-10,-10,-10,-20 },
			{ -10,  0,  0,  0,  0,  0,  0,-10 },
			{ -10,  0,  5, 10, 10,  5,  0,-10 },
			{ -10,  5,  5, 10, 10,  5,  5,-10 },
			{ -10,  0, 10, 10, 10, 10,  0,-10 },
			{ -10, 10, 10, 10, 10, 10, 10,-10 },
			{ -10,  5,  0,  0,  0,  0,  5,-10 },
			{ -20,-10,-10,-10,-10,-10,-10,-20 }
		};
		
		int[][] queenPieceSquares = {
				
			{ -20,-10,-10, -5, -5,-10,-10,-20 },
			{ -10,  0,  0,  0,  0,  0,  0,-10 },
			{ -10,  0,  5,  5,  5,  5,  0,-10 },
			{  -5,  0,  5,  5,  5,  5,  0, -5 },
			{   0,  0,  5,  5,  5,  5,  0, -5 },
			{ -10,  5,  5,  5,  5,  5,  0,-10 },
			{ -10,  0,  5,  0,  0,  0,  0,-10 },
			{ -20,-10,-10, -5, -5,-10,-10,-20 }
			
		};
		
		int[][] rookPieceSquares = {
				
			{ 0,  0,  0,  0,  0,  0,  0,  0 },
			{  5, 10, 10, 10, 10, 10, 10,  5 },
			{ -5,  0,  0,  0,  0,  0,  0, -5 },
			{ -5,  0,  0,  0,  0,  0,  0, -5 },
			{ -5,  0,  0,  0,  0,  0,  0, -5 },
			{ -5,  0,  0,  0,  0,  0,  0, -5 },
			{ -5,  0,  0,  0,  0,  0,  0, -5 },
			{  0,  0,  0,  5,  5,  0,  0,  0 }
			
		};
		
		int[][] kingPieceSquares = {
				
			{ -30,-40,-40,-50,-50,-40,-40,-30 },
			{ -30,-40,-40,-50,-50,-40,-40,-30 },
			{ -30,-40,-40,-50,-50,-40,-40,-30 },
			{ -30,-40,-40,-50,-50,-40,-40,-30 },
			{ -20,-30,-30,-40,-40,-30,-30,-20 },
			{ -10,-20,-20,-20,-20,-20,-20,-10 },
			{  20, 20,  0,  0,  0,  0, 20, 20 },
			{  20, 30, 10,  0,  0, 10, 30, 20 }
			
		};
		
		if(p.symbol.equals("p")){
			return gridFromPerspective(pawnPieceSquares, p.player)[p.getY()][p.getX()];
		}
		else if(p.symbol.equals("n")){
			return gridFromPerspective(knightPieceSquares, p.player)[p.getY()][p.getX()];
		}
		else if(p.symbol.equals("b")){
			return gridFromPerspective(bishopPieceSquares, p.player)[p.getY()][p.getX()];
		}
		else if(p.symbol.equals("q")){
			return gridFromPerspective(queenPieceSquares, p.player)[p.getY()][p.getX()];
		}
		else if(p.symbol.equals("r")){
			return gridFromPerspective(rookPieceSquares, p.player)[p.getY()][p.getX()];
		}
		else if(p.symbol.equals("k")){
			return gridFromPerspective(kingPieceSquares, p.player)[p.getY()][p.getX()];
		}
		else{
			System.out.println("Invalid piece for Piece Square Function");
			System.exit(0);
			return 0;
		}
	}
}
