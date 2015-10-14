//Initiation file for UHS Programming Chess Robot
//Try to limit number of methods and lines in this class; namely holds the main() method. 

package chessbot;

import java.util.ArrayList;

public class Init {

	public static void main(String[] args) {
		
		ArrayList<Pieces> list = new ArrayList<Pieces>();
		Queen q = new Queen(7, 3);
		Queen w = new Queen(0, 3);
		list.add(q);
		list.add(w);
		
		Board test = new Board(list);
		System.out.println(test);
		
		// TODO Auto-generated method stub
		System.out.print("muahahhahahahahaha");

	}

}
