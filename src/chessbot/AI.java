package chessbot;

import java.util.*;

public class AI {
	
	//AIController
	AIController AIC = new AIController();
	
	// Constants
	static final int INFINITY = 1000000000;

	// Initialize board
	Board board;
	
	//Initialize killerMoves list of Move objects
	List<Move> killerMoves = new ArrayList<Move>();
	
	//Function that calls the main Negamax function
	//Increments the maximum depth allowed until total static computations is exceeded
	void search() {
		int currentDepth;
		int alpha = -INFINITY;
		int beta = INFINITY;
		if (AIController.useOpeningBook){
			OpeningMove opening = OpeningBook.getOpeningMove(board, AIC.currentECO);
			if (opening != null){
				AIC.bestRootMove = new MoveAndScore(new Move(board, opening.move), 0);
				AIC.currentECO = opening.ECO;
				return;
			}
		
		}
		
		if(AIController.useTimeControls){
			AIController.plyFromOpening += 1;
			AIController.timeLimit = AIC.getAllottedTime();
			AIController.timeLeft -= AIController.timeLimit;
		}
		
		for (currentDepth = 1; currentDepth <= AIC.DEPTH_LIMIT; currentDepth++) {
			int previousNodes = AIC.totalNodes;
			AIC.evaluateToDepth = currentDepth;
			int result = negaMax(alpha, beta, currentDepth);
			int nodesBeforeResearch = AIC.totalNodes;
			if (result <= alpha){
				alpha = -INFINITY;
				result = negaMax(alpha, beta, currentDepth);
			}
			if (result >= beta){
				beta = INFINITY;
				result = negaMax(alpha, beta, currentDepth);
			}
			AIC.researches += AIC.totalNodes - nodesBeforeResearch;
			
			if (AIController.aspirationWindow){
				alpha = result - 10;
				beta = result + 10;
			}
			if (result == Evaluation.CHECKMATE || result == -Evaluation.CHECKMATE){
				AIC.stopSearching = true;
			}
			if (AIC.stopSearching) {
				break;
			}
			AIC.depthsPV.add("PV at depth " + currentDepth + ": " + new PV(board) + " EBF: " + AIC.totalNodes*100/previousNodes/100.0);
		
		}
		AIC.depthsPV.add("PV at final depth " + AIC.evaluateToDepth + ": " + new PV(board));
	}
	
	boolean isRepetition(){
		int totalHalfMoves = board.zobristHistory.size();
		for (int i = totalHalfMoves - board.halfMoveClock; i < totalHalfMoves-1; i++){
			if (board.currentZobrist == board.zobristHistory.get(i)){
				return true;
			}
		}
		return false;
	}
	
	boolean pvCausesRepetition(){
		PV pv = new PV(board);
		for (int i = board.zobristHistory.size()-board.halfMoveClock; i < board.zobristHistory.size()-1; i++){
			for (Long z : pv.zobristList){
				if (z == board.zobristHistory.get(i)){
					return true;
				}
			}
		}
		return false;
	}

	//Function that takes an input of the depth and the move to add to the killerMoves list
	//A move is considered a killer move if it resulted in a beta cutoff at a different branch at the same depth
	//Stores three killer moves per depth
	void addKillerMove(int depth, Move move) {
		if (killerMoves.size() < (depth + 1) * 3) {
			for (int i = killerMoves.size(); i < (depth + 1) * 3; i++) {
				killerMoves.add(i, null);
			}
		}

		Move m1 = killerMoves.get(depth * 3);
		Move m2 = killerMoves.get(depth * 3 + 1);
		Move m3 = killerMoves.get(depth * 3 + 2);

		if (move.equals(m1)) {
			return;
		} else if (move.equals(m2)) {
			killerMoves.set(depth * 3, m2);
			killerMoves.set(depth * 3 + 1, m1);
			return;
		} else if (move.equals(m3)) {
			killerMoves.set(depth * 3, m3);
			killerMoves.set(depth * 3 + 1, m1);
			killerMoves.set(depth * 3 + 2, m2);
			return;
		} else {
			killerMoves.set(depth * 3, move);
			killerMoves.set(depth * 3 + 1, m1);
			killerMoves.set(depth * 3 + 2, m2);
		}

	}

	//Function that returns the list of the three killer moves at that depth
	List<Move> getKillerMoves(int depth) {
		if (killerMoves.size() < (depth + 1) * 3) {
			for (int i = killerMoves.size(); i < (depth + 1) * 3; i++) {
				killerMoves.add(i, null);
			}
		}

		List<Move> moves = new ArrayList<Move>();

		Move m1 = killerMoves.get(depth * 3);
		Move m2 = killerMoves.get(depth * 3 + 1);
		Move m3 = killerMoves.get(depth * 3 + 2);

		if (m1 != null) {
			moves.add(m1);
		}
		if (m2 != null) {
			moves.add(m2);
		}
		if (m3 != null) {
			moves.add(m3);
		}
		return moves;
	}

	//Constructor for the class
	public AI(Board board) {
		this.board = board;
	}
	
	// Basic Quiescence Search
	int qSearch(int alpha, int beta) {
		
		if ((AIC.totalNodes & 2047) == 0){ 
			AIC.checkTimeLimit(); //check every 2048 nodes
		}
		// Increment the static computations
		AIC.staticComputations++;
		if (AIC.stopSearching){
			return 0;
		}
		
		if (AIC.computationsAtPly.get(board.ply) == null){
			AIC.computationsAtPly.put(board.ply, 0);
		}
		
		AIC.computationsAtPly.put(board.ply, AIC.computationsAtPly.get(board.ply) + 1);

		int standPat = board.evaluateBoard() * board.sideMove.evalFactor();
		
		long zHash = board.currentZobrist;
		boolean newAlpha = false;

		// Update the alpha if the position is an improvement
		if (alpha < standPat) {
			newAlpha = true;
			alpha = standPat;
		}
		
		// Look for a beta cutoff
		if (standPat >= beta) {
			return standPat; // Fail-soft
		}else{
			board.ply++;
		}

		int maxValue = standPat;
		// Examine all captures
		int moveNum = 0;
		Move bestMove = null;
		List<Move> moves = new ArrayList<Move>();
		if (board.isCheck(board.attacksToKing(board.sideMove))){
			moves = board.rawMoves();
			if (AIController.sortMoves){
				Collections.sort(moves);
			}
		}else{
			
			List<Move> loudMoves = board.loudMoves();
			if (AIController.sortMoves){
				Collections.sort(loudMoves);
			}
			
			moves.addAll(loudMoves);

		}
		AIC.totalNodes++;
		AIC.quiescentNodes++;
		for (Move m : moves) {
			if (!board.isLegal(m)){
				continue;
			}
			moveNum++;
			if (AIController.useBitBoards){
				int captureValue = board.bitboard.SEE(m.to.getIndex(), m.from.getIndex(), board.sideMove);
				if (captureValue < 0){
					continue; //Unbeneficial move
				}
			}
			m.execute();
			int currentScore = -qSearch(-beta, -alpha);
			m.reverse();
			
			if (AIC.stopSearching){
				board.ply--;
				return 0;
			}

			maxValue = Math.max(currentScore, maxValue);

			if (alpha < currentScore) {
				bestMove = m;
				newAlpha = true;
			}
			
			if (currentScore >= beta) {
				if (moveNum == 1){
					AIC.fhf++;
				}
				AIC.fh++;
				break;
			}
			
			alpha = Math.max(currentScore, alpha);

		}
		
		if (bestMove != null && maxValue < beta && newAlpha){
			HashEntry newEntry = new HashEntry(zHash, 0, maxValue, HashEntry.PV_NODE, new Move(board, bestMove));
			TranspositionTable.addEntry(newEntry); //add the move entry. only gets placed if eval is higher than previous entry
		}

		board.ply--;
		return maxValue;

	}
	
	int negaMax(int alpha, int beta, int depth){
		
		AIC.totalNodes++;
		//Get hash of current board
		long zHash = board.currentZobrist;
		int index = Zobrist.getIndex(zHash, TranspositionTable.hashSize);
		
		//find entry with same index
		HashEntry oldEntry = TranspositionTable.trans.get(index);
	
		if (isRepetition()){
			return Evaluation.contemptFactor * board.sideMove.evalFactor();
		}
		if (board.isGameOver() == true){
			return board.evaluateBoard() * board.sideMove.evalFactor();
		}
		
		//Test if it is the final depth or the game is over
		if (depth == 0) {
						
			if (AIController.useTTEvals && oldEntry != null && oldEntry.zobrist == zHash && oldEntry.nodeType == HashEntry.PV_NODE && (oldEntry.depth+board.halfMoveClock) < 100 && !pvCausesRepetition()){
				return oldEntry.eval; //passes up the pre-computed evaluation
			}else{
				if(AIController.quiescenceSearch){
					return qSearch(alpha, beta);
				}else{
					AIC.staticComputations++;
					if (AIC.stopSearching){
						return 0;
					}

					return board.evaluateBoard() * board.sideMove.evalFactor();
				}
				
			}
		}
		
		List<Move> orderedMoves = new ArrayList<Move>();
		List<Move> allAvailible = board.rawMoves();
		if (AIController.sortMoves){
			Collections.sort(allAvailible);
		}
		
		if(board.ply == 0){
			if (AIController.iterativeDeepeningMoveReordering) {
				if (AIC.bestRootMove != null && allAvailible.contains(AIC.bestRootMove.move)){
					orderedMoves.add(new Move(board, AIC.bestRootMove.move));
				}
			}
		}
		
		if(oldEntry != null //if there is an old entry
			&& oldEntry.zobrist == zHash){  //and the boards are the same
			
			if (AIController.useTTEvals && oldEntry.depth >= (depth) && (oldEntry.depth+board.halfMoveClock) < 100 && !pvCausesRepetition()){

				if(oldEntry.nodeType == HashEntry.PV_NODE){ //the evaluated node is PV
					AIC.usedTTCount++;
					if (board.ply != 0){
						return oldEntry.eval; //passes up the pre-computed evaluation
					}else{
						AIC.bestRootMove = new MoveAndScore(new Move(board, oldEntry.move), oldEntry.eval);
						return oldEntry.eval;
					}
				}else if(board.ply != 0 && oldEntry.nodeType == HashEntry.CUT_NODE && oldEntry.eval >= beta){ //beta cutoff
					AIC.usedTTCount++;
					return oldEntry.eval;
				}else if(board.ply != 0 && oldEntry.nodeType == HashEntry.ALL_NODE && oldEntry.eval <= alpha){ //beta cutoff
					AIC.usedTTCount++;
					return oldEntry.eval;
				}else{
					if (!orderedMoves.contains(oldEntry.move) && allAvailible.contains(oldEntry.move)){
						orderedMoves.add(new Move(board, oldEntry.move)); // make the move be computed first
					}
				}
			}else if (AIController.TTMoveReordering){ // if the entry we have is not accurate enough
				if (!orderedMoves.contains(oldEntry.move) && allAvailible.contains(oldEntry.move)){
					orderedMoves.add(new Move(board, oldEntry.move)); // make the move be computed first
				}
			}
		
		}
		
		if (AIController.killerHeuristic){
			for (Move m: getKillerMoves(board.ply)){
				if (!orderedMoves.contains(m) && allAvailible.contains(m)){
					orderedMoves.add(new Move(board, allAvailible.get(allAvailible.indexOf(m))));
				}
			}
		}
		
		for (Move m: allAvailible){
			if (!orderedMoves.contains(m)){
				orderedMoves.add(m);
			}
		}
	
		//Collections.shuffle(movesAvailible); //should give out same move

		int maxValue = -INFINITY;
		Move bestMove = null;
		boolean newAlpha = false;
		int moveNum = 0;
		
		for (Move move: orderedMoves) {
			if (!board.isLegal(move)){
				continue;
			}
			moveNum++;	
			move.execute();
			int currentScore;
			if (!newAlpha || !AIController.PVSearch){
				board.ply++;
				currentScore = -negaMax( -beta, -alpha, depth-1);
				board.ply--;
			}else{
				board.ply++;
				currentScore = -negaMax( -alpha-1, -alpha, depth-1); //Do a null window search
				board.ply--;
				if (currentScore > alpha && currentScore < beta){ //if move has the possibility of increasing alpha 
					int previousTotalNodes = AIC.totalNodes;
					board.ply++;
					currentScore = -negaMax( -beta, -alpha, depth-1); //do a full-window search
					board.ply--;
					AIC.researches += AIC.totalNodes-previousTotalNodes;
				}
				
			}
			
			// reset board
			move.reverse();			
			
			if (AIC.stopSearching){
				return 0;
			}
			
			
			if (currentScore > maxValue) {
				bestMove = move;
				if (board.ply == 0){
					HashEntry newEntry = new HashEntry(zHash, depth, currentScore, HashEntry.PV_NODE, new Move(board, move));
					TranspositionTable.addEntry(newEntry);
					AIC.bestRootMove = new MoveAndScore(new Move(board, bestMove), currentScore);
				}
			}
			

			maxValue = Math.max(currentScore, maxValue);
			
			// If move is too good to be true and pruning needs to been done, don't evaluate the rest of the
			// sibling state
			if (maxValue >= beta){
				if (moveNum == 1){
					AIC.fhf++;
				}
				AIC.fh++;
				addKillerMove(board.ply, new Move(board, move));
				break;
			}
			
			if (currentScore > alpha) newAlpha = true; //if alpha increased, next search should be a null window search
			alpha = Math.max(currentScore, alpha); 
			
		}
		//Push entry to the TranspositionTable
		HashEntry newEntry = null;
		if(maxValue >= beta){
			newEntry = new HashEntry(zHash, depth, maxValue, HashEntry.CUT_NODE, new Move(board, bestMove));
		}else if (!newAlpha){
			newEntry = new HashEntry(zHash, depth, maxValue, HashEntry.ALL_NODE, new Move(board, bestMove));
		}else{
			newEntry = new HashEntry(zHash, depth, maxValue, HashEntry.PV_NODE, new Move(board, bestMove));
		}
		TranspositionTable.addEntry(newEntry); //add the move entry. only gets placed if eval is higher than previous entry

		if (orderedMoves.size() == 1 && board.ply == 0){
			AIC.stopSearching = true;
		}
		return maxValue;
		
	}
}
