package chessbot;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class AlphaBetaMinimax {

	Board board;
	int maxComputations = 5000;
	List<MoveAndScore> rootsChildrenScore = new ArrayList<>();
	List<MoveAndScore> currentRootsChildrenScore = new ArrayList<>();
	int staticComputations = 0;
	int currentStaticComputations = 0;
	int finalDepth = 1;
	
	int MIN = Integer.MIN_VALUE+1;
	int MAX = Integer.MAX_VALUE;
	
	
	public Move bestMove(){
		
		List<Move>primeMoves = new ArrayList<>();
		
		Collections.sort(rootsChildrenScore);
		
		for(MoveAndScore ms : rootsChildrenScore){
			if(ms.score == rootsChildrenScore.get(0).score){
				primeMoves.add(ms.move);
			}else{
				break;
			}
		}
		
		int rand = ThreadLocalRandom.current().nextInt(0, primeMoves.size());
		return primeMoves.get(rand);
	}

	public AlphaBetaMinimax(Board board, boolean player) {
		this.board = board;
		//maxDepth = maxDepth +  ( player ? -1 : 1); //so depths are different
		//negaMax(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, player);
		finalDepth = progressiveDeepening(player);
	}
	
	int progressiveDeepening(boolean player){
		int depth;
		for (depth = 1; depth < 25; depth++){
			double result = negaMax(MIN, MAX, 0, player, depth);
			if (result != 404 ){
				staticComputations = currentStaticComputations;
				rootsChildrenScore.clear();
				rootsChildrenScore.addAll(currentRootsChildrenScore);
				Collections.sort(rootsChildrenScore);
			}else{
				return depth - 1;
			}
		}
		return depth;
	}
	
	
	double negaMax(double alpha, double beta, int depth, boolean player, int maxDepth){
		if (alpha >= beta) {
			return MAX;
		}
		
		if (depth == maxDepth || board.isGameOver(player) == true) {
			currentStaticComputations++;
			if (currentStaticComputations > maxComputations) return 404; //arbitrary number to end the search
			return board.evaluateBoard() * ( player ? -1 : 1 );	
		}
		
		List<Move> movesAvailible;
		
		//Get hash of current board
		long zHash = Zobrist.getZobristHash(board);
		int index = (int)(zHash % TranspositionTable.hashSize);
		//find entry with same index
		HashEntry oldEntry = TranspositionTable.trans.get(index);
		
		if(oldEntry != null //if there is an old entry
				&& oldEntry.zobrist == zHash //and the boards are the same
				&& oldEntry.depthLeft >= (maxDepth - depth) //the the depth of the entry is not less than what we have to go through
				&& oldEntry.player == player){ //and it's the same player's move
			if (depth != 0){
				return oldEntry.eval; //passes up the precomputed evaluation
			}else{
				currentRootsChildrenScore.add(new MoveAndScore(oldEntry.move, oldEntry.eval)); //puts the precomputed move into root moves array
				return 0;
			}
		}
		
		if(depth == 0){
			currentStaticComputations = 0;
			currentRootsChildrenScore.clear();
			movesAvailible = new ArrayList<Move>();
			Collections.sort(rootsChildrenScore);
			for (MoveAndScore ms: rootsChildrenScore){
				movesAvailible.add(ms.move);
			}
			List<Move> allAvailible = board.allMoves(player);
			for (Move m: allAvailible){
				if (!movesAvailible.contains(m)){
					movesAvailible.add(m);
				}
			}
			
		}else{
			movesAvailible = board.allMoves(player);
		}
		
		double maxValue = MIN;
		for (Move move: movesAvailible) {
			
			move.execute();
			
			double currentScore;
			currentScore = -negaMax( -beta, -alpha, depth + 1, !player, maxDepth);
			
			if (currentScore == -404){
				move.reverse();
				return 404;
			}
			
			if (depth == 0 && currentScore > maxValue) {
				currentRootsChildrenScore.add(new MoveAndScore(move, currentScore));
			}

			maxValue = Math.max(currentScore, maxValue);
			alpha = Math.max(currentScore, alpha);
			
			//Push entry to the TranspositionTable
			HashEntry newEntry = new HashEntry(zHash, maxDepth - depth, currentScore, move, player);
			TranspositionTable.addEntry(newEntry); //add the move entry. only gets placed if eval is higher than previous entry

			// reset board
			move.reverse();
			
			// If a pruning has been done, don't evaluate the rest of the
			// sibling states
			if (currentScore == MIN){
				break;
			}
				
		}
		return maxValue;
	}

}
