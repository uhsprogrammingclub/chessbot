package chessbot;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class AlphaBetaMinimax {

	Board board;
	int maxDepth = 2;
	List<MoveAndScore> rootsChildrenScore = new ArrayList<>();
	int staticComputations = 0;
	
	
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
		maxDepth = maxDepth +  ( player ? -1 : 1); //so depths are different
		negaMax(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, player);
	}
	
	
	int negaMax(int alpha, int beta, int depth, boolean player){
		if (alpha >= beta) {
			return Integer.MAX_VALUE;
		}
		
		if (depth == maxDepth || board.isGameOver(player) == true) {
			staticComputations++;
			return board.evaluateBoard() * ( player ? -1 : 1 );	
		}
		
		List<Move> movesAvailible = board.allMoves(player);
		
		if(depth == 0){
            rootsChildrenScore.clear();
		}
		int maxValue = Integer.MIN_VALUE;
		for (Move move: movesAvailible) {
			move.execute();
			int currentScore = -negaMax(-beta, -alpha, depth + 1, !player);
			
			if (depth == 0) {
				rootsChildrenScore.add(new MoveAndScore(move, currentScore));
			}

			maxValue = Math.max(currentScore, maxValue);
			alpha = Math.max(maxValue, alpha);

			// reset board
			move.reverse();
			// If a pruning has been done, don't evaluate the rest of the
			// sibling states
			if (currentScore == Integer.MIN_VALUE){
				//System.out.println("Pruning at depth "+depth+": " + move);
				break;
			}
				
		}
		return maxValue;
	}

}
