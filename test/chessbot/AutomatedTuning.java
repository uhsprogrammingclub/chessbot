package chessbot;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import chessbot.Board.Value;

public class AutomatedTuning {
	
	boolean winAtChess = false;
	boolean BratkoKopec = true;
	boolean silentButDeadly = false;
	boolean all = false;
	int[] computationTimes = {100, 1000};
	
	Map<String, Integer> testSuites = new HashMap<String, Integer>();
	
	Map<Board.Value, Integer> initialValues = new HashMap<Board.Value, Integer>();
	
	@Before
	public void init() {
		Zobrist.zobristFillArray();
		initialValues.put(Board.Value.DOUBLED_PAWN, 25);
		initialValues.put(Board.Value.ISOLATED_PAWN, 25);
		initialValues.put(Board.Value.HALF_OPEN_FILE, 25);
		initialValues.put(Board.Value.PAWN_CHAIN, 25);
		initialValues.put(Board.Value.HOLE, 25);
		
		testSuites.put("Bratko-Kopec_Test", 24);
		testSuites.put("Win_At_Chess_Suite", 300);
		testSuites.put("SilentButDeadly", 134);
	}

	@Test
	public void individualSuites() throws IOException{
		List<String> finalValues = new ArrayList<String>();

		for (int time: computationTimes){
			for (Map.Entry<String, Integer> suite : testSuites.entrySet()){
				String suiteName = suite.getKey();
				String[] tests = getTests(suiteName, suite.getValue());
				Map<Board.Value, Integer> tunedMap = new HashMap<Board.Value, Integer>();
				tunedMap.putAll(initialValues);
		
				System.out.println("\n###Running "+suiteName+"###\n");
				
				List<String> bestResults = new ArrayList<String>();
				for (int interval = 25; interval > 0; interval /=2){
					for (Map.Entry<Value, Integer> entry : tunedMap.entrySet())
					{
						int tunedValue = tune(tunedMap, entry.getKey(), interval, tests, time, bestResults);
						tunedMap.put(entry.getKey(), tunedValue);
					}
				}
				String result = "";
				result += "\n"+suiteName + " at " + time/1000.0 + "s:";
				System.out.println("\n"+suiteName + " at " + time/1000.0 + "s:");
				for (String s : bestResults){
					System.out.println(s);
				}
				result += tunedMap.toString();
				System.out.println(tunedMap);
				finalValues.add(result);
			}
		}
		for (String s : finalValues){
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
	
	public int tune(Map<Board.Value, Integer> valuesMap, Board.Value param, int interval, String[] tests, int time, List<String> bestResults){
		int bestCorrect = 0;
		int value = valuesMap.get(param);
		for (int mod = 0; mod < 3; mod++){
			Map<Board.Value, Integer> experimentalMap = new HashMap<Board.Value, Integer>();
			experimentalMap.putAll(valuesMap);
			int experimentalValue = value;
			if (mod == 1){
				experimentalValue = value+interval;
			}else if (mod == 2){
				experimentalValue = value-interval;
			}
			experimentalMap.put(param, value);
			String[] resultString = {""};
			int result = runTestSuite(tests, experimentalMap, time, resultString);
			
			if (result > bestCorrect){
				bestCorrect = result;
				if (mod != 0){
					bestResults.add(resultString[0]);
					System.out.println("New best "+ param + "(" + experimentalValue + "): " + resultString[0]);
					mod--; //research with the new value.
					value = experimentalValue;
				}
			}
		}
		return value;
	}
	
	public int runTestSuite(String[] tests, Map<Board.Value, Integer> valuesMap, int time, String[] resultString){
		int numOfTests = tests.length;
		TranspositionTable.trans.clear();
		StructureTable.pawns.clear();
			
		int correct = 0;

		int testNum = 0;
		for (String edp : tests) {
			testNum++;
			Map<String, String[]> info = Utils.edpGetInfo(edp);
			
			Board b = Utils.boardFromFEN(Utils.edpGetFEN(edp));
			b.setValues(valuesMap);
			
			AIController.setComputationTime(time);
			AI ai = new AI(b);
			ai.search();
			
			String botMove = "";
			if (ai.AIC.bestRootMove != null){
				botMove = ai.AIC.bestRootMove.move.getSAN();
			}
			for (String s : info.get("bm")) {
				if (s.equals(botMove)){
					correct++;
					resultString[0] = resultString[0] + testNum + " ";
				}	
			}
		}
		resultString[0] = "Number Correct: " + correct + "/" + numOfTests + "; Values:" + valuesMap + "; Correct tests: " + resultString[0];
		return correct;
	}

}
