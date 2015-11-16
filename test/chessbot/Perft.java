package chessbot;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.File;

public class Perft {

	Game g;
	int leafNodes = 0;
	
	@Before
	public void init(){
		g = new Game();
	}
	
	@Test
	public void test() throws IOException {
		
		String filePath = new File("").getAbsolutePath() + "/PerftTests";
		
		FileReader fr = new FileReader(filePath);
		BufferedReader textReader = new BufferedReader(fr);
		
		int lines = 126;
		String[] tests = new String[lines];
		
		for(int i = 0; i< lines; i++){
			tests[i] = textReader.readLine();
		}
		textReader.close();
		
		for (String test : tests){
			String[] strSplit = test.split(" ;");
			
			//Map array to correct variables
			String FEN = strSplit[0];

			
			int[] depths = new int[strSplit.length-1];
			
			for (int i = 0; i<depths.length; i++){
				depths[i] = Integer.parseInt(strSplit[i+1].substring(3));
			}
			
			Board b = Utils.boardFromFEN(FEN);
			
			for (int i = 1; i<=depths.length; i++){
				leafNodes = 0;
				PerftTest(b, i);
				assertEquals("Depth "+i+": "+FEN, leafNodes, depths[i-1]);
				System.out.println("leaf nodes: "+leafNodes + ", expected: " + depths[i-1] + "at depth "+i+": "+FEN);
			}
			
		}
		
	}
	
	
	void PerftTest(Board b, int depth){
		if (depth == 0){
			leafNodes++;
			return;
		}
		
		List<Move> moves = b.allMoves(b.playerMove);
		Collections.sort(moves);
		
		for (Move m : moves){
			m.execute();
			PerftTest(b, depth-1);
			m.reverse();
		}
	}

}
