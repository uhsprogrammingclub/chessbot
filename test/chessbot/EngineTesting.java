package chessbot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

public class EngineTesting {


	@Before
	public void init() {
		Zobrist.zobristFillArray();
	}
	
	boolean winAtChess = false;
	boolean BratkoKopec = true;
	
	@Test
	public void WinAtChessSuite() throws IOException {
		
		if(!winAtChess){
			return;
		}
		
		//Fill this array with the different times you want tested...
		int[] computationTimes = {500};

		String filePath = new File("").getAbsolutePath() + "/Win_At_Chess_Suite";

		FileReader fr = new FileReader(filePath);
		BufferedReader textReader = new BufferedReader(fr);

		int lines = 300;
		String[] tests = new String[lines];

		for (int i = 0; i < lines; i++) {
			tests[i] = textReader.readLine();
		}
		textReader.close();
		

		for (int time : computationTimes) {

			List<String> results = new ArrayList<String>();
			List<String> fails = new ArrayList<String>();
			List<String> best = new ArrayList<String>();
			int bestCorrect = 0;
			
			int[] correctResults = new int[lines];
			
			int bestIsolatedPawnValue = 25;
			int bestDoubledPawnValue = 25;
			int bestHalfOpenFileValue = 25;
			int bestPawnChainValue = 25;
			int bestHoleValue = 25;
			
			
			for (int interval = 25; interval > 0; interval /=2){
				
				int newbestIsolatedPawnValue = bestIsolatedPawnValue;
				int newbestDoubledPawnValue = bestDoubledPawnValue;
				int newbestHalfOpenFileValue = bestHalfOpenFileValue;
				int newbestPawnChainValue = bestPawnChainValue;
				int newbestHoleValue = bestHoleValue;
				
				for (int pawnTest = 1; pawnTest <=5; pawnTest++){
					
					int newbestCorrect = 0;
					for (int valueChange = 0; valueChange <= 2; valueChange++){
						int isolatedPawnValue = bestIsolatedPawnValue;
						int doubledPawnValue = bestDoubledPawnValue;
						int halfOpenFileValue = bestHalfOpenFileValue;
						int pawnChainValue = bestPawnChainValue;
						int holeValue = bestHoleValue;
						
						int modificationFactor = 0;
						if (valueChange == 1){
							modificationFactor = -1;
						}else if (valueChange == 2){
							modificationFactor = 1;
						}
						
						if (pawnTest == 1){
							isolatedPawnValue += modificationFactor*interval;
						}else if (pawnTest == 2){
							doubledPawnValue += modificationFactor*interval;
						}else if (pawnTest == 3){
							halfOpenFileValue += modificationFactor*interval;
						}else if (pawnTest == 4){
							pawnChainValue += modificationFactor*interval;
						}else if (pawnTest == 5){
							holeValue += modificationFactor*interval;
						}
						
						TranspositionTable.trans.clear();
						StructureTable.pawns.clear();
	
						int correct = 0;
	
						int testNum = 0;
						for (String edp : tests) {
							testNum++;
							Map<String, String[]> info = Utils.edpGetInfo(edp);
							Board b = Utils.boardFromFEN(Utils.edpGetFEN(edp));
							AIController.setComputationTime(time);
							
							b.isolatedPawnValue = isolatedPawnValue;
							b.doubledPawnValue = doubledPawnValue;
							b.halfOpenFileValue = halfOpenFileValue;
							b.pawnChainValue = pawnChainValue;
							b.holeValue = holeValue;
	
							AI ai = new AI(b);
							ai.search();
							String botMove = "";
							if (ai.AIC.bestRootMove != null){
								botMove = ai.AIC.bestRootMove.move.getSAN();
							}
							System.out.println(b);
							
							System.out.println("Test ID: " + Arrays.toString(info.get("id")) + " Actual Best Moves: "
									+ Arrays.toString(info.get("bm")) + " Computer Move: [" + botMove + "] Pawn values:" + isolatedPawnValue + ", "+ doubledPawnValue + ", "+ halfOpenFileValue + ", "+ pawnChainValue + ", "+ holeValue );
	
							for (String s : info.get("bm")) {
								if (s.equals(botMove)){
									correctResults[testNum-1]++;
									correct++;
								}else{
									fails.add(edp);
								}
									
							}
						}
						results.add("Time Allowed: " + time + " Number Correct: " + correct + "/" + lines + " Pawn values:" + isolatedPawnValue + ", "+ doubledPawnValue + ", "+ halfOpenFileValue + ", "+ pawnChainValue + ", "+ holeValue );
						if (correct > bestCorrect){
							best.add("Time Allowed: " + time + " Number Correct: " + correct + "/" + lines + " Pawn values:" + isolatedPawnValue + ", "+ doubledPawnValue + ", "+ halfOpenFileValue + ", "+ pawnChainValue + ", "+ holeValue );
							bestCorrect = correct;
						}
						if (correct > newbestCorrect){
							if (pawnTest == 1){
								newbestIsolatedPawnValue = isolatedPawnValue;
							}else if (pawnTest == 2){
								newbestDoubledPawnValue = doubledPawnValue;
							}else if (pawnTest == 3){
								newbestHalfOpenFileValue = halfOpenFileValue;
							}else if (pawnTest == 4){
								newbestPawnChainValue = pawnChainValue;
							}else if (pawnTest == 5){
								newbestHoleValue = holeValue;
							}
							
							newbestCorrect = correct;
						}
						for(String s : best){
							System.out.println(s);
						}
					
					}
				}
				bestIsolatedPawnValue = newbestIsolatedPawnValue;
				bestDoubledPawnValue = newbestDoubledPawnValue;
				bestHalfOpenFileValue = newbestHalfOpenFileValue;
				bestPawnChainValue = newbestPawnChainValue;
				bestHoleValue = newbestHoleValue;
			}
		
			
			
			for(String s : fails){
				System.out.println("All fails:");
				System.out.println(s);
			}
			for(String s : results){
				System.out.println("All results:");
				System.out.println(s);
			}
			for(String s : best){
				System.out.println("Best results:");
				System.out.println(s);
			}
			for(int i = 0; i<lines; i++){
				System.out.println("Total correct for each test:");
				System.out.println((i+1) + ": " + correctResults[i]);
			}
			
			System.out.println("bestIsolatedPawnValue: "+ bestIsolatedPawnValue);
			System.out.println("bestDoubledPawnValue: "+ bestDoubledPawnValue);
			System.out.println("bestHalfOpenFileValue: "+ bestHalfOpenFileValue);
			System.out.println("bestPawnChainValue: "+ bestPawnChainValue);
			System.out.println("bestHoleValue: "+ bestHoleValue);
		}
	}

	@Test
	public void test() throws IOException {
		
		if(!BratkoKopec){
			return;
		}

		//Fill this array with the different times you want tested...
		int[] computationTimes = {500, 1000, 2000};

		String filePath = new File("").getAbsolutePath() + "/Bratko-Kopec_Test";

		FileReader fr = new FileReader(filePath);
		BufferedReader textReader = new BufferedReader(fr);

		int lines = 24;
		String[] tests = new String[lines];

		for (int i = 0; i < lines; i++) {
			tests[i] = textReader.readLine();
		}
		textReader.close();
		

		for (int time : computationTimes) {

			List<String> results = new ArrayList<String>();
			List<String> best = new ArrayList<String>();
			int[] correctResults = new int[lines];
			
			int bestCorrect = 0;		
			int bestIsolatedPawnValue = 25;
			int bestDoubledPawnValue = 25;
			int bestHalfOpenFileValue = 25;
			int bestPawnChainValue = 25;
			int bestHoleValue = 25;
			
			
			for (int interval = 25; interval > 0; interval /=2){
				
				int newbestIsolatedPawnValue = bestIsolatedPawnValue;
				int newbestDoubledPawnValue = bestDoubledPawnValue;
				int newbestHalfOpenFileValue = bestHalfOpenFileValue;
				int newbestPawnChainValue = bestPawnChainValue;
				int newbestHoleValue = bestHoleValue;
				
				for (int pawnTest = 1; pawnTest <=5; pawnTest++){
					
					int newbestCorrect = 0;
					for (int valueChange = 0; valueChange <= 2; valueChange++){
						int isolatedPawnValue = bestIsolatedPawnValue;
						int doubledPawnValue = bestDoubledPawnValue;
						int halfOpenFileValue = bestHalfOpenFileValue;
						int pawnChainValue = bestPawnChainValue;
						int holeValue = bestHoleValue;
						
						int modificationFactor = 0;
						if (valueChange == 1){
							modificationFactor = -1;
						}else if (valueChange == 2){
							modificationFactor = 1;
						}
						if (pawnTest == 1){
							isolatedPawnValue += modificationFactor*interval;
						}else if (pawnTest == 2){
							doubledPawnValue += modificationFactor*interval;
						}else if (pawnTest == 3){
							halfOpenFileValue += modificationFactor*interval;
						}else if (pawnTest == 4){
							pawnChainValue += modificationFactor*interval;
						}else if (pawnTest == 5){
							holeValue += modificationFactor*interval;
						}
						
						System.out.println("isolatedPawnValue: "+ isolatedPawnValue 
								+  " doubledPawnValue: "+ doubledPawnValue 
								+ " halfOpenFileValue: "+ halfOpenFileValue 
								+ " pawnChainValue: "+ pawnChainValue
								+ " holeValue: "+ holeValue);
						
						TranspositionTable.trans.clear();
						StructureTable.pawns.clear();
	
	
						int correct = 0;

						int testNum = 0;
						for (String edp : tests) {
							testNum++;
							Map<String, String[]> info = Utils.edpGetInfo(edp);
							Board b = Utils.boardFromFEN(Utils.edpGetFEN(edp));
							AIController.setComputationTime(time);
							
							b.isolatedPawnValue = isolatedPawnValue;
							b.doubledPawnValue = doubledPawnValue;
							b.halfOpenFileValue = halfOpenFileValue;
							b.pawnChainValue = pawnChainValue;
							b.holeValue = holeValue;

							AI ai = new AI(b);
							ai.search();
							String botMove = "";
							if (ai.AIC.bestRootMove != null){
								botMove = ai.AIC.bestRootMove.move.getSAN();
							}
							//System.out.println(b);
							
							//System.out.println("Test ID: " + Arrays.toString(info.get("id")) + " Actual Best Moves: "
							//		+ Arrays.toString(info.get("bm")) + " Computer Move: [" + botMove + "] Pawn values:" + isolatedPawnValue + ", "+ doubledPawnValue + ", "+ halfOpenFileValue + ", "+ pawnChainValue + ", "+ holeValue );

							for (String s : info.get("bm")) {
								if (s.equals(botMove)){
									correctResults[testNum-1]++;
									correct++;
								}	
							}
						}
						results.add("Time Allowed: " + time + " Number Correct: " + correct + "/" + lines + " Pawn values:" + isolatedPawnValue + ", "+ doubledPawnValue + ", "+ halfOpenFileValue + ", "+ pawnChainValue + ", "+ holeValue );
						if (correct > bestCorrect){
							System.out.println("bestCorrect: Time Allowed: " + time + " Number Correct: " + correct + "/" + lines + " Pawn values:" + isolatedPawnValue + ", "+ doubledPawnValue + ", "+ halfOpenFileValue + ", "+ pawnChainValue + ", "+ holeValue );
							best.add("Time Allowed: " + time + " Number Correct: " + correct + "/" + lines + " Pawn values:" + isolatedPawnValue + ", "+ doubledPawnValue + ", "+ halfOpenFileValue + ", "+ pawnChainValue + ", "+ holeValue );
							bestCorrect = correct;
						}
						if (correct > newbestCorrect && valueChange != 0){
							if (pawnTest == 1){
								newbestIsolatedPawnValue = isolatedPawnValue;
							}else if (pawnTest == 2){
								newbestDoubledPawnValue = doubledPawnValue;
							}else if (pawnTest == 3){
								newbestHalfOpenFileValue = halfOpenFileValue;
							}else if (pawnTest == 4){
								newbestPawnChainValue = pawnChainValue;
							}else if (pawnTest == 5){
								newbestHoleValue = holeValue;
							}
							System.out.println("newbestCorrect: Time Allowed: " + time + " Number Correct: " + correct + "/" + lines + " Pawn values:" + isolatedPawnValue + ", "+ doubledPawnValue + ", "+ halfOpenFileValue + ", "+ pawnChainValue + ", "+ holeValue );
							newbestCorrect = correct;
						}
						
					}
				}
				bestIsolatedPawnValue = newbestIsolatedPawnValue;
				bestDoubledPawnValue = newbestDoubledPawnValue;
				bestHalfOpenFileValue = newbestHalfOpenFileValue;
				bestPawnChainValue = newbestPawnChainValue;
				bestHoleValue = newbestHoleValue;
				
				System.out.println("\n Finished interval:");
				System.out.println("bestIsolatedPawnValue: "+ bestIsolatedPawnValue 
						+  " bestDoubledPawnValue: "+ bestDoubledPawnValue 
						+ " bestHalfOpenFileValue: "+ bestHalfOpenFileValue 
						+ " bestPawnChainValue: "+ bestPawnChainValue
						+ " bestHoleValue: "+ bestHoleValue);
			}
		
			
			for(String s : results){
				System.out.println("All results:");
				System.out.println(s);
			}
			for(String s : best){
				System.out.println("Best results:");
				System.out.println(s);
			}
			for(int i = 0; i<lines; i++){
				System.out.println("Total correct for each test:");
				System.out.println((i+1) + ": " + correctResults[i]);
			}
			
			System.out.println("Final:");
			System.out.println("bestIsolatedPawnValue: "+ bestIsolatedPawnValue);
			System.out.println("bestDoubledPawnValue: "+ bestDoubledPawnValue);
			System.out.println("bestHalfOpenFileValue: "+ bestHalfOpenFileValue);
			System.out.println("bestPawnChainValue: "+ bestPawnChainValue);
			System.out.println("bestHoleValue: "+ bestHoleValue);
		}
		
	}
}
