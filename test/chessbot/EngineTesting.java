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
		int[] computationTimes = {5000};

		String filePath = new File("").getAbsolutePath() + "/Win_At_Chess_Suite";

		FileReader fr = new FileReader(filePath);
		BufferedReader textReader = new BufferedReader(fr);

		int lines = 300;
		String[] tests = new String[lines];

		for (int i = 0; i < lines; i++) {
			tests[i] = textReader.readLine();
		}
		textReader.close();

		List<String> results = new ArrayList<String>();
		List<String> fails = new ArrayList<String>();

		for (int time : computationTimes) {

			int correct = 0;

			for (String edp : tests) {
				
				Map<String, String[]> info = Utils.edpGetInfo(edp);
				Board b = Utils.boardFromFEN(Utils.edpGetFEN(edp));
				AIController.setComputationTime(time);

				AI ai = new AI(b);
				ai.search();
				String botMove = ai.AIC.bestRootMove.move.getSAN();
				System.out.println(b);
				
				System.out.println("Test ID: " + Arrays.toString(info.get("id")) + " Actual Best Moves: "
						+ Arrays.toString(info.get("bm")) + " Computer Move: [" + botMove + "]");

				for (String s : info.get("bm")) {
					if (s.equals(botMove)){
						correct++;
					}else{
						fails.add(edp);
					}
						
				}
			}
			results.add("Time Allowed: " + time + " Number Correct: " + correct + "/" + lines);
		}
		
		for(String s : results){
			System.out.println(s);
		}
		for(String s : fails){
			System.out.println(s);
		}
	}

	@Test
	public void test() throws IOException {
		
		if(!BratkoKopec){
			return;
		}

		//Fill this array with the different times you want tested...
		int[] computationTimes = {10000};

		String filePath = new File("").getAbsolutePath() + "/Bratko-Kopec_Test";

		FileReader fr = new FileReader(filePath);
		BufferedReader textReader = new BufferedReader(fr);

		int lines = 24;
		String[] tests = new String[lines];

		for (int i = 0; i < lines; i++) {
			tests[i] = textReader.readLine();
		}
		textReader.close();

		List<String> results = new ArrayList<String>();

		for (int time : computationTimes) {

			int correct = 0;

			for (String edp : tests) {
				
				Map<String, String[]> info = Utils.edpGetInfo(edp);
				Board b = Utils.boardFromFEN(Utils.edpGetFEN(edp));
				AIController.setComputationTime(time);

				AI ai = new AI(b);
				ai.search();
				String botMove = ai.AIC.bestRootMove.move.getSAN();
				System.out.println(b);
				
				System.out.println("Test ID: " + Arrays.toString(info.get("id")) + " Actual Best Moves: "
						+ Arrays.toString(info.get("bm")) + " Computer Move: [" + botMove + "]");

				for (String s : info.get("bm")) {
					if (s.equals(botMove))
						correct++;
				}
			}
			results.add("Time Allowed: " + time + " Number Correct: " + correct + "/24");
		}
		
		for(String s : results){
			System.out.println(s);
		}
	}
}
