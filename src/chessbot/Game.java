package chessbot;

import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {


	//Default the FEN set up to the standard position
	static String defaultSetup = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	static String setup = defaultSetup; 
	
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
		//g.setFEN("3rr1k1/1pp2pp1/p6p/2bP1R2/1nP1p3/2R1P3/1P1NK3/8 b - - 0 0"); - Reed Game
		//g.setFEN("1k1r4/pp1b1R2/3q2pp/4p3/2B5/4Q3/PPP2B2/2K5 b - - 0 0");
		g.setFEN("r1bq1b1r/ppp1kpp1/5n1p/nB1Pp1N1/8/6P1/PPPP1P1P/RNBQ1RK1 w - - 0 9");
		g.setFEN(setup);
		g.init();
		g.start();
		
	}
	
	public void init(){
		
		setBoard(setup);
		
		initGUI();
		System.out.println(b.evaluatePawnStructure());
		System.out.println(b);
		
		
		playerMovesFirst = b.playerMove;
	}
	
	public void start(){
		OpeningBook book = new OpeningBook(Utils.boardFromFEN(defaultSetup));
		Thread bookThread = new Thread(book, "Opening Book Thread");  
		bookThread.start();  
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
		List<Move> validMoves = b.allMoves();

		OpeningBook.getOpeningMove(b);
		
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
		System.out.println("Pawn Structure Score: " + b.evaluatePawnStructure());
		
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
		
		AI ai = new AI(b);
		ai.search();
		
		System.out.println(ai.AIC);
		
		MoveAndScore move = ai.AIC.bestRootMove;
		
		System.out.println("\nBot: " +move);

		move.move.execute();
		System.out.println("Pawn Structure Score: " + b.evaluatePawnStructure());
		
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
