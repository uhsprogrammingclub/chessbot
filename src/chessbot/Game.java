//Initiation file for UHS Programming Chess Robot
//Try to limit number of methods and lines in this class; namely holds the main() method. 

package chessbot;

import java.util.*;

public class Game {
	
	//Forsyth-Edwards Notation (FEN) game setup default
	//static String setup = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"; 
	
	//Suicide setup
	static String setup = "rnbqkbnr/pppp1ppp/8/4p3/3PP3/8/PPP2PPP/RNBQKBNR b KQkq - 0 2";
	//static String setup = "rnb1kbnr/pppp1ppp/8/4p3/3PP2q/5N2/PPP2PPP/RNBQKB1R b KQkq - 0 1";
	//Forsyth-Edwards Notation (FEN) game setup test
	//static String setup = "r1bqkbnr/pppp1ppp/4p3/2P5/3Pn3/5N2/PP3PPP/RNBQK2R b KQkq - 0 7"; 

	//Horizon Effect Test #1
	//static String setup = "7K/8/8/R7/R7/8/P1rr4/7k b - - 0 1"; 
	
	//Horizon Effect Test #2
	//static String setup = "7k/1n1n4/2P5/8/5b2/8/7P/7K b - - 0 1"; 

	//static String setup = "rnbqkbnr/pppp1ppp/8/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 0 2"; 
		
	//Scanner to take Human input
	static Scanner s = new Scanner(System.in);
	
	//Boolean that determines whether the AI plays itself
	static boolean botVBot = false;
	static boolean playerMovesFirst = true;
	
	//Initialize the grid GUI layout
	static GridLayoutManager gui = new GridLayoutManager();
	
	//Points to be created by the GUI
	static volatile Point squareFrom = null;
	static volatile Point squareTo = null;
	
	public static void main(String[] args) {
		
		Board b = Utils.boardFromFEN(setup);
		playerMovesFirst = b.playerMove;
		
		Zobrist.zobristFillArray();

		gui.updateBoard(b);
		
		
		if (botVBot){
			botMakeMove(b);
		}else{
			if (playerMovesFirst){
				takePlayerMove(b);
			}else{
				botMakeMove(b);
			}
		}
	}

	static void takePlayerMove(Board b) {
		
		b.fullMoveCounter++;
		
		//Clear GUI input
		squareFrom = null;
		squareTo = null;
		
		//Make the GUI board active
		GridLayoutManager.setActive(true);
		
		Move move = null;
		List<Move> validMoves = b.allMoves(b.playerMove);

		
		while (true) {
			
			Point from = Game.squareFrom;
			Point to = Game.squareTo;
			
			if(from != null && to != null){
				
				//If it is a promotion move
				if(b.getPiece(from).symbol.equals("p") && (to.y == 7 || to.y == 0)){
					System.out.println("What piece would you like to promote to? Queen = q; Knight = n, etc.");
					String pieceStr = s.nextLine();
					move = new Move(b, from, to, pieceStr);
							
				}else{
					move = new Move(b, from, to, null);
				}
			
				boolean validMove = false;
				
				for (Move m : validMoves) {

					if (move.equals(m)) {
						validMove = true;
						break;
					}
				}
				
				if (validMove) {
					break;
				}else{
					System.out.println(move + " is an invalid move!");
					
					//Clear GUI input
					Game.squareFrom = null;
					Game.squareTo = null;
				}		
			}
			
		} //End of While loop
		
		System.out.println("Move: " + move);
		move.execute();
		
		gui.updateBoard(b);
		
		if (!b.isGameOver()) {
			botMakeMove(b);
		} else {
			System.out.println(b);
			System.out.println("Game Over!");
		}
		
	}

	static void botMakeMove(Board b) {
		
		//Make the GUI board inactive
		GridLayoutManager.setActive(false);
		
		System.out.println(b);
		System.out.println("Processing move...");
		
		long startTime = System.currentTimeMillis();
		AlphaBetaMinimax ai = new AlphaBetaMinimax(b);
		long endTime = System.currentTimeMillis();
		
		for(MoveAndScore m : ai.rootsChildrenScore){
			System.out.println("Root Move: " + m);
		}
		System.out.println("Time expended: " + (endTime-startTime)/1000.0);
		System.out.println("Final Depth: " + ai.finalDepth);
		System.out.println("Moves Evaluated: " + ai.movesEvaluated);
		System.out.println("Static computations at each depth: " + ai.computationsAtDepth);
		System.out.println("Total static computations: " + ai.staticComputations);
		System.out.println("Transposition Table Size: " + TranspositionTable.trans.size());
		
		Move move = ai.bestMove();
		
		System.out.println("\nBot: " +move);

		move.execute();
		
		//System.out.println("Pieces: ");
		
		for (Piece p : b.pieceList){
			if (p.alive == true && p.worth != 0){ 
				//System.out.print(p + ", ");
			}
		}
		gui.updateBoard(b);
		
		if (!b.isGameOver()) {
			if (botVBot){
				botMakeMove(b);
			}else{
				takePlayerMove(b);
			}
		} else {
			System.out.println(b);
			System.out.println("Game Over!");
		}
	}
	
	
}
