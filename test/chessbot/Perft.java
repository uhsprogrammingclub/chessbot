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
			//String FEN = "2kr4/7r/8/8/8/8/8/4K2R w K - 0 1";

			int[] depths = new int[strSplit.length-1];
			
			for (int i = 0; i<depths.length; i++){
				depths[i] = Integer.parseInt(strSplit[i+1].substring(3));
			}
			
			Board b = Utils.boardFromFEN(FEN);
			
			for (int i = 1; i<=depths.length; i++){
				//i = 1;
				System.out.println(b);
				System.out.println("Starting Test To Depth: "+i);
				leafNodes = 0;
				
				List<Move> moves = b.allMoves();
				Collections.sort(moves);
				
				int moveNum = 0;
				for (Move m : moves){
					moveNum++;
					
					int oldNodes = leafNodes;
					m.execute();
					PerftTest(b, i-1);
					m.reverse();
					
					System.out.println("Move: "+ moveNum + " " + m + " " + (leafNodes-oldNodes));
				}
				
				System.out.println("leaf nodes: "+leafNodes + ", expected: " + depths[i-1]);
	
				assertEquals("Depth "+i+": "+FEN, depths[i-1], leafNodes);
			}
			
		}
		
		//String FEN = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1";
		//Board b = Utils.boardFromFEN(FEN);
		//leafNodes = 0;
		//PerftTest(b, 4);
		//assertEquals("Depth "+4+": "+FEN, leafNodes, 4085603);
		//System.out.println("leaf nodes: "+leafNodes + ", expected: " + 4085603 + "at depth "+4+": "+FEN);
		
		
	}
	
	
	void PerftTest(Board b, int depth){
		if (depth == 0){
			leafNodes++;
			return;
		}
		
		List<Move> moves = b.allMoves();
		Collections.sort(moves);
		
		for (Move m : moves){
			m.execute();
			PerftTest(b, depth-1);
			m.reverse();
		}
	}

}
