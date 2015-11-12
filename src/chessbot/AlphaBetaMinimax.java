package chessbot;

import java.util.*;

public class AlphaBetaMinimax {

	// Constants
	static final int MAX_COMPUTATIONS = 100000;
	static final double PRECISION = 0.00001;
	static final int DEPTH_LIMIT = 25;
	static final int MIN = -1000000000;
	static final int MAX = 1000000000;

	// Speed up techniques
	boolean PVSearch = true; // Using null window search
	boolean killerHeuristic = true; // Records 3 killer moves per depth to evaluate first
	boolean TTMoveReordering = true; // Transposition entries are evaluated first
	boolean useTTEvals = true; // Use previous TT entries when encountered
	boolean iterativeDeepeningMoveReordering = true; // Evaluate the best moves at the previous depth first
	boolean quiescenceSearch = true; // Complete basic quiescence search after finishing main search to counter horizon effect

	// Initialize board
	Board board;

	// Create arrays to hold root children 
	List<MoveAndScore> rootsChildrenScore = new ArrayList<>();
	List<MoveAndScore> currentRootsChildrenScore = new ArrayList<>();

	// To hold the principal variation at each depth (serves no functional purpose)
	List<String> depthsPV = new ArrayList<>();

	// Defaulting variables
	int staticComputations = 0;
	int evaluateToDepth = 0;
	int finalDepth = 1;
	int movesEvaluated = 0;

	//Initializing the PV Link lists
	List<MoveAndScore> PVLine = new ArrayList<MoveAndScore>();
	List<MoveAndScore> currentPVLine = new ArrayList<MoveAndScore>();

	//Initialize killerMoves list of Move objects
	List<Move> killerMoves = new ArrayList<Move>();

	//Hashtable to store number of computations at each depth (serves no functional purpose)
	Hashtable<Integer, Integer> computationsAtDepth = new Hashtable<Integer, Integer>(100);
	
	//Experimental method to hold computations at depth - not finished, but serves no functional purpose
	List<Integer> evaluationDepths = new ArrayList<Integer>();

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

	//Function that sorts then returns the first (theoretically the best) move from the root moves
	public Move bestMove() {
		Collections.sort(rootsChildrenScore);

		return new Move(rootsChildrenScore.get(0).move);
	}

	//Constructor for the class
	public AlphaBetaMinimax(Board board) {
		this.board = board;
		finalDepth = progressiveDeepening();

		depthsPV.add("PV at final depth " + finalDepth + ": " + new PV(board));

		for (String s : depthsPV) {
			System.out.println(s);
		}
		System.out.println("PV line at final depth " + finalDepth + ": " + PVLine.toString());
	}

	//Function that calls the main Negamax function
	//Increments the maximum depth allowed until total static computations is exceeded
	int progressiveDeepening() {
		int depth;
		for (depth = 1; depth < DEPTH_LIMIT; depth++) {

			// Add entry to the array to record static computations at each
			// depth
			evaluationDepths.add(depth - 1, 0);

			evaluateToDepth = depth;
			List<HashEntry> NewPVLine = new ArrayList<HashEntry>();
			int result = negaMax(MIN, MAX, 0, depth, NewPVLine);
			if (result != 40400) {
				rootsChildrenScore.clear();
				rootsChildrenScore.addAll(currentRootsChildrenScore);
				Collections.sort(rootsChildrenScore);
				PVLine.clear();
				PVLine.addAll(currentPVLine);
			} else {
				for (MoveAndScore m : currentRootsChildrenScore) {
					if (rootsChildrenScore.contains(m)) {
						rootsChildrenScore.remove(m);
					}
				}
				rootsChildrenScore.addAll(currentRootsChildrenScore);
				Collections.sort(rootsChildrenScore);

				if (PVLine.size() == 0 || PVLine.get(0).move.equals(currentPVLine.get(0).move)
						|| PVLine.get(0).score < currentPVLine.get(0).score) {
					PVLine.clear();
					PVLine.addAll(currentPVLine);
				}
				return depth;
			}

			depthsPV.add("PV at depth " + depth + ": " + new PV(board));
		}

		return depth;
	}

	// Basic Quiescence Search
	int qSearch(int alpha, int beta) {

		// Increment the static computations
		staticComputations++;
		if (staticComputations > MAX_COMPUTATIONS)
			return 40400;

		int standPat = board.evaluateBoard() * (board.playerMove ? -1 : 1);

		// Look for a beta cutoff
		if (standPat >= beta) {
			return standPat; // Fail-soft
		}

		// Update the alpha if the position is an improvement
		if (alpha < standPat) {
			alpha = standPat;
		}

		// Examine all captures
		for (Move m : board.loudMoves(board.playerMove)) {

			m.execute();
			int currentScore = -qSearch(-beta, -alpha);
			m.reverse();

			if (currentScore >= beta) {
				return currentScore;
			}

			alpha = Math.max(currentScore, alpha);

		}

		return alpha;

	}

	int negaMax(int alpha, int beta, int depth, int maxDepth, List<HashEntry> parentLine){
		
		//Get hash of current board
		long zHash = Zobrist.getZobristHash(board);
		int index = Zobrist.getIndex(zHash);
		
		//find entry with same index
		HashEntry oldEntry = TranspositionTable.trans.get(index);
		
		//Initialize the PV line for this node
		List<HashEntry> nodeLine = new ArrayList<HashEntry>();

		//Test if it is the final depth or the game is over
		if (depth == maxDepth || board.isGameOver() == true) {
			
			if (computationsAtDepth.get(depth) == null){
				computationsAtDepth.put(depth, 0);
			}
			
			computationsAtDepth.put(depth, computationsAtDepth.get(depth) + 1);
			
			if (oldEntry != null && oldEntry.zobrist == zHash && oldEntry.nodeType == HashEntry.PV_NODE){
				parentLine.clear();
				return oldEntry.eval; //passes up the pre-computed evaluation
			}else{
				
				return quiescenceSearch ? qSearch(-beta, -alpha) : board.evaluateBoard() * ( board.playerMove ? -1 : 1 );
				
			}
			
		}

		
		if (PVLine.size() < depth+1){
			PVLine.add(depth, null);
		}
		
		List<Move> movesAvailible = new ArrayList<Move>();
		List<Move> allAvailible = board.allMoves(board.playerMove);
		
		if(oldEntry != null //if there is an old entry
			&& oldEntry.zobrist == zHash){  //and the boards are the same
			
			if (useTTEvals && oldEntry.depthLeft >= (maxDepth - depth) ){
				if(oldEntry.nodeType == HashEntry.PV_NODE){ //the evaluated node is PV
					if (depth != 0){
						parentLine.clear();
						return oldEntry.eval; //passes up the pre-computed evaluation
					}else{
						currentPVLine.clear();
						currentPVLine.add(new MoveAndScore(new Move(oldEntry.move), oldEntry.eval));
						currentRootsChildrenScore.clear();
						currentRootsChildrenScore.add(new MoveAndScore(new Move(oldEntry.move), oldEntry.eval));
						return oldEntry.eval;
					}
				}else if(depth != 0 && oldEntry.nodeType == HashEntry.CUT_NODE && oldEntry.eval >= beta){ //beta cutoff
					return oldEntry.eval;
				}else if(depth != 0 && oldEntry.nodeType == HashEntry.ALL_NODE && oldEntry.eval <= alpha){ //beta cutoff
					return oldEntry.eval;
				}else{
					if (!movesAvailible.contains(oldEntry.move) && allAvailible.contains(oldEntry.move)){
						movesAvailible.add(new Move(oldEntry.move)); // make the move be computed first
					}
				}
			}else if (TTMoveReordering){ // if the entry we have is not accurate enough
				if (!movesAvailible.contains(oldEntry.move) && allAvailible.contains(oldEntry.move)){
					movesAvailible.add(new Move(oldEntry.move)); // make the move be computed first
				}
			}
		
		}
		
		if(depth == 0){
			staticComputations = 0;
			currentRootsChildrenScore.clear();
			//add the moves from previous depth iteration with the highest moves in the first place. 
			Collections.sort(rootsChildrenScore);

			if (iterativeDeepeningMoveReordering) {
				for (MoveAndScore ms: rootsChildrenScore){
					if (!movesAvailible.contains(ms.move) && allAvailible.contains(ms.move)){
						movesAvailible.add(new Move(ms.move));
					}
				}
			}
		}
		
		
		if (killerHeuristic){
			for (Move m: getKillerMoves(depth)){
				if (!movesAvailible.contains(m) && allAvailible.contains(m)){
					movesAvailible.add(new Move(m));
				}
			}
		}
		
		for (Move m: allAvailible){
			if (!movesAvailible.contains(m)){
				movesAvailible.add(m);
			}
		}
	
		//Collections.shuffle(movesAvailible); //should give out same move

		int maxValue = MIN;
		Move bestMove = null;
		boolean newAlpha = false;
		
		for (Move move: movesAvailible) {
					
			move.execute();
			int currentScore;
			if (!newAlpha || !PVSearch){
				currentScore = -negaMax( -beta, -alpha, depth + 1, maxDepth, nodeLine);
				movesEvaluated++;
			}else{

				currentScore = -negaMax( -alpha-1, -alpha, depth + 1, maxDepth, nodeLine); //Do a null window search
				movesEvaluated++;
				
				if (currentScore > alpha && currentScore < beta){ //if move has the possibility of increasing alpha 
					currentScore = -negaMax( -beta, -alpha, depth + 1, maxDepth, nodeLine); //do a full-window search
					movesEvaluated++;
				}
				
			}
			
			// reset board
			move.reverse();			
			
			if (currentScore == -40400){
				return 40400;
			}
			
			
			if (currentScore > maxValue) {
				bestMove = move;
				if (depth == 0){
					HashEntry newEntry = new HashEntry(zHash, maxDepth - depth, currentScore, HashEntry.PV_NODE, new Move(move));
					parentLine.clear();
					parentLine.add(newEntry);
					parentLine.addAll(nodeLine);
					currentPVLine.clear();
					for (HashEntry h: parentLine){
						currentPVLine.add(new MoveAndScore(h.move, h.eval));
						TranspositionTable.addEntry(h);
					}
					currentRootsChildrenScore.add(new MoveAndScore(new Move(bestMove), currentScore));
				}
			}
			

			maxValue = Math.max(currentScore, maxValue);
			
			// If move is too good to be true and pruning needs to been done, don't evaluate the rest of the
			// sibling state
			if (maxValue >= beta){
				addKillerMove(depth, new Move(move));
				break;
			}
			
			if (currentScore > alpha) newAlpha = true; //if alpha increased, next search should be a null window search
			alpha = Math.max(currentScore, alpha); 
			
		}
		//Push entry to the TranspositionTable
		HashEntry newEntry = null;
		if(maxValue >= beta){
			newEntry = new HashEntry(zHash, maxDepth - depth, beta, HashEntry.CUT_NODE, new Move(bestMove));
		}else if (!newAlpha){
			newEntry = new HashEntry(zHash, maxDepth - depth, alpha, HashEntry.ALL_NODE, new Move(bestMove));

		}else{
			HashEntry entry = new HashEntry(zHash, maxDepth - depth, maxValue, HashEntry.PV_NODE, new Move(bestMove));
			nodeLine.add(0, entry);
			parentLine.clear();
			parentLine.addAll(nodeLine);
		}
		TranspositionTable.addEntry(newEntry); //add the move entry. only gets placed if eval is higher than previous entry

		return maxValue;
		
	}
}
