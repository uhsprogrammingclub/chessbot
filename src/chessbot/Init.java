//Initiation file for UHS Programming Chess Robot
//Try to limit number of methods and lines in this class; namely holds the main() method. 

package chessbot;

import java.util.ArrayList;
import java.util.Arrays;

public class Init {

	public static void main(String[] args) {
		
		ArrayList<Piece> list = new ArrayList<Piece>();
		Piece q = new Queen(7, 3, true);
		Piece w = new Queen(0, 3, false);
		Piece p = new Pawn(6, 3, true);
		Piece l = new Pawn(5, 4, false);
		list.add(q);
		list.add(w);
		list.add(p);
		list.add(l);
		
		Board test = new Board(list);
		System.out.println(test);
		
		for (int[] n : p.findMoves(test)){
			System.out.println(Arrays.toString(n));
		}
		
		
		//Fundamental ideas - getting list of all possible moves for the computer
		
		
	}

}
