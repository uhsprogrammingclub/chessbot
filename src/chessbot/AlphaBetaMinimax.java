package chessbot;

import java.util.*;

public class AlphaBetaMinimax {

	Board board;
	int maxComputations = 1000;
	List<MoveAndScore> rootsChildrenScore = new ArrayList<>();
	List<MoveAndScore> currentRootsChildrenScore = new ArrayList<>();
	int staticComputations = 0;
	int evaluateToDepth = 0;
	int finalDepth = 1;
	
	Hashtable<Integer, Integer> computationsAtDepth = new Hashtable<Integer, Integer>(100);
	
	int MIN = Integer.MIN_VALUE+1;
	int MAX = Integer.MAX_VALUE;
	
	
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
		if (alpha >= beta) {
			return MAX;
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
		for (Move m: allAvailible){
			if (!movesAvailible.contains(m)){
				movesAvailible.add(m);
			}
		}
		
		double maxValue = MIN;
		Move bestMove = allAvailible.get(0);
		for (Move move: movesAvailible) {
			
			int desiredDepth = maxDepth;
			
			if (depth == maxDepth-1 //if last move to be made
				&& move.destinationPc.worth != 0 && (desiredDepth - 2 < evaluateToDepth )){ //and its a capture move
				desiredDepth++; //make sure there is another move
			}
			move.execute();
			
			double currentScore;
			currentScore = -negaMax( -beta, -alpha, depth + 1, desiredDepth);
			
			if (currentScore == -404){
				move.reverse();
				return 404;
			}
			
			if (depth == 0 && currentScore > maxValue) {
				currentRootsChildrenScore.add(new MoveAndScore(move, currentScore));
				bestMove = move;
			}

			maxValue = Math.max(currentScore, maxValue);
			alpha = Math.max(currentScore, alpha);
			
			// reset board
			move.reverse();
			
			// If a pruning has been done, don't evaluate the rest of the
			// sibling states
			if (currentScore == MIN){
				break;
			}
			
		}
		//Push entry to the TranspositionTable
		HashEntry newEntry = new HashEntry(zHash, maxDepth - depth, maxValue, alpha, beta, bestMove);
		TranspositionTable.addEntry(newEntry); //add the move entry. only gets placed if eval is higher than previous entry
		
		return maxValue;
	}

}
