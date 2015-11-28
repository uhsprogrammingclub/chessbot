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
		Game.loadBitboards();
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
			//for(int l = 0; l < 2048; l++){
				//b.history[l] = new HistoryEntry();
			//}
			
			// playerTwo has experimental features, playerOne does not
			while(true){
				
				AI playerOne = new AI(b);
				
				//Set the AIController to the accepted settings
				
				//Check if the game is over...
				if(b.isGameOver()){
					System.out.println(b);
					if(b.evaluateBoard() > 1000){
						System.out.println("The player with the baseline settings lost a game!");
						playerTwoWins++;
					}else{
						System.out.println("Stalemate.");
					}
					
					break;
				}
			
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

					if(b.evaluateBoard() < -1000){
						System.out.println("The player with the experimental settings lost a game!");
						playerOneWins++;
						break;
					}else{
						System.out.println("Stalemate.");
					}
					
				}
				
				AI playerTwo = new AI(b);

				//For the experimental...
				AIController.quiescenceSearch = true;
				playerTwo.search();
				if (playerTwo.AIC.bestRootMove != null) {
					playerTwo.AIC.bestRootMove.move.execute();
				}
				
				System.out.println(b);
				System.out.println(playerTwo.AIC.bestRootMove + " " + b.evaluateBoard());
				//System.out.println("Fifty Moves: " + b.fiftyMove + " Half Moves: " + b.halfMoves);
				
				AIController.quiescenceSearch = false;
		
			}
			
		}
		
		System.out.println("Player Two Wins: " + playerTwoWins + " Player One Wins: " + playerOneWins);
		
		
		
		}
	}
