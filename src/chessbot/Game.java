//Initiation file for UHS Programming Chess Robot
//Try to limit number of methods and lines in this class; namely holds the main() method. 

package chessbot;

import java.util.*;

public class Game {

	static Scanner s = new Scanner(System.in);
	static boolean botVBot = true;

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
		Piece p5 = new Pawn(4, 1, true);
		Piece p6 = new Pawn(5, 1, true);
		Piece p7 = new Pawn(6, 1, true);
		Piece p8 = new Pawn(7, 1, true);

		Piece R1 = new Rook(0, 7, false);
		Piece N1 = new Knight(1, 7, false);
		Piece B1 = new Bishop(2, 7, false);
		Piece Q = new Queen(3, 7, false);
		Piece K = new King(4, 7, false);
		Piece B2 = new Bishop(5, 7, false);
		Piece N2 = new Knight(6, 7, false);
		Piece R2 = new Rook(7, 7, false);

		Piece P1 = new Pawn(0, 6, false);
		Piece P2 = new Pawn(1, 6, false);
		Piece P3 = new Pawn(2, 6, false);
		Piece P4 = new Pawn(3, 6, false);
		Piece P5 = new Pawn(4, 6, false);
		Piece P6 = new Pawn(5, 6, false);
		Piece P7 = new Pawn(6, 6, false);
		Piece P8 = new Pawn(7, 6, false);

		list.add(r1);
		list.add(n1);
		list.add(b1);
		list.add(q);
		list.add(k);
		list.add(b2);
		list.add(n2);
		list.add(r2);

		list.add(p1);
		list.add(p2);
		list.add(p3);
		list.add(p4);
		list.add(p5);
		list.add(p6);
		list.add(p7);
		list.add(p8);

		list.add(R1);
		list.add(N1);
		list.add(B1);
		list.add(Q);
		list.add(K);
		list.add(B2);
		list.add(N2);
		list.add(R2);

		list.add(P1);
		list.add(P2);
		list.add(P3);
		list.add(P4);
		list.add(P5);
		list.add(P6);
		list.add(P7);
		list.add(P8);

		Board b = new Board(list);
		
		Zobrist.zobristFillArray();
		
		//botMakeMove(b, true);
		takePlayerMove(b);
	}

	static void takePlayerMove(Board b) {
		
		// Get all values
	    /*for (Enumeration e = TranspositionTable.trans.keys(); e.hasMoreElements();)
	    {
		System.out.println (TranspositionTable.trans.get(e.nextElement()));
	    }*/
		
		Move move;
		List<Move> validMoves = b.allMoves(true);
		while (true) {
			System.out.println(b);
			
			System.out.print("[A-H][1-8], [A-H][1-8]: ");
			String[] sp = s.nextLine().split(" ");
			
			Point from = new Point("I5");
			Point to = new Point("I5");
			
			if(sp.length != 1){
				 from = new Point(sp[0].replaceAll("\\s+",""));
				 to = new Point(sp[1].replaceAll("\\s+",""));
			}else{
				System.out.println("You need to seperate your moves with a single space.");
			}
			

			move = new Move(b, from, to);
			boolean validMove = false;
			for (Move m : validMoves) {

				if (move.equals(m)) {
					validMove = true;
					break;
				}
			}
			if (validMove) {
				break;
			} else {
				System.out.println(move + " is an invalid move!");
			}

		}
		System.out.println(move);
		move.execute();
		if (!b.isGameOver(false)) {
			botMakeMove(b, false);
		} else {
			System.out.println(b);
			System.out.println("Game Over!");
		}
	}

	static void botMakeMove(Board b, boolean player) {
		System.out.println(b);
		System.out.println("Processing move...");
		AlphaBetaMinimax ai = new AlphaBetaMinimax(b, player);
		Move move = ai.bestMove();
		
		for(MoveAndScore m : ai.rootsChildrenScore){
			System.out.println(m);
		}
		System.out.println("Static computations: " + ai.staticComputations);
		System.out.println("Max Depth: " + ai.finalDepth);
		System.out.println(move);
		
		move.execute();
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
