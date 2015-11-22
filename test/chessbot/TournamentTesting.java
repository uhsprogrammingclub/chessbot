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
	
	@Test
	public void TestAgainstSelf(){
		
		
		AIController.timeLimit = 500;
		AIController.useOpeningBook = false;
		int numGames = 10;
		
		int playerOneWins = 0;
		int playerTwoWins = 0;
		
		for(int i = 0; i < numGames; i++){
			
			TranspositionTable.trans.clear();
			
			Board b = Utils.boardFromFEN(Game.defaultSetup);
			
			// playerTwo has experimental features, playerOne does not
			while(true){
				
				System.out.println(b);
				System.out.println("Evaluation: " + b.evaluateBoard());
				
				AI playerOne = new AI(b);
				
				//Set the AIController to the accepted settings
				
				//Check if the game is over...
				if(b.isGameOver()){
					System.out.println(b);
					if(b.evaluateBoard() != 0){
						System.out.println("The player with the baseline settings lost a game!");
						playerTwoWins++;
					}else{
						System.out.println("Stalemate.");
					}
					
					break;
				}
				
				AIController.usePawnEvals = false;
				playerOne.search();
				if (playerOne.AIC.bestRootMove != null) {
					
					playerOne.AIC.bestRootMove.move.execute();
					System.out.println(playerOne.AIC.bestRootMove);
				}
				
				AI playerTwo = new AI(b);
				
				//Check if the game is over...
				if(b.isGameOver()){
					System.out.println(b.evaluateBoard());
					System.out.println(b);

					if(b.evaluateBoard() != 0){
						System.out.println("The player with the experimental settings lost a game!");
						playerOneWins++;
						break;
					}else{
						System.out.println("Stalemate.");
					}
					
				}

				//For the experimental...
				AIController.usePawnEvals = true;
				playerTwo.search();
				if (playerTwo.AIC.bestRootMove != null) {
					playerTwo.AIC.bestRootMove.move.execute();
					System.out.println(playerTwo.AIC.bestRootMove);
				}
		
			}
			
			System.exit(0);
			
		}
		
		System.out.println("Player Two Wins: " + playerTwoWins + " Player One Wins: " + playerOneWins);
		
		
		
		
	}
	
	
	
	
	
	
	
	boolean BratkoKopec = false;

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
