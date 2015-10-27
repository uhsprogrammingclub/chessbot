package chessbot;

import java.util.*;

public class AlphaBetaMinimax {

	Board board;
	int maxComputations = 50000;
	List<MoveAndScore> rootsChildrenScore = new ArrayList<>();
	List<MoveAndScore> currentRootsChildrenScore = new ArrayList<>();
	int staticComputations = 0;
	int evaluateToDepth = 0;
	int finalDepth = 1;
	int movesEvaluated = 0;
	
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
		}else if (move.equals(m1)){
			killerMoves.set(depth*3, m2);
			killerMoves.set(depth*3+1, m1);
			return;
		}else if (move.equals(m1)){
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
		System.out.println("PV at final depth " + finalDepth + ": "+ new PV(board));
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
				rootsChildrenScore.addAll(currentRootsChildrenScore);
				Collections.sort(rootsChildrenScore);
				return depth;
			}
			System.out.println("PV at depth " + depth + ": "+ new PV(board));
		}
		
		return depth;
	}
	
	
	double negaMax(double alpha, double beta, int depth, int maxDepth){
		
		if (depth == maxDepth && board.isCheck(board.playerMove) && (maxDepth < evaluateToDepth + 1 )){
			maxDepth++; //look ahead if last move caused a check
		}
		
		if (depth == maxDepth || board.isGameOver() == true) {
			if (computationsAtDepth.get(depth) == null){
				computationsAtDepth.put(depth, 0);
			}
			computationsAtDepth.put(depth, computationsAtDepth.get(depth) + 1);
			
			staticComputations++;
			if (staticComputations > maxComputations) return 404; //arbitrary number to end the search
			return board.evaluateBoard() * ( board.playerMove ? -1 : 1 );	
		}
		
		//Get hash of current board
		long zHash = Zobrist.getZobristHash(board);
		int index = Zobrist.getIndex(zHash);
		//find entry with same index
		HashEntry oldEntry = TranspositionTable.trans.get(index);
		
		List<Move> movesAvailible = new ArrayList<Move>();
		
		if(oldEntry != null //if there is an old entry
			&& oldEntry.zobrist == zHash){  //and the boards are the same
			if (oldEntry.depthLeft >= (maxDepth - depth) && oldEntry.alpha <= alpha && oldEntry.beta >= beta){ //the the depth of the entry is not less than what we have to go through
				return oldEntry.eval; //passes up the pre-computed evaluation
			}else{ // if the entry we have is not accurate enough
				movesAvailible.add(new Move(oldEntry.move)); // make the move be computed first
			}
		}
		
		if(depth == 0){
			staticComputations = 0;
			currentRootsChildrenScore.clear();
			//add the moves from previous depth iteration with the highest moves in the first place. 
			Collections.sort(rootsChildrenScore);
			for (MoveAndScore ms: rootsChildrenScore){
				movesAvailible.add(new Move(ms.move));
			}
		}
		
		List<Move> allAvailible = board.allMoves(board.playerMove);
		
		for (Move m: getKillerMoves(depth)){
			if (allAvailible.contains(m)){
				movesAvailible.add(new Move(m));
			}
		}
		
		for (Move m: allAvailible){
			if (!movesAvailible.contains(m)){
				movesAvailible.add(m);
			}
		}
		
		double maxValue = MIN;
		Move bestMove = null;
		boolean bestAlpha = false;
		boolean nullWindow = false;
		if (alpha == beta-0.00001){
			nullWindow = true;
		}
		for (Move move: movesAvailible) {
			
			int desiredDepth = maxDepth;
			
			if (depth == maxDepth-1 //if last move to be made
				&& move.destinationPc.worth != 0 && (desiredDepth < evaluateToDepth + 1 )){ //and its a capture move
				desiredDepth++; //make sure there is another move
			}
			move.execute();
			
			double currentScore;
			if (!bestAlpha && !nullWindow){
				currentScore = -negaMax( -beta, -alpha, depth + 1, desiredDepth);
				movesEvaluated++;
			}else{
				currentScore = -negaMax( -alpha-0.00001, -alpha, depth + 1, desiredDepth); //Do a null window search
				movesEvaluated++;
				if (currentScore > alpha && currentScore < beta && !nullWindow){ //if move has the possibility of increasing alpha and this isn't a null window search
					currentScore = -negaMax( -beta, -alpha, depth + 1, desiredDepth); //do a full-window search
					movesEvaluated++;
				}
			}
			
			if (currentScore == -404){
				move.reverse();
				return 404;
			}
			
			if (currentScore > maxValue) {
				bestMove = move;
				if (depth == 0){
					currentRootsChildrenScore.add(new MoveAndScore(move, currentScore));
				}
			}

			maxValue = Math.max(currentScore, maxValue);
			if (alpha > currentScore) bestAlpha = true; //if alpha increased, next search should be a null window search
			alpha = Math.max(currentScore, alpha); 
			
			// reset board
			move.reverse();
			
			// If move is too good to be true and pruning needs to been done, don't evaluate the rest of the
			// sibling states
			if (maxValue >= beta){
				addKillerMove(depth, move);
				break;
			}
			if (nullWindow) break; //only do first move if null window search
			
		}
		//Push entry to the TranspositionTable
		if (!nullWindow){ //don't add entry to hash table because eval is not accurate
			HashEntry newEntry = new HashEntry(zHash, maxDepth - depth, maxValue, alpha, beta, bestMove);
			TranspositionTable.addEntry(newEntry); //add the move entry. only gets placed if eval is higher than previous entry
		}
		return maxValue;
	}

}
