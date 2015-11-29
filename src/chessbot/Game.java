package chessbot;

import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chessbot.Board.Side;

public class Game {


	//Default the FEN set up to the standard position
	static String defaultSetup = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	static String setup = defaultSetup; 
	
	//Scanner to take Human input
	static Scanner s = new Scanner(System.in);
	
	//Boolean that determines whether the AI plays itself
	static Side firstSideToMove = Side.W;
	
	static boolean whiteIsBot = false;
	static boolean blackIsBot = true;
	
	//Initialize the grid GUI layout
	static GridLayoutManager gui;
	
	//Points to be created by the GUI
	static volatile Point squareFrom = null;
	static volatile Point squareTo = null;
	
	//The current board of the game
	Board b;
	
	//Current Opening
	static String currentECO = "000";
	
	public Game(){
		Zobrist.zobristFillArray();
	}
	
	public void setFEN(String FEN){
		setup = FEN;
	}
	
	public void setBoard(String setup){
		b = Utils.boardFromFEN(setup);
	}
	
	public static void main(String[] args){
		
		/*String f = "N1";
		System.out.println(f.charAt(1));
		System.out.println((int)f.charAt(1));
		System.exit(0);
		
		List<String> allMatches = new ArrayList<String>();
		 Matcher m = Pattern.compile("(?:[PNBRQK]?[a-h]?[1-8]?x?[a-h][1-8](?:\\=[PNBRQK])?|O(-?O)\\{1,2\\})[\\+#]?(\\s*[\\!\\?]+)?")
		     .matcher("Qxe5Pxe4+");
		 while (m.find()) {
		   allMatches.add(m.group());
		 }
		 
		 for(String s : allMatches){
			 System.out.println(s);
		 }
		 System.exit(0);*/
		
		Game g = new Game();
		//g.setFEN("3rr1k1/1pp2pp1/p6p/2bP1R2/1nP1p3/2R1P3/1P1NK3/8 b - - 0 0"); //- Reed Game
		//g.setFEN("rn1qkb1r/pp2pppp/2pp4/3nP3/3P2b1/5N2/PPP1BPPP/RNBQ1RK1 w kq - 0 7");
		//g.setFEN("r1bqkbnr/pppp1ppp/2n5/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 0 4"); //Fried Liver Attack
		//g.setFEN("1k2r3/1pp2pp1/p6p/P2K3P/4r3/8/7q/8 b - - 0 36");
		//g.setFEN("rnbqk2r/2p4p/1p2P1P1/7n/P2P4/P2P4/5P1P/RNBQKBNR b KQkq - 0 12");
		//g.setFEN("rnbqkbnr/ppppppp1/7p/8/8/2N5/PPPPPPPP/R1BQKBNR w KQkq - 0 2");
		//g.setFEN("8/8/p4k2/2r5/4K3/8/8/8 w - - 0 1");
		//g.setFEN("8/8/4k1p1/7p/8/6P1/5K2/8 w - - 0 1");
		//g.setFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1"); //Everything
		//g.setFEN("r3k2r/8/8/8/8/8/PPPPPPPP/4K3 b kq - 0 1");
		g.setFEN(setup);
		g.init();
		g.start();
		
	}
	
	public void init(){
		
		loadBitboards();
		setBoard(setup);
		initGUI();
		
		firstSideToMove = b.sideMove;
		
	}
	
	static void loadBitboards(){
		if (AIController.useBitBoards){
			BB.initPresets();
			MagicBitboards.generateOccupancyVariations(true);
			MagicBitboards.generateMoveDatabase(true);
			MagicBitboards.generateOccupancyVariations(false);
			MagicBitboards.generateMoveDatabase(false);
		}
		
	}
	
	public void start(){
		if (AIController.useOpeningBook){
			OpeningBook book = new OpeningBook(Utils.boardFromFEN(defaultSetup));
			Thread bookThread = new Thread(book, "Opening Book Thread");  
			bookThread.start();  
		}
		if (firstSideToMove == Side.W){
			whiteMove(b);
		}else{
			blackMove(b);
		}
	}
	
	public void initGUI(){
		gui = new GridLayoutManager();
		gui.updateBoard(b);
	}
	
	static void whiteMove(Board b) {
		if (whiteIsBot){
			botMakeMove(b);
		}else{
			takePlayerMove(b);
		}
		if (!b.isGameOver()) {
			blackMove(b);
		} else {
			System.out.println(b);
			System.out.println("Game Over!");
		}
	}
	
	static void blackMove(Board b) {
		if (blackIsBot){
			botMakeMove(b);
		}else{
			takePlayerMove(b);
		}
		if (!b.isGameOver()) {
			whiteMove(b);
		} else {
			System.out.println(b);
			System.out.println("Game Over!");
		}
	}
	
	static void takePlayerMove(Board b) {
		
		
		//Clear GUI input
		squareFrom = null;
		squareTo = null;
		
		//Make the GUI board active
		GridLayoutManager.setActive(true);
		
		Move move = null;
		List<Move> validMoves = b.allMoves();

		OpeningBook.getOpeningMove(b, currentECO);
		
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
		
	}
	

	static void botMakeMove(Board b) {
		
		//Make the GUI board inactive
		GridLayoutManager.setActive(false);
		
		System.out.println(b);
		System.out.println("Processing move...");
		
		AI ai = new AI(b);
		ai.AIC.currentECO = currentECO;
		ai.search();
		currentECO = ai.AIC.currentECO;
		System.out.println("Using Opening: " + OpeningBook.getECOName(currentECO) + "(" + currentECO + ")");

		System.out.println(ai.AIC);
		
		MoveAndScore move = ai.AIC.bestRootMove;
		
		System.out.println("\nBot: " +move);

		move.move.execute();
		System.out.println("Pawn Structure Score: " + b.evaluatePawnStructure());
		
		gui.updateBoard(b);
		
	}
	
	
}
