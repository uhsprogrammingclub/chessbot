//Initiation file for UHS Programming Chess Robot
//Try to limit number of methods and lines in this class; namely holds the main() method. 

package chessbot;

import java.util.*;

public class Init {

	public static void main(String[] args) {
		
		List<Piece> list = new ArrayList<Piece>();
		Piece q = new Queen(3, 0, true);
		Piece w = new Queen(3, 7, false);
		Piece p = new Pawn(3, 1, true);
		Piece l = new Pawn(4, 2, false);
		list.add(q);
		list.add(w);
		list.add(p);
		list.add(l);
		
		Board test = new Board(list);
		System.out.println(test);
		
		
		for (Move n : p.findMoves(test)){
			System.out.println(n);
		}
		
		
		//Fundamental ideas - getting list of all possible moves for the computer
		
		
	}

}
