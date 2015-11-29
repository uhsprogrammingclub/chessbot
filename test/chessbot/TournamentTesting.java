package chessbot;

import org.junit.Before;
import org.junit.Test;

public class TournamentTesting {
	
	@Before
	public void init() {
		Zobrist.zobristFillArray();
		Game.loadBitboards();
		OpeningBook book = new OpeningBook(Utils.boardFromFEN(Game.defaultSetup));
		Thread bookThread = new Thread(book, "Opening Book Thread");  
		bookThread.start(); 
	}
	
	@Test
	public void TestAgainstSelf(){
		
		AIController.timeLimit = 500;
		AIController.useOpeningBook = true;
		int numGames = 10;
		
		int playerOneWins = 0;
		int playerTwoWins = 0;
		
		for(int i = 0; i < numGames; i++){
			
			TranspositionTable.trans.clear();
			
			Board b = Utils.boardFromFEN(Game.defaultSetup);
			
			// playerTwo has experimental features, playerOne does not
			while(true){
				
				TranspositionTable.trans.clear();
				
				AI playerOne = new AI(b);
				
				//Set the AIController to the accepted settings
				
				//Check if the game is over...
				if(b.isGameOver()){
					System.out.println(b);
					if(b.evaluateBoard() > 10000){
						System.out.println("The player with the baseline settings lost a game!");
						playerTwoWins++;
					}else{
						System.out.println("Stalemate.");
						System.exit(0);
					}
					
					break;
				}
			
				Evaluation.contemptFactor = 9999;
				playerOne.search();
				if (playerOne.AIC.bestRootMove != null) {
					
					playerOne.AIC.bestRootMove.move.execute();
				}
			
				System.out.println(b);
				System.out.println(playerOne.AIC.bestRootMove + " " + b.evaluateBoard());
				
				//Check if the game is over...
				if(b.isGameOver()){
					System.out.println(b.evaluateBoard());
					System.out.println(b);

					if(b.evaluateBoard() < -10000){
						System.out.println("The player with the experimental settings lost a game!");
						playerOneWins++;
						break;
					}else{
						System.out.println("Stalemate.");
						System.exit(0);
					}
					
				}
				
				TranspositionTable.trans.clear();
				
				AI playerTwo = new AI(b);
				
				//For the experimental...
				AIController.useTTEvals = true;
				AIController.killerHeuristic = true;
				AIController.quiescenceSearch = true;
				Evaluation.contemptFactor = -9999;

				AIController.iterativeDeepeningMoveReordering = true;
				playerTwo.search();
				if (playerTwo.AIC.bestRootMove != null) {
					playerTwo.AIC.bestRootMove.move.execute();
				}
				
				System.out.println(b);
				System.out.println(playerTwo.AIC.bestRootMove + " " + b.evaluateBoard());
				
				AIController.useTTEvals = false;
				AIController.killerHeuristic = false;
				AIController.quiescenceSearch = false;
				AIController.iterativeDeepeningMoveReordering = false;
		
			}
			
		}
		
		System.out.println("Player Two Wins: " + playerTwoWins + " Player One Wins: " + playerOneWins);
		
		
		
		}
	}
