package chessbot;

import java.util.*;

public class AlphaBetaMinimax {
	
	Board board;
	int maxDepth = 4;
	Move bestMove;
	
	
	public AlphaBetaMinimax(Board board){
		this.board = board;
		miniMaxAlgorithm(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, false);
	}
	
	int miniMaxAlgorithm(int alpha, int beta, int depth, boolean player){
		if(beta<=alpha){
			if(player == true){
				return Integer.MAX_VALUE;
			}else{
				return Integer.MIN_VALUE; 
			}
		}
		
		if (depth == maxDepth || board.isGameOver() == true){
			return board.evaluateBoard();
		}
		
		List<Move> movesAvailible = board.allMoves();
        
        if(movesAvailible.isEmpty()) return 0;
        
		int maxValue = Integer.MIN_VALUE, minValue = Integer.MAX_VALUE;
		
		for (int i = 0; i<movesAvailible.size(); i++){
			Move move = movesAvailible.get(i);
			Move reverseMove = new Move(move.piece.position, move.piece);
			if (move.piece.isPlayer == player){
				int currentScore = 0;
				board.makeMove(move);
				
				if (player == false){
					currentScore = miniMaxAlgorithm(alpha, beta, depth + 1, true);
					if(depth == 0 && currentScore > maxValue)
						bestMove = move;
						
					maxValue = Math.max(currentScore, maxValue);
					
					alpha = Math.max(maxValue, alpha);
					
				}else if (player == true){
					
					currentScore = miniMaxAlgorithm(alpha, beta, depth + 1, false);
					minValue = Math.min(currentScore, minValue);
					
					beta = Math.min(minValue, beta);
				}
				
				//reset board
				board.makeMove(reverseMove);
	            
	            //If a pruning has been done, don't evaluate the rest of the sibling states
	            if(currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE) break;
			}
		}
		return player == false ? maxValue : minValue;
	}
	
	
}
