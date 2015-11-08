package chessbot;

import java.util.*;

public class AlphaBetaMinimax {
	
	//Speed up techniques
	boolean PVSearch = true;
	boolean killerHeuristic = true;
	boolean TTMoveReordering = true;
	boolean useTTEvals = true;
	boolean iterativeDeepeningMoveReordering = true;

	Board board;
	int maxComputations = 20000;
	List<MoveAndScore> rootsChildrenScore = new ArrayList<>();
	List<MoveAndScore> currentRootsChildrenScore = new ArrayList<>();
	List<String> depthsPV = new ArrayList<>();
	int staticComputations = 0;
	int evaluateToDepth = 0;
	int finalDepth = 1;
	int movesEvaluated = 0;
	
	List<MoveAndScore> PVLine = new ArrayList<MoveAndScore>();
	
	List<Move> killerMoves = new ArrayList<Move>();
	
	Hashtable<Integer, Integer> computationsAtDepth = new Hashtable<Integer, Integer>(100);
	
	int MIN = Integer.MIN_VALUE+1;
	int MAX = Integer.MAX_VALUE;
	
	void addKillerMove(int depth, Move move){
		if (killerMoves.size() < (depth+1)*3){
			for (int i = killerMoves.size(); i<(depth+1)*3; i++){
				killerMoves.add(i, null);
			}
		}
		
		Move m1 = killerMoves.get(depth*3);
		Move m2 = killerMoves.get(depth*3+1);
		Move m3 = killerMoves.get(depth*3+2);
		
		if (move.equals(m1)){
			return;
		}else if (move.equals(m2)){
			killerMoves.set(depth*3, m2);
			killerMoves.set(depth*3+1, m1);
			return;
		}else if (move.equals(m3)){
			killerMoves.set(depth*3, m3);
			killerMoves.set(depth*3+1, m1);
			killerMoves.set(depth*3+2, m2);
			return;
		}else{
			killerMoves.set(depth*3, move);
			killerMoves.set(depth*3+1, m1);
			killerMoves.set(depth*3+2, m2);
		}
		
	}
	List<Move> getKillerMoves(int depth){
		if (killerMoves.size() < (depth+1)*3){
			for (int i = killerMoves.size(); i<(depth+1)*3; i++){
				killerMoves.add(i, null);
			}
		}
		
		List<Move> moves = new ArrayList<Move>();
		
		Move m1 = killerMoves.get(depth*3);
		Move m2 = killerMoves.get(depth*3+1);
		Move m3 = killerMoves.get(depth*3+2);
		
		if (m1 != null){
			moves.add(m1);
		}
		if (m2 != null){
			moves.add(m2);
		}
		if (m3 != null){
			moves.add(m3);
		}
		return moves;
	}
	
	public Move bestMove(){		
		Collections.sort(rootsChildrenScore);

		return new Move(rootsChildrenScore.get(0).move);
	}

	public AlphaBetaMinimax(Board board) {
		this.board = board;
		finalDepth = progressiveDeepening();
		
		depthsPV.add("PV at final depth " + finalDepth + ": "+ new PV(board));
		
		for(String s : depthsPV){
			System.out.println(s);
		}
		//System.out.println("PV line at final depth " + finalDepth + ": "+ PVLine.toString());
	}
	
	int progressiveDeepening(){
		int depth;
		for (depth = 1; depth < 25; depth++){
			evaluateToDepth = depth;
			double result = negaMax(MIN, MAX, 0, depth);
			if (result != 404 ){
				rootsChildrenScore.clear();
				rootsChildrenScore.addAll(currentRootsChildrenScore);
				Collections.sort(rootsChildrenScore);
			}else{
				for (MoveAndScore m : currentRootsChildrenScore){
					if (rootsChildrenScore.contains(m)){
						rootsChildrenScore.remove(m);
					}
				}
				rootsChildrenScore.addAll(currentRootsChildrenScore);
				Collections.sort(rootsChildrenScore);
				return depth;
			}
			
			depthsPV.add("PV at depth " + depth + ": "+ new PV(board));
			//depthsPV.add("PV line at depth " + depth + ": "+ PVLine.toString());
		}
		
		return depth;
	}
	
	
	double negaMax(double alpha, double beta, int depth, int maxDepth){
		if (depth == maxDepth || board.isGameOver() == true) {
			if (computationsAtDepth.get(depth) == null){
				computationsAtDepth.put(depth, 0);
			}
			computationsAtDepth.put(depth, computationsAtDepth.get(depth) + 1);
			
			staticComputations++;
			if (staticComputations > maxComputations) return 404; //arbitrary number to end the search
			return board.evaluateBoard() * ( board.playerMove ? -1 : 1 );	
		}
		if (PVLine.size() < depth+1){
			PVLine.add(depth, null);
		}
		
		//Get hash of current board
		long zHash = Zobrist.getZobristHash(board);
		int index = Zobrist.getIndex(zHash);
		//find entry with same index
		HashEntry oldEntry = TranspositionTable.trans.get(index);
		
		List<Move> movesAvailible = new ArrayList<Move>();
		List<Move> allAvailible = board.allMoves(board.playerMove);
		
		if(oldEntry != null //if there is an old entry
			&& oldEntry.zobrist == zHash){  //and the boards are the same
			
			if (useTTEvals && oldEntry.depthLeft >= (maxDepth - depth) ){
				if(depth != 0 && oldEntry.nodeType == HashEntry.PV_NODE){ //the evaluated node is PV
					return oldEntry.eval; //passes up the pre-computed evaluation
				}else if(depth != 0 && oldEntry.nodeType == HashEntry.CUT_NODE && oldEntry.eval > beta){ //beta cutoff
					return oldEntry.eval;
				}else if(depth != 0 && oldEntry.nodeType == HashEntry.ALL_NODE && oldEntry.eval < alpha){ //beta cutoff
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

		double maxValue = MIN;
		Move bestMove = null;
		boolean newAlpha = false;
		for (Move move: movesAvailible) {
					
			move.execute();
			double currentScore;
			if (!newAlpha || !PVSearch){
				currentScore = -negaMax( -beta, -alpha, depth + 1, maxDepth);
				movesEvaluated++;
			}else{
				currentScore = -negaMax( -alpha-0.00001, -alpha, depth + 1, maxDepth); //Do a null window search
				movesEvaluated++;
				if (currentScore > alpha && currentScore < beta){ //if move has the possibility of increasing alpha 
					currentScore = -negaMax( -beta, -alpha, depth + 1, maxDepth); //do a full-window search
					movesEvaluated++;
				}
			}
			
			if (currentScore == -404){
				move.reverse();
				if (depth == 0 && bestMove != null){
					HashEntry newEntry = new HashEntry(zHash, maxDepth - depth, maxValue, HashEntry.PV_NODE, bestMove);
					PVLine.set(depth, new MoveAndScore(bestMove, maxValue));
					TranspositionTable.addEntry(newEntry);
				}
				return 404;
			}
			
			if (currentScore > maxValue) {
				bestMove = move;
				if (depth == 0){
					currentRootsChildrenScore.add(new MoveAndScore(bestMove, currentScore));
				}
			}
			

			maxValue = Math.max(currentScore, maxValue);
			if (currentScore > alpha) newAlpha = true; //if alpha increased, next search should be a null window search
			alpha = Math.max(currentScore, alpha); 
			
			// reset board
			move.reverse();
			
			// If move is too good to be true and pruning needs to been done, don't evaluate the rest of the
			// sibling states
			if (maxValue >= beta){
				addKillerMove(depth, move);
				break;
			}
			
		}
		//Push entry to the TranspositionTable
		HashEntry newEntry = null;
		if(maxValue >= beta){
			newEntry = new HashEntry(zHash, maxDepth - depth, beta, HashEntry.CUT_NODE, new Move(bestMove));
		}else if (!newAlpha){
			newEntry = new HashEntry(zHash, maxDepth - depth, alpha, HashEntry.ALL_NODE, new Move(bestMove));
		}else if (beta - alpha > 0.00001){
			newEntry = new HashEntry(zHash, maxDepth - depth, maxValue, HashEntry.PV_NODE, new Move(bestMove));
			PVLine.set(depth, new MoveAndScore(bestMove, maxValue));
		}
		TranspositionTable.addEntry(newEntry); //add the move entry. only gets placed if eval is higher than previous entry
		return maxValue;
		
	}

}
