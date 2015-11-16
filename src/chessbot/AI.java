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
		for (currentDepth = 1; currentDepth <= AIC.DEPTH_LIMIT; currentDepth++) {

			AIC.evaluateToDepth = currentDepth;
			List<HashEntry> NewPVLine = new ArrayList<HashEntry>();
			negaMax(-INFINITY, INFINITY, 0, currentDepth, NewPVLine);
			if (AIC.stopSearching) {
				return;
			}
			AIC.depthsPV.add("PV at depth " + currentDepth + ": " + new PV(board));
		}

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
		search();

		AIC.depthsPV.add("PV at final depth " + AIC.evaluateToDepth + ": " + new PV(board));

		for (String s : AIC.depthsPV) {
			System.out.println(s);
		}
		System.out.println("PV line at final depth " + AIC.evaluateToDepth + ": " + AIC.PVLine.toString());
	}

	// Basic Quiescence Search
	int qSearch(int alpha, int beta) {
		AIC.totalNodes++;
		AIC.checkTimeLimit();
		// Increment the static computations
		AIC.staticComputations++;
		if (AIC.stopSearching){
			return 0;
		}

		int standPat = board.evaluateBoard() * (board.playerMove ? -1 : 1);
		
		long zHash = Zobrist.getZobristHash(board);
		boolean newAlpha = false;

		// Update the alpha if the position is an improvement
		if (alpha < standPat) {
			newAlpha = true;
			alpha = standPat;
		}

		int maxValue = standPat;
		// Examine all captures
		int moveNum = 0;
		Move bestMove = null;
		List<Move> moves = new ArrayList<Move>();
		if (board.isCheck(board.playerMove)){
			moves = board.allMoves(board.playerMove);
			if (AIC.sortMoves){
				Collections.sort(moves);
			}
		}else{
			// Look for a beta cutoff
			if (standPat >= beta) {
				return standPat; // Fail-soft
			}
			
			List<Move> captureMoves = board.captureMoves(board.playerMove);
			List<Move> checkMoves = board.checkMoves(board.playerMove);
			if (AIC.sortMoves){
				Collections.sort(captureMoves);
				Collections.sort(checkMoves);
			}
			moves.addAll(captureMoves);
			//moves.addAll(checkMoves);
		}
		for (Move m : moves) {
			moveNum++;
			
			m.execute();
			int currentScore = -qSearch(-beta, -alpha);
			m.reverse();
			
			if (AIC.stopSearching){
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
			HashEntry newEntry = new HashEntry(zHash, 0, maxValue, HashEntry.PV_NODE, new Move(bestMove));
			TranspositionTable.addEntry(newEntry); //add the move entry. only gets placed if eval is higher than previous entry
		}

		return maxValue;

	}

	int negaMax(int alpha, int beta, int depth, int maxDepth, List<HashEntry> parentLine){
		AIC.totalNodes++;
		AIC.checkTimeLimit();
		
		//Get hash of current board
		long zHash = Zobrist.getZobristHash(board);
		int index = Zobrist.getIndex(zHash);
		
		//find entry with same index
		HashEntry oldEntry = TranspositionTable.trans.get(index);
		
		//Initialize the PV line for this node
		List<HashEntry> nodeLine = new ArrayList<HashEntry>();

		//Test if it is the final depth or the game is over
		if (depth == maxDepth || board.isGameOver() == true) {
			
			if (AIC.computationsAtDepth.get(depth) == null){
				AIC.computationsAtDepth.put(depth, 0);
			}
			
			AIC.computationsAtDepth.put(depth, AIC.computationsAtDepth.get(depth) + 1);
			
			if (AIC.useTTEvals && oldEntry != null && oldEntry.zobrist == zHash && oldEntry.nodeType == HashEntry.PV_NODE){
				parentLine.clear();
				return oldEntry.eval; //passes up the pre-computed evaluation
			}else{
				if(AIC.quiescenceSearch){
					return qSearch(alpha, beta);
				}else{
					AIC.staticComputations++;
					if (AIC.stopSearching){
						return 0;
					}

					return board.evaluateBoard() * ( board.playerMove ? -1 : 1 );
				}
				
			}
		}
		
		List<Move> orderedMoves = new ArrayList<Move>();
		List<Move> allAvailible = board.allMoves(board.playerMove);
		if (AIC.sortMoves){
			Collections.sort(allAvailible);
		}
		
		if(depth == 0){
			if (AIC.iterativeDeepeningMoveReordering) {
				if (AIC.bestRootMove != null && allAvailible.contains(AIC.bestRootMove.move)){
					orderedMoves.add(new Move(AIC.bestRootMove.move));
				}
			}
		}
		
		if(oldEntry != null //if there is an old entry
			&& oldEntry.zobrist == zHash){  //and the boards are the same
			
			if (AIC.useTTEvals && oldEntry.depthLeft >= (maxDepth - depth) ){
				if(oldEntry.nodeType == HashEntry.PV_NODE){ //the evaluated node is PV
					if (depth != 0){
						parentLine.clear();
						return oldEntry.eval; //passes up the pre-computed evaluation
					}else{
						AIC.PVLine.clear();
						AIC.PVLine.add(new MoveAndScore(new Move(oldEntry.move), oldEntry.eval));
						AIC.bestRootMove = new MoveAndScore(new Move(oldEntry.move), oldEntry.eval);
						return oldEntry.eval;
					}
				}else if(depth != 0 && oldEntry.nodeType == HashEntry.CUT_NODE && oldEntry.eval >= beta){ //beta cutoff
					return oldEntry.eval;
				}else if(depth != 0 && oldEntry.nodeType == HashEntry.ALL_NODE && oldEntry.eval <= alpha){ //beta cutoff
					return oldEntry.eval;
				}else{
					if (!orderedMoves.contains(oldEntry.move) && allAvailible.contains(oldEntry.move)){
						orderedMoves.add(new Move(oldEntry.move)); // make the move be computed first
					}
				}
			}else if (AIC.TTMoveReordering){ // if the entry we have is not accurate enough
				if (!orderedMoves.contains(oldEntry.move) && allAvailible.contains(oldEntry.move)){
					orderedMoves.add(new Move(oldEntry.move)); // make the move be computed first
				}
			}
		
		}
		
		if (AIC.killerHeuristic){
			for (Move m: getKillerMoves(depth)){
				if (!orderedMoves.contains(m) && allAvailible.contains(m)){
					orderedMoves.add(new Move(m));
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
			moveNum++;	
			move.execute();
			int currentScore;
			if (!newAlpha || !AIC.PVSearch){
				currentScore = -negaMax( -beta, -alpha, depth + 1, maxDepth, nodeLine);
			}else{
				currentScore = -negaMax( -alpha-1, -alpha, depth + 1, maxDepth, nodeLine); //Do a null window search
				
				if (currentScore > alpha && currentScore < beta){ //if move has the possibility of increasing alpha 
					currentScore = -negaMax( -beta, -alpha, depth + 1, maxDepth, nodeLine); //do a full-window search
				}
				
			}
			
			// reset board
			move.reverse();			
			
			if (AIC.stopSearching){
				return 0;
			}
			
			
			if (currentScore > maxValue) {
				bestMove = move;
				if (depth == 0){
					HashEntry newEntry = new HashEntry(zHash, maxDepth - depth, currentScore, HashEntry.PV_NODE, new Move(move));
					TranspositionTable.addEntry(newEntry);
					parentLine.clear();
					parentLine.add(newEntry);
					parentLine.addAll(nodeLine);
					AIC.PVLine.clear();
					for (HashEntry h: parentLine){
						AIC.PVLine.add(new MoveAndScore(h.move, h.eval));
						//TranspositionTable.addEntry(h);
					}
					AIC.bestRootMove = new MoveAndScore(new Move(bestMove), currentScore);
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
				addKillerMove(depth, new Move(move));
				break;
			}
			
			if (currentScore > alpha) newAlpha = true; //if alpha increased, next search should be a null window search
			alpha = Math.max(currentScore, alpha); 
			
		}
		//Push entry to the TranspositionTable
		HashEntry newEntry = null;
		if(maxValue >= beta){
			newEntry = new HashEntry(zHash, maxDepth - depth, maxValue, HashEntry.CUT_NODE, new Move(bestMove));
		}else if (!newAlpha){
			newEntry = new HashEntry(zHash, maxDepth - depth, maxValue, HashEntry.ALL_NODE, new Move(bestMove));

		}else{
			newEntry = new HashEntry(zHash, maxDepth - depth, maxValue, HashEntry.PV_NODE, new Move(bestMove));
			nodeLine.add(0, newEntry);
			parentLine.clear();
			parentLine.addAll(nodeLine);
		}
		TranspositionTable.addEntry(newEntry); //add the move entry. only gets placed if eval is higher than previous entry

		return maxValue;
		
	}
}
