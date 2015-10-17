//Initiation file for UHS Programming Chess Robot
//Try to limit number of methods and lines in this class; namely holds the main() method. 

package chessbot;

import java.util.ArrayList;
import java.util.Arrays;

public class Init {

	public static void main(String[] args) {
		
		ArrayList<Pieces> list = new ArrayList<Pieces>();
		Pieces q = new Queen(7, 3, true);
		Pieces w = new Queen(0, 3, false);
		Pieces p = new Pawn(6, 3, true);
		Pieces l = new Pawn(5, 4, false);
		list.add(q);
		list.add(w);
		list.add(p);
		list.add(l);
		
		Board test = new Board(list);
		System.out.println(test);
		
		for (Point n : p.findMoves(test)){
			System.out.println(n);
		}
	}

}
