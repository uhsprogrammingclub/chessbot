package chessbot;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class AlphaBetaMinimax {

	Board board;
	int maxDepth = 2;
	List<MoveAndScore> rootsChildrenScore = new ArrayList<>();
	int staticComputations = 0;
	int playerBot = 1;
	
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

	public AlphaBetaMinimax(Board board, int playerBot) {
		this.board = board;
		this.playerBot = playerBot;
		maxDepth = maxDepth + playerBot;
		miniMaxAlgorithm(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, false);
	}

	int miniMaxAlgorithm(int alpha, int beta, int depth, boolean player) {
		
		if (beta <= alpha) {
			if (!player) {
				return Integer.MAX_VALUE;
			} else {
				return Integer.MIN_VALUE;
			}
		}

		if (depth == maxDepth || board.isGameOver() == true) {
			staticComputations++;
			return board.evaluateBoard()*playerBot;	
		}

		List<Move> movesAvailible = board.allMoves(playerBot == 1 ? player : !player);
		
		if(depth == 0){
            rootsChildrenScore.clear();
        }

		int maxValue = Integer.MIN_VALUE, minValue = Integer.MAX_VALUE;

		for (int i = 0; i < movesAvailible.size(); i++) {
			Move move = movesAvailible.get(i);
			int currentScore = 0;
			move.execute();
			if (!player) {
				currentScore = miniMaxAlgorithm(alpha, beta, depth + 1, true);
				
				if (depth == 0) {
					rootsChildrenScore.add(new MoveAndScore(move, currentScore));
				}

				maxValue = Math.max(currentScore, maxValue);

				alpha = Math.max(maxValue, alpha);

			} else if (player) {

				currentScore = miniMaxAlgorithm(alpha, beta, depth + 1, false);
				minValue = Math.min(currentScore, minValue);

				beta = Math.min(minValue, beta);
			}

			// reset board
			move.reverse();
			// If a pruning has been done, don't evaluate the rest of the
			// sibling states
			if (currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE){
				//System.out.println("Pruning at depth "+depth+": " + move);
				break;
			}
				
		}
		return player == false ? maxValue : minValue;
	}

}
