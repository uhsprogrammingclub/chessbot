package chessbot;

import java.util.*;

public class Game {
	
	//Default the FEN set up to the standard position
	static String setup = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		
	//Scanner to take Human input
	static Scanner s = new Scanner(System.in);
	
	//Boolean that determines whether the AI plays itself
	static boolean botVBot = false;
	static boolean playerMovesFirst = true;
	
	//Initialize the grid GUI layout
	static GridLayoutManager gui;
	
	//Points to be created by the GUI
	static volatile Point squareFrom = null;
	static volatile Point squareTo = null;
	
	//The current board of the game
	Board b;
	
	public void setFEN(String FEN){
		setup = FEN;
	}
	
	public void setBoard(String setup){
		b = Utils.boardFromFEN(setup);
	}
	
	public static void main(String args){
		
		Game g = new Game();
		g.init();
		
	}
	
	public void init(){
		
		setBoard(setup);
		initGUI();
		
		playerMovesFirst = b.playerMove;
		
		Zobrist.zobristFillArray();

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
	
	public void initGUI(){
		gui = new GridLayoutManager();
		gui.updateBoard(b);
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
	
	public int getBotMove(Board b){
		
		AlphaBetaMinimax ai = new AlphaBetaMinimax(b);
		Move move = ai.bestMove();
		move.execute();
		
		return b.evaluateBoard();
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
