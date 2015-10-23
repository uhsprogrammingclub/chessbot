//Initiation file for UHS Programming Chess Robot
//Try to limit number of methods and lines in this class; namely holds the main() method. 

package chessbot;

import java.util.*;
import java.util.Map.Entry;

public class Game {

	//Scanner to take Human input
	static Scanner s = new Scanner(System.in);
	
	//Boolean that determines whether the AI plays itself
	static boolean botVBot = false;
	
	//Initialize the grid GUI layout
	static GridLayoutManager gui = new GridLayoutManager();
	
	//Points to be created by the GUI
	static volatile Point squareFrom = null;
	static volatile Point squareTo = null;
	
	public static void main(String[] args) {

		List<Piece> list = new ArrayList<Piece>();

		Piece r1 = new Rook(0, 0, true);
		Piece n1 = new Knight(1, 0, true);
		Piece b1 = new Bishop(2, 0, true);
		Piece q = new Queen(3, 0, true);
		Piece k = new King(4, 0, true);
		Piece b2 = new Bishop(5, 0, true);
		Piece n2 = new Knight(6, 0, true);
		Piece r2 = new Rook(7, 0, true);

		Piece p1 = new Pawn(0, 1, true);
		Piece p2 = new Pawn(1, 1, true);
		Piece p3 = new Pawn(2, 1, true);
		Piece p4 = new Pawn(3, 1, true);
		Piece p5 = new Pawn(4, 3, true);
		Piece p6 = new Pawn(5, 1, true);
		Piece p7 = new Pawn(6, 1, true);
		Piece p8 = new Pawn(7, 1, true);

		Piece R1 = new Rook(0, 7, false);
		Piece N1 = new Knight(1, 7, false);
		Piece B1 = new Bishop(2, 7, false);
		Piece Q = new Queen(3, 7, false);
		Piece K = new King(0, 7, false);
		Piece B2 = new Bishop(5, 7, false);
		Piece N2 = new Knight(6, 7, false);
		Piece R2 = new Rook(7, 7, false);

		Piece P1 = new Pawn(0, 6, false);
		Piece P2 = new Pawn(1, 6, false);
		Piece P3 = new Pawn(2, 6, false);
		Piece P4 = new Pawn(3, 6, false);
		Piece P5 = new Pawn(4, 5, false);
		Piece P6 = new Pawn(5, 2, false);
		Piece P7 = new Pawn(6, 6, false);
		Piece P8 = new Pawn(7, 6, false);

		/*list.add(r1);
		list.add(n1);
		list.add(b1);
		list.add(q);*/
		list.add(k);/*
		list.add(b2);
		list.add(n2);
		list.add(r2);

		list.add(p1);
		list.add(p2);
		list.add(p3);
		list.add(p4);
		list.add(p5);
		list.add(p6);
		list.add(p7);*/
		list.add(p8);

		/*list.add(R1);
		list.add(N1);
		list.add(B1);
		list.add(Q);*/
		list.add(K);/*
		list.add(B2);
		list.add(N2);
		list.add(R2);

		list.add(P1);
		list.add(P2);
		list.add(P3);
		list.add(P4);
		list.add(P5);*/
		list.add(P6);
		/*list.add(P7);
		list.add(P8);*/

		Board b = new Board(list);
		
		Zobrist.zobristFillArray();

		gui.updateBoard(b);
		
		
		//botMakeMove(b, false);
		takePlayerMove(b);
	}

	static void takePlayerMove(Board b) {
		
		/*for (Entry<Integer, HashEntry> entry : TranspositionTable.trans.entrySet() ) {
		    System.out.println(entry.getKey() + " " + entry.getValue());
		}*/
		
		//Clear GUI input
		squareFrom = null;
		squareTo = null;
		
		//Make the GUI board active
		GridLayoutManager.setActive(true);
		
		Move move = null;
		List<Move> validMoves = b.allMoves(true);
		
		while (true) {
			
			Point from = Game.squareFrom;
			Point to = Game.squareTo;
			
			/*
			System.out.println(b);
			System.out.print("[A-H][1-8] [A-H][1-8]: ");
			String[] sp = s.nextLine().split(" ");
			
			Point from = new Point("I5");
			Point to = new Point("I5");
			
			if(sp.length != 1){
				 from = new Point(sp[0].replaceAll("\\s+",""));
				 to = new Point(sp[1].replaceAll("\\s+",""));
			}else{
				System.out.println("You need to seperate your moves with a single space.");
			}*/
			
			if(from != null && to != null){
				
				//If it is a promotion move
				if(b.locations[from.x][from.y].symbol.equals("p") && (to.y == 7 || to.y == 0)){
					System.out.println("What piece would you like to promote to? Queen = q; Knight = n, etc.");
					String pieceStr = s.nextLine();
					if(pieceStr.equals("q")){
						move = new Move(b, from, to, "q");
					}
					else if(pieceStr.equals("n")){
						move = new Move(b, from, to, "n");
					}
					else if(pieceStr.equals("r")){
						move = new Move(b, from, to, "r");
					}
					else if(pieceStr.equals("b")){
						move = new Move(b, from, to, "b");
					}
							
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
		
		System.out.println(move);
		move.execute();
		
		gui.updateBoard(b);
		
		if (!b.isGameOver(false)) {
			botMakeMove(b, false);
		} else {
			System.out.println(b);
			System.out.println("Game Over!");
		}
		
	}

	static void botMakeMove(Board b, boolean player) {
		
		//Make the GUI board inactive
		GridLayoutManager.setActive(false);
		
		System.out.println(b);
		System.out.println("Processing move...");
		
		long startTime = System.currentTimeMillis();
		AlphaBetaMinimax ai = new AlphaBetaMinimax(b, player);
		long endTime = System.currentTimeMillis();
		
		for(MoveAndScore m : ai.rootsChildrenScore){
			System.out.println(m);
		}
		System.out.println("Time expended: " + (endTime-startTime)/1000.0);
		System.out.println("Static computations: " + ai.staticComputations);
		System.out.println("Transposition Table Size: " + TranspositionTable.trans.size());
		System.out.println("Max Depth: " + ai.finalDepth);
		
		Move move = ai.bestMove();
		System.out.println(move);
		
		/*for (Entry<Integer, HashEntry> entry : TranspositionTable.trans.entrySet()) {
		    System.out.println(entry.getKey() + " " + entry.getValue());
		}*/
		System.out.println("PV: " + new PV(b, player));
		move.execute();
		gui.updateBoard(b);
		
		if (!b.isGameOver(!player)) {
			if (botVBot){
				botMakeMove(b, !player);
			}else{
				takePlayerMove(b);
			}
		} else {
			System.out.println(b);
			System.out.println("Game Over!");
		}
	}
	
	
}
