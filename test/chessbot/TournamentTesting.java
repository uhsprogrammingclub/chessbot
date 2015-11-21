package chessbot;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TournamentTesting {


	@Before
	public void init() {
		Zobrist.zobristFillArray();
	}
	
	boolean BratkoKopec = true;

	@Test
	public void BratkoKopecSuite() throws IOException {

		if (!BratkoKopec) {
			return;
		}

		// Fill this array with the different times you want tested...
		int[] computationTimes = { 10000 };

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

			int correct = 0;

			for (String edp : tests) {

				Map<String, String[]> info = Utils.edpGetInfo(edp);
				Board b = Utils.boardFromFEN(Utils.edpGetFEN(edp));
				AIController.setComputationTime(time);

				AI ai = new AI(b);
				ai.search();
				String botMove = "";
				if (ai.AIC.bestRootMove != null) {
					botMove = ai.AIC.bestRootMove.move.getSAN();
				}
				System.out.println(b);

				System.out.println("Test ID: " + Arrays.toString(info.get("id")) + " Actual Best Moves: "
						+ Arrays.toString(info.get("bm")) + " Computer Move: [" + botMove + "]");

				for (String s : info.get("bm")) {
					if(s.equals(botMove)){
						correct++;
					}
				}
			}
			System.out.println("Time Allowed: " + time + " Number Correct: " + correct + "/" + lines);

		}

	}

}
