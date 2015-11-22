package chessbot;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import chessbot.Board.Value;

public class EngineTesting {
	
	boolean winAtChess = false;
	boolean BratkoKopec = true;
	boolean silentButDeadly = false;

	
	int[] computationTimes = {1000};
	
	@Before
	public void init() {
		Zobrist.zobristFillArray();
	}

	@Test
	public void BratkoKopecSuite() throws IOException{
		if (!BratkoKopec) return;

		String suiteName = "Bratko-Kopec_Test";
		int lines = 24;
		List<String> finalResults = new ArrayList<String>();

		for (int time: computationTimes){
				String[] tests = getTests(suiteName, lines);
				System.out.println("\n### Running "+suiteName+ " at " + time/1000.0 + "s ###\n");
				
				String result = runTestSuite(tests, time);
				System.out.println(result);
				finalResults.add(result);
		}
		for (String s : finalResults){
			System.out.println(s);
		}
	}
	
	@Test
	public void WinAtChessSuite() throws IOException{
		if (!winAtChess) return;

		String suiteName = "Win_At_Chess_Suite";
		int lines = 300;
		List<String> finalResults = new ArrayList<String>();

		for (int time: computationTimes){
				String[] tests = getTests(suiteName, lines);
				System.out.println("\n### Running "+suiteName+ " at " + time/1000.0 + "s ###\n");
				
				String result = runTestSuite(tests, time);
				System.out.println(result);
				finalResults.add(result);
		}
		for (String s : finalResults){
			System.out.println(s);
		}
	}
	
	@Test
	public void SilentButDeadlySuite() throws IOException{
		if (!silentButDeadly) return;

		String suiteName = "SilentButDeadly";
		int lines = 134;
		List<String> finalResults = new ArrayList<String>();

		for (int time: computationTimes){
				String[] tests = getTests(suiteName, lines);
				System.out.println("\n### Running "+suiteName+ " at " + time/1000.0 + "s ###\n");
				
				String result = runTestSuite(tests, time);
				System.out.println(result);
				finalResults.add(result);
		}
		for (String s : finalResults){
			System.out.println(s);
		}
	}
	
	public String[] getTests(String suite, int lines) throws IOException{
		String filePath = new File("").getAbsolutePath() + "/" + suite;

		FileReader fr = new FileReader(filePath);
		BufferedReader textReader = new BufferedReader(fr);

		String[] tests = new String[lines];

		for (int i = 0; i < lines; i++) {
			tests[i] = textReader.readLine();
		}
		textReader.close();
		return tests;
	}
	
	
	public String runTestSuite(String[] tests, int time){
		int numOfTests = tests.length;
		TranspositionTable.trans.clear();
		StructureTable.pawns.clear();
			
		String resultString = "";
		int correct = 0;

		int testNum = 0;
		for (String edp : tests) {
			testNum++;
			Map<String, String[]> info = Utils.edpGetInfo(edp);
			
			Board b = Utils.boardFromFEN(Utils.edpGetFEN(edp));
			
			AIController.setComputationTime(time);
			AI ai = new AI(b);
			ai.search();
			
			String botMove = "";
			if (ai.AIC.bestRootMove != null){
				botMove = ai.AIC.bestRootMove.move.getSAN();
			}
			System.out.println("Test ID: " + Arrays.toString(info.get("id")) + " Actual Best Moves: "
					+ Arrays.toString(info.get("bm")) + " Computer Move: [" + botMove + "]" );

			for (String s : info.get("bm")) {
				if (s.equals(botMove)){
					correct++;
					resultString = resultString + testNum + " ";
				}	
			}
		}
		resultString = "Number Correct: " + correct + "/" + numOfTests + " at " + time/1000.0 + "s:" + "; Correct Tests: " + resultString;
		return resultString;
	}

}
