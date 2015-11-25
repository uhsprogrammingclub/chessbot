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

	int leafNodes = 0;
	static final int TEST_LIMIT = 10000;

	@Before
	public void init() {
		Game.loadBitboards();
	}

	@Test
	public void test() throws IOException {
		String firstResult = "";
		for (int j = 0; j < 2; j++){
			long startTime = System.currentTimeMillis();
			String filePath = new File("").getAbsolutePath() + "/PerftTests";
	
			FileReader fr = new FileReader(filePath);
			BufferedReader textReader = new BufferedReader(fr);
	
			int lines = 126;
			String[] tests = new String[lines];
	
			for (int i = 0; i < lines; i++) {
				tests[i] = textReader.readLine();
			}
			textReader.close();
	
			int testNum = 0;
			if (j == 1){
				AIController.useBitBoards = false;
			}
			for (String test : tests) {
				testNum++;
				if (testNum > TEST_LIMIT) break;
				String[] strSplit = test.split(" ;");
	
				// Map array to correct variables
				String FEN = strSplit[0];
				//FEN = "r1bqkbnr/pppppppp/8/8/1n6/3P4/PPPKPPPP/RNBQ1BNR w kq - 0 2";
	
				int[] depths = new int[strSplit.length - 1];
	
				for (int i = 0; i < depths.length; i++) {
					depths[i] = Integer.parseInt(strSplit[i + 1].substring(3));
				}
	
				Board b = Utils.boardFromFEN(FEN);
				
				System.out.println("\n### Running Test #" + testNum + " ###\n");
	
				for (int i = 1; i <= depths.length; i++) {
					//i = 1;
					System.out.println(b);
					System.out.println("Starting Test To Depth: " + i);
					leafNodes = 0;
					
					List<Move> moves = b.allMoves();
	
					Collections.sort(moves);
	
					int moveNum = 0;
					for (Move m : moves) {
						moveNum++;
	
						int oldNodes = leafNodes;
						m.execute();
						PerftTest(b, i - 1);
						m.reverse();
	
						System.out.println("Move: " + moveNum + " " + m + " " + (leafNodes - oldNodes));
					}
	
					System.out.println("leaf nodes: " + leafNodes + ", expected: " + depths[i - 1]);
	
					assertEquals("Depth " + i + ": " + FEN, depths[i - 1], leafNodes);
				}
			}
			System.out.println(firstResult);
			firstResult = "Perft test finished successfully in "+(System.currentTimeMillis()-startTime)/60000.0 + " mins";
			System.out.println(firstResult);
		}
	}

	void PerftTest(Board b, int depth) {
		if (depth == 0) {
			leafNodes++;
			return;
		}

		List<Move> moves = b.allMoves();

		Collections.sort(moves);

		for (Move m : moves) {
			//System.out.println("Move: " + m);
			m.execute();
			PerftTest(b, depth - 1);
			m.reverse();
		}
	}

}
