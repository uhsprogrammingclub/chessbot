package chessbot;

import java.util.*;

//Class to hold board variables and methods
public class Board {

	/**
	 * Note about board: the 7th and 8th rows are for the Player, the 1st and
	 * 2nd rows are for the computer.
	 **/

	// One-dimensional array to hold the locations of all of the pieces
	Piece[] locations = new Piece[64];

	// One-dimensional array to hold simple list of pieces
	List<Piece> pieceList = new ArrayList<Piece>();
	
	BB bitboard;
	
	enum Side { W, B, NONE, BOTH;
		Side getOtherSide(){
			if (this == W){
				return B;
			}else if (this == B){
				return W;
			}else{
				return NONE;
			}
		}
		
		int evalFactor(){
			if (this == B){
				return -1;
			}else if (this == W){
				return 1;
			}else{
				return 0;
			}
		}
	}
	
	//Boolean that stores whether it is the player's move
	Side sideMove;
	
	//Array to store castling information
	
	// Castling bits
	final byte WKCA = 1;
	final byte WQCA = 2;
	final byte BKCA = 4;
	final byte BQCA = 8;
	
	int castleRights = 15;
	
	boolean whiteCastled = false;
	boolean blackCastled = false;
	
	Point enPassantTarget = null;
	
	List<String> moveHistory = new ArrayList<String>();
	List<Long> zobristHistory = new ArrayList<Long>();
	
	int halfMoveClock = 0;
	int fullMoveCounter = 0;
	int ply = 0;

	long currentZobrist = 0;
		
	Piece emptySquare = new Empty();

	// Override java.lang.Object.toString method to create easier to read output
	// in the form of a table
	@Override
	public String toString() {

		// String variable to eventually return		
		String aString = "FEN: " + Utils.boardToFEN(this) + " Board Zobrist Hash: " + currentZobrist + "\n";
		//aString += "Piece List: "+pieceList +"\n";
		aString += "Move History: "+moveHistory +"\n";
		
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
		//aString += BB.toString(bitboard.combine());
		aString += "\n";
		
		//aString += BitBoard.toString(bitboard.pawnsAbleToDoublePush(true));
		//aString += "\n";
		
		//aString += BitBoard.toString(bitboard.pawnsAbleToDoublePush(false));
		//aString += "\n";
		
		//long enPassantBit = 0;
		//if (enPassantTarget != null){
			//enPassantBit = (long)1 << enPassantTarget.getIndex();
		//}
		//aString += BitBoard.toString(bitboard.pawnsAbleToAttack(bitboard.pieceBitBoards[BitBoards.BPawn.i], false, enPassantBit));
		//aString += "\n";
		

		return aString;

	}
	
	
	public boolean canCastle(Side side, boolean kSide){
		
		if (isCheck(attacksToKing(side))){
			return false;
		}
		long allBB = bitboard.combine();
		
		if (side == Side.W && (castleRights & WKCA) != 0 && kSide){
			
			if ((allBB & 0x60L) != 0 || bitboard.attacksTo(allBB, 5, side) != 0 || bitboard.attacksTo(allBB, 6, side) != 0){
				return false;
			}
			return true;
		}else if (side == Side.B && (castleRights & BKCA) != 0 && kSide){
			
			if ((allBB & 0x6000000000000000L) != 0 || bitboard.attacksTo(allBB, 61, side) != 0 || bitboard.attacksTo(allBB, 62, side) != 0){
				return false;
			}
			return true;
		}else if (side == Side.W && (castleRights & WQCA) != 0 && !kSide){
			
			if ((allBB & 0xEL) != 0 || bitboard.attacksTo(allBB, 3, side) != 0 || bitboard.attacksTo(allBB, 2, side) != 0){
				return false;
			}
			return true;
		}else if (side == Side.B && (castleRights & BKCA) != 0 && !kSide){
			
			if ((allBB & 0x0E00000000000000L) != 0 || bitboard.attacksTo(allBB, 59, side) != 0 || bitboard.attacksTo(allBB, 58, side) != 0){
				return false;
			}
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
	
	public Side getSide(Point pos) {
		if (pos.squareExists()){
			Piece p = getPiece(pos);
			return p.side;
		}else{
			System.out.println("ERROR: getTeam(Point pos) called invalid square");
			return Side.NONE;
		}
	}
	
	public Piece getPiece(Point pos) {
		if (pos.squareExists()){
			return locations[pos.getIndex()];
		}else{
			System.out.println("ERROR: getPiece(Point pos) called invalid square");
			return emptySquare;
		}
	}
	
	public void setSquare(Point pos, Piece piece) {
		if (pos.squareExists()){
			for (int i = 0; i < bitboard.pieceBitBoards.length; i++){
				bitboard.pieceBitBoards[i] = BB.clearBit(bitboard.pieceBitBoards[i], pos.getIndex());
			}
			locations[pos.getIndex()] = piece;
			piece.position = pos;
			if (AIController.useBitBoards){
				bitboard.setBitFromPiece(piece);
			}
		}else{
			System.out.println("ERROR: setSquare(Point pos, Piece piece) called invalid square");

		}
	}

	// Constructor for the Board class - when given a list of pieces
	public Board(List<Piece> list, Side firstSideToMove) {

		// Assign list to pieceList variable
		pieceList = list;

		// Fill the locations array with empty squares
		for (int i = 0; i < locations.length; i++) {
			locations[i] = emptySquare;
		}

		// Place the pieces passed in the list
		for (Piece piece : list) {
			locations[piece.position.getIndex()] = piece;
		}
		
		sideMove = firstSideToMove;
		
		bitboard = new BB(this);
		
		currentZobrist = Zobrist.getZobristHash(this);
	}
	
	//Find all possible capture moves
	public List<Move> captureMoves(){
		
		List<Move> captureMoves = new ArrayList<Move>();
		List<Move> allMoves = legalMoves(rawMoves());
		
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
		List<Move> allMoves = legalMoves(rawMoves());
		
		for(Move m : allMoves){
			
			if(m.isCheck()){
				checkMoves.add(m);
			}
			
		}
		
		return checkMoves;
	}

	// Find all possible moves
	public List<Move> legalMoves(List<Move> rawMoves) {
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
		if (this.isCheck(attacksToKing(sideMove.getOtherSide()))) {
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
		if (!AIController.useBitBoards){
			for (Piece p: pieceList){
				if (p.alive && p.side == sideMove){
					rawMoves.addAll(p.findMoves(this));
				}
			}
		}else{
			long friendlyBB = bitboard.getFriendlyBB(sideMove);
			rawMoves.addAll(Pawn.getMovesFromBitboard(this, friendlyBB & bitboard.pieceBitBoards[BB.PAWNS], sideMove));
			rawMoves.addAll(Rook.getMovesFromBitboard(this, friendlyBB & bitboard.pieceBitBoards[BB.ROOKS], sideMove));
			rawMoves.addAll(Bishop.getMovesFromBitboard(this, friendlyBB & bitboard.pieceBitBoards[BB.BISHOPS], sideMove));
			rawMoves.addAll(Queen.getMovesFromBitboard(this, friendlyBB & bitboard.pieceBitBoards[BB.QUEENS], sideMove));
			rawMoves.addAll(King.getMovesFromBitboard(this, friendlyBB & bitboard.pieceBitBoards[BB.KINGS], sideMove));
			rawMoves.addAll(Knight.getMovesFromBitboard(this, friendlyBB & bitboard.pieceBitBoards[BB.KNIGHTS], sideMove));
		}

		return rawMoves;
	}

	// Function that identifies whether the king is directly threatened
	public boolean isCheck(long attacksToKing) {

		/*if (!AIController.useBitBoards){
			// Load the King's position
			Piece king = getKing(side);
	
			// Base case: there is no check
			if (king != null){
				return Utils.isChecked(this, king);
			}else{
				System.out.println("ERROR: King was eaten!!!");
				System.out.println(this);
				System.exit(0);
			}
		}else{*/
	
		if (attacksToKing != 0){
			return true;
		}
		
		//}
		return false;
	}
	
	// Function that identifies whether the king is in double check
	public boolean isDoubleCheck(long attacksToKing) {
		if (BB.countSetBits(attacksToKing) > 1){
			return true;
		}
		return false;
	}
	
	// Function that gets all attacks on king
	public long attacksToKing(Side side) {

		long friendlyBB = bitboard.getFriendlyBB(side);
		int kingIndex = BB.bitScanForward(bitboard.pieceBitBoards[BB.KINGS] & friendlyBB);

		return bitboard.attacksTo(bitboard.combine(), kingIndex, side);
	}
	
	// Function that identifies whether it's a checkmate
	public boolean isCheckmate() {

		long kingAttacks = attacksToKing(sideMove);
		if (!isCheck(kingAttacks)){
			return false;
		}
		long friendlyBB = bitboard.getFriendlyBB(sideMove);
		List <Move> potentialMoves = new ArrayList<Move>();
		
		//king's moves
		potentialMoves.addAll(King.getMovesFromBitboard(this, friendlyBB & bitboard.pieceBitBoards[BB.KINGS], sideMove));
		if (!isDoubleCheck(kingAttacks)){
			potentialMoves.addAll(bitboard.attacksTo(this, BB.bitScanForward(kingAttacks), sideMove.getOtherSide()));
			
			if ((kingAttacks & bitboard.pieceBitBoards[BB.KNIGHTS]) == 0){
				long attackerPath = 0;
				//TODO find attacker path
				
				while(attackerPath != 0){
					int i = BB.bitScanForward(attackerPath);
					potentialMoves.addAll(bitboard.attacksTo(this, i, sideMove.getOtherSide()));
					attackerPath = BB.clearBit(attackerPath, i);
				}
			}
		}
		
		if (legalMoves(potentialMoves).size() == 0) {
			return true;
		}
		
		return false;
	}
	
	public boolean isStalemate() {

		long kingAttacks = attacksToKing(sideMove);
		if (isCheck(kingAttacks)){
			return false;
		}
		long friendlyBB = bitboard.getFriendlyBB(sideMove);
		List <Move> potentialMoves = new ArrayList<Move>();
		
		//king's moves
		potentialMoves.addAll(King.getMovesFromBitboard(this, friendlyBB & bitboard.pieceBitBoards[BB.KINGS], sideMove));
		
		//TODO check if all pawns are rammed
		//TODO check if all other pieces are pinned
		
		if (legalMoves(potentialMoves).size() == 0) {
			return true;
		}
		
		return false;
	}

	// Function that finds the King's position on the board
	public Piece getKing(Side side) {
		if (!AIController.useBitBoards){
			for (Piece p : pieceList) {
				if (p.side == side && p.symbol.equals("k") && p.alive == true) {
					// Return the King's position
					return p;
				}
			}
		}else{
			long friendlyBB = bitboard.getFriendlyBB(side);
			long king = friendlyBB & bitboard.pieceBitBoards[BB.KINGS];
			int kingIndex = BB.bitScanForward(king);
			Piece p = getPiece(new Point(kingIndex));
			if (p.alive == true) {
				// Return the King's position
				return p;
			}else{
				System.out.println("ERROR: Dead King in bitboard!");
			}
		}
		
		// Default return
		return null;
	}

	// Function that identifies whether the game is over
	public boolean isGameOver() {
		if (isCheck(attacksToKing(sideMove.getOtherSide()))){
			System.out.println("ERROR: Executed illegal move!");
		}
		if (isCheckmate() || isStalemate() || legalMoves(rawMoves()).size() == 0) {
			return true;
		}
		if (halfMoveClock >= 100) {
			return true;
		}
		for (int i = zobristHistory.size()-halfMoveClock; i < zobristHistory.size()-1; i++){
			if (currentZobrist == zobristHistory.get(i)){
				for (int ii = i+1; i < zobristHistory.size()-1; ii++){
					if (currentZobrist == zobristHistory.get(ii)){
						return true;
					}
				}
			}
		}
		// Base case
		return false;
	}

	// Function that retrieves numeric value assigned to position. High values
	// are good for the player, low values good for the computer
	public int evaluateBoard() {
		int score = scoreBoard(Side.W) - scoreBoard(Side.B);

		if(AIController.usePawnEvaluations) score += evaluatePawnStructure();

		score += evaluateCastling();
		
		if( isGameOver() ){
			if (isCheck(attacksToKing(sideMove)) && legalMoves(rawMoves()).size() == 0){
				score = Evaluation.CHECKMATE * -sideMove.evalFactor();
			}else{
				score = Evaluation.contemptFactor;
			}
		}
		
		return score;
	}
	
	public int evaluateCastling(){
		int score = 0;
		if(whiteCastled) score += 45;
		if(blackCastled) score -= 45;
		if(!whiteCastled && (castleRights & (WQCA | WKCA)) == 0) score -= 45;
		if(!blackCastled && (castleRights & (BQCA | BKCA)) == 0) score += 45;
		return score;
	}
	
	// Analyze the board, and assign a numeric value to it for the position
	// based on how favorable it is for the designated player.
	public int scoreBoard(Side side) {

		// Default the score to zero
		int score = 0;
		
		//Implement piece square scores
		score += ((earlyPieceSquares(side) * (256 - getPhase() )) + (latePieceSquares(side) * getPhase() )) / 256;
		
		//System.out.println("Weighted: " + score);
		
		// Sum the worth of all of the pieces to get a base score
		long friendlyBB = bitboard.getFriendlyBB(side);
		
		score += Pawn.WORTH * BB.countSetBits(bitboard.pieceBitBoards[BB.PAWNS] & friendlyBB);
		score += Knight.WORTH * BB.countSetBits(bitboard.pieceBitBoards[BB.KNIGHTS] & friendlyBB);
		score += Bishop.WORTH * BB.countSetBits(bitboard.pieceBitBoards[BB.BISHOPS] & friendlyBB);
		score += Rook.WORTH * BB.countSetBits(bitboard.pieceBitBoards[BB.ROOKS] & friendlyBB);
		score += Queen.WORTH * BB.countSetBits(bitboard.pieceBitBoards[BB.QUEENS] & friendlyBB);
		score += King.WORTH * BB.countSetBits(bitboard.pieceBitBoards[BB.KINGS] & friendlyBB);
		
		return score;
	}
	
	// Function that retrieves numeric value assigned to pawn structure. High values
	// are good for the player, low values good for the computer
	public int evaluatePawnStructure() {
		
		int pawnEvaluation = 0;
	
		//Get hash of current board
		long pHash = Zobrist.getPawnZobristHash(this);
		int index = Zobrist.getIndex(pHash, TranspositionTable.hashSize);
				
		//find entry with same index
		StructureHashEntry oldEntry = StructureTable.pawns.get(index);
		if(oldEntry != null && pHash == oldEntry.pawnZobrist){
			pawnEvaluation = oldEntry.eval;
		}else{
			
			//Complete passed pawn evaluations
			long blackPawns = bitboard.pieceBitBoards[BB.PAWNS] & bitboard.pieceBitBoards[BB.BLACK];
			long whitePawns = bitboard.pieceBitBoards[BB.PAWNS] & bitboard.pieceBitBoards[BB.WHITE];

			pawnEvaluation += checkPassedPawns(whitePawns, blackPawns);
			pawnEvaluation += checkDoubledPawns(whitePawns, blackPawns);
			pawnEvaluation += checkIsolatedPawns(whitePawns, blackPawns); 
			pawnEvaluation += checkPawnChains(whitePawns, blackPawns);
			pawnEvaluation += checkHalfOpenFiles(whitePawns, blackPawns);
			pawnEvaluation += checkHoles(whitePawns, blackPawns);
		}	
	
		//Add the entry to the hash table
		StructureTable.addEntry(new StructureHashEntry(pHash, pawnEvaluation));
			
		return pawnEvaluation;
	}
	
	public int checkHoles(long whitePawns, long blackPawns){
		long whiteFilled = (BB.upLeft(whitePawns) | BB.upRight(whitePawns)) | whitePawns;
		long blackFilled = (BB.downLeft(blackPawns) | BB.downRight(blackPawns)) | blackPawns;

		long whiteKing = bitboard.pieceBitBoards[BB.KINGS] & bitboard.pieceBitBoards[BB.WHITE];
		long blackKing = bitboard.pieceBitBoards[BB.KINGS] & bitboard.pieceBitBoards[BB.BLACK];
		long whiteKingHoles = BB.fillFile(BB.RANK_1 & (whiteKing | BB.right(whiteKing) | BB.left(whiteKing))) & ( BB.up(BB.RANK_1));
		long blackKingHoles = BB.fillFile(BB.RANK_8 & (blackKing | BB.right(blackKing) | BB.left(blackKing))) & ( BB.down(BB.RANK_8));
		
		long whiteResult = ~whiteFilled & (0x3C3C0000L | whiteKingHoles);
		long blackResult = ~blackFilled & (0x3C3C00000000L | blackKingHoles);
		
		return Evaluation.holeValue * (BB.countSetBits(blackResult) - BB.countSetBits(whiteResult));
				
	}
	
	public int checkHalfOpenFiles(long whitePawns, long blackPawns){
		long whiteFiles = BB.fillFile(whitePawns);
		long blackFiles = BB.fillFile(blackPawns);
		long whiteResult = whiteFiles & ~blackFiles & BB.RANK_1;
		long blackResult = ~whiteFiles & blackFiles & BB.RANK_1;	

		return (BB.countSetBits(blackResult) - BB.countSetBits(whiteResult)) * Evaluation.halfOpenFileValue;
	}
	
	public int checkPawnChains(long whitePawns, long blackPawns){
		long whiteResult = (BB.upLeft(whitePawns) | BB.upRight(whitePawns)) & whitePawns;
		long blackResult = (BB.downLeft(blackPawns) | BB.downRight(blackPawns)) & blackPawns;
		return (BB.countSetBits(whiteResult) - BB.countSetBits(blackResult)) * Evaluation.pawnChainValue;
	}
	
	public int checkIsolatedPawns(long whitePawns, long blackPawns){
		
		long pawnFiles = BB.fillFile(whitePawns);
		long protectedFiles = (BB.left(pawnFiles) | BB.right(pawnFiles));	
		long whiteIsolatedPawns = whitePawns & ~protectedFiles;
		pawnFiles = BB.fillFile(blackPawns);
		protectedFiles = BB.left(pawnFiles) | BB.right(pawnFiles);
		long blackIsolatedPawns = blackPawns & ~protectedFiles;
		return Evaluation.isolatedPawnValue * (BB.countSetBits(blackIsolatedPawns) - BB.countSetBits(whiteIsolatedPawns));
	}
	
	public int checkDoubledPawns(long whitePawns, long blackPawns){
		
		long whiteRearSpans = BB.down(BB.downFill(whitePawns));
		long blackRearSpans = BB.up(BB.upFill(blackPawns));
		long whiteResult = whiteRearSpans & whitePawns;
		long blackResult = blackRearSpans & blackPawns;
		return (BB.countSetBits(blackResult) - BB.countSetBits(whiteResult)) * Evaluation.doubledPawnValue;
	}
	
	public int checkPassedPawns(long whitePawns, long blackPawns){
		long whiteFrontSpans = BB.up(BB.upFill(whitePawns));
		long blackFrontSpans = BB.down(BB.downFill(blackPawns));
		whiteFrontSpans |= BB.left(whiteFrontSpans) | BB.right(whiteFrontSpans);
		blackFrontSpans |= BB.left(blackFrontSpans) | BB.right(blackFrontSpans);
		long whiteResult = whitePawns & ~blackFrontSpans;
		long blackResult = blackPawns & ~whiteFrontSpans;
		return (BB.countSetBits(whiteResult) - BB.countSetBits(blackResult)) * Evaluation.passedPawnValue;
	}
	
	
	/*Deprecated Functions 
	public boolean isIsolatedPawn(Piece pawn)
	{
				
		for(Piece p: pieceList)
		{
			if(p.symbol.equals("p") && p.player == pawn.player && (p.getX() == pawn.getX()+1 || p.getX() == pawn.getX()-1))
			{
				return false;
			}
		}

		return false;
		
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
	*/
	
	
	public void setValues(Map<Evaluation.Value, Integer> valuesMap){
		
		for (Map.Entry<Evaluation.Value, Integer> entry : valuesMap.entrySet())
		{
			if (entry.getKey() == Evaluation.Value.DOUBLED_PAWN){
				Evaluation.doubledPawnValue = entry.getValue();
			}else if (entry.getKey() == Evaluation.Value.HALF_OPEN_FILE){
				Evaluation.halfOpenFileValue = entry.getValue();
			}else if (entry.getKey() == Evaluation.Value.ISOLATED_PAWN){
				Evaluation.isolatedPawnValue = entry.getValue();
			}else if (entry.getKey() == Evaluation.Value.PAWN_CHAIN){
				Evaluation.pawnChainValue = entry.getValue();
			}else if (entry.getKey() == Evaluation.Value.HOLE){
				Evaluation.holeValue = entry.getValue();
			}else if (entry.getKey() == Evaluation.Value.PASSED_PAWN){
				Evaluation.passedPawnValue = entry.getValue();
			}
		}
	}
	
	public int[] gridFromPerspective(int[] grid, Side side){
		
		if(side == Side.B){
			return grid;
		}else{
			
			int[] flippedGrid = new int[64];
			
			for(int i = 7; i < 32; i += 8){
				for(int j = 0; j < 8; j++){
					flippedGrid[i-j] = grid[63-(i-j)];
					flippedGrid[63-(i-j)] = grid[i-j];
				}
			}
			
			/*System.out.println("\nOriginal:");
			
			for(int i = 0; i < 64; i+=8){
				System.out.println("");
				for(int j = 0; j < 8; j++){
					System.out.print(" " + grid[i+j] + " ");
				}
			}
			
			System.out.println("\nFlipped: ");
			
			for(int i = 0; i < 64; i+=8){
				System.out.println("");
				for(int j = 0; j < 8; j++){
					System.out.print(" " + flippedGrid[i+j] + " ");
				}
			}*/
			
			return flippedGrid;
		}
	}

	public int getPhase(){
		int phase = Evaluation.totalPhase;
		phase -= (BB.countSetBits(bitboard.pieceBitBoards[BB.PAWNS]) * Evaluation.pawnPhase 
				+ BB.countSetBits(bitboard.pieceBitBoards[BB.BISHOPS]) * Evaluation.bishopPhase
				+ BB.countSetBits(bitboard.pieceBitBoards[BB.KNIGHTS]) * Evaluation.knightPhase
				+ BB.countSetBits(bitboard.pieceBitBoards[BB.QUEENS]) * Evaluation.queenPhase);
		
		return (phase * 256 + (Evaluation.totalPhase / 2)) / Evaluation.totalPhase;
	}
	
	public int latePieceSquares(Side side){
		
		int score = 0;

		int[] indexes = { BB.PAWNS, BB.KNIGHTS, BB.BISHOPS, BB.QUEENS, BB.KINGS };

		for (int index : indexes) {

			int colorIndex = (side == Side.W) ? BB.WHITE : BB.BLACK;
			long pieces = bitboard.pieceBitBoards[index] & bitboard.pieceBitBoards[colorIndex];

			int[] positions = BB.getSetBits(pieces);
			
			for (int i : positions) {

				switch (index) {

				case BB.PAWNS:
					score += gridFromPerspective(Evaluation.pawnPieceSquaresL, side)[i];	
					break;
				case BB.BISHOPS:
					score += gridFromPerspective(Evaluation.bishopPieceSquaresE, side)[i];
					break;
				case BB.KNIGHTS:
					score += gridFromPerspective(Evaluation.knightPieceSquaresE, side)[i];
					break;
				case BB.ROOKS:
					score += gridFromPerspective(Evaluation.rookPieceSquaresE, side)[i];
					break;
				case BB.KINGS:
					score += gridFromPerspective(Evaluation.kingPieceSquaresL, side)[i];

				}

			}

		}

		return score;

	}
	
	public int earlyPieceSquares(Side side) {
		
		int score = 0;

		int[] indexes = { BB.PAWNS, BB.KNIGHTS, BB.BISHOPS, BB.QUEENS, BB.KINGS };

		for (int index : indexes) {

			int colorIndex = (side == Side.W) ? BB.WHITE : BB.BLACK;
			long pieces = bitboard.pieceBitBoards[index] & bitboard.pieceBitBoards[colorIndex];

			int[] positions = BB.getSetBits(pieces);
			
			for (int i : positions) {

				switch (index) {

				case BB.PAWNS:
					score += gridFromPerspective(Evaluation.pawnPieceSquaresE, side)[i];
					break;
				case BB.BISHOPS:
					score += gridFromPerspective(Evaluation.bishopPieceSquaresE, side)[i];
					break;
				case BB.KNIGHTS:
					score += gridFromPerspective(Evaluation.knightPieceSquaresE, side)[i];
					break;
				case BB.ROOKS:
					score += gridFromPerspective(Evaluation.rookPieceSquaresE, side)[i];
					break;
				case BB.KINGS:
					score += gridFromPerspective(Evaluation.kingPieceSquaresE, side)[i];

				}

			}

		}

		return score;

	}
	
}

