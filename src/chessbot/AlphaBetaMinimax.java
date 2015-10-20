package chessbot;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class AlphaBetaMinimax {

	Board board;
	int maxComputations = 2000;
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
			int result = negaMax(MIN, MAX, 0, player, depth);
			if (result != 404 ){
				staticComputations = currentStaticComputations;
				rootsChildrenScore.clear();
				rootsChildrenScore.addAll(currentRootsChildrenScore);
			}else{
				return depth - 1;
			}
		}
		return depth;
	}
	
	
	int negaMax(int alpha, int beta, int depth, boolean player, int maxDepth){
		if (alpha > beta) {
			return MAX;
		}
		
		if (depth == maxDepth || board.isGameOver(player) == true) {
			currentStaticComputations++;
			if (currentStaticComputations > maxComputations) return 404; //arbitrary number to end the search
			return board.evaluateBoard() * ( player ? -1 : 1 );	
		}
		
		List<Move> movesAvailible = board.allMoves(player);
		
		if(depth == 0){
			currentStaticComputations = 0;
			currentRootsChildrenScore.clear();
		}
		int maxValue = MIN;
		for (Move move: movesAvailible) {
			move.execute();
			int currentScore = -negaMax( -beta, -alpha, depth + 1, !player, maxDepth);
			if (currentScore == -404){
				move.reverse();
				return 404;
			}
			
			if (depth == 0) {
				currentRootsChildrenScore.add(new MoveAndScore(move, currentScore));
			}

			maxValue = Math.max(currentScore, maxValue);
			alpha = Math.max(currentScore, alpha);
			
			//Push entry to the TranspositionTable
			HashEntry entry = new HashEntry(Zobrist.getZobristHash(board), maxDepth - depth, currentScore, move);
			TranspositionTable.addEntry(entry);

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
