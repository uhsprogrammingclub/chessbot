//Initiation file for UHS Programming Chess Robot
//Try to limit number of methods and lines in this class; namely holds the main() method. 

package chessbot;

import java.util.*;

public class Init {

	public static void main(String[] args) {
		
		
		Piece r1 = new Rook(0, 0, false);
		Piece n1 = new Knight(1, 0, false);
		Piece b1 = new Bishop(2, 0, false);
		Piece q = new Queen(3, 0, false);
		Piece k = new King(4, 0, false);
		Piece b2 = new Bishop(5, 0, false);
		Piece n2 = new Knight(6, 0, false);
		Piece r2 = new Rook(7, 0, false);
		
		Piece p1 = new Pawn(0, 1, false);
		Piece p2 = new Pawn(1, 1, false);
		Piece p3 = new Pawn(2, 1, false);
		Piece p4 = new Pawn(3, 1, false);
		Piece p5 = new Pawn(4, 1, false);
		Piece p6 = new Pawn(5, 1, false);
		Piece p7 = new Pawn(6, 1, false);
		Piece p8 = new Pawn(7, 1, false);
		
		
		
		List<Piece> list = new ArrayList<Piece>();
		Piece R1 = new Rook(0, 7, true);
		Piece N1 = new Knight(1, 7, true);
		Piece B1 = new Bishop(2, 7, true);
		Piece Q = new Queen(3, 7, true);
		Piece K = new King(4, 7, true);
		Piece B2 = new Bishop(5, 7, true);
		Piece N2 = new Knight(6, 7, true);
		Piece R2 = new Rook(7, 7, true);
		
		Piece P1 = new Pawn(0, 6, true);
		Piece P2 = new Pawn(1, 6, true);
		Piece P3 = new Pawn(2, 6, true);
		Piece P4 = new Pawn(3, 6, true);
		Piece P5 = new Pawn(4, 6, true);
		Piece P6 = new Pawn(5, 6, true);
		Piece P7 = new Pawn(6, 6, true);
		Piece P8 = new Pawn(7, 6, true);
		
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
		
		
		
		Board test = new Board(list);
		System.out.println(test);
		
		
		for (Point n : p5.findMoves(test)){
			System.out.println(n);
		}
		
		
		//Fundamental ideas - getting list of all possible moves for the computer
		
		
	}

}
