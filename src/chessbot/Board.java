package chessbot;

import java.util.ArrayList;

//Class to hold board variables and methods
public class Board {
	
	/** Note about board: the 7th and 8th rows are for the Player, the 1st and 2nd rows are for the computer. **/
	
	//Two-dimensional array to hold the locations of all of the pieces
	Piece[][] locations = new Piece[8][8];
	
	//Override java.lang.Object.toString method to create easier to read output in the form of a table
	@Override
	public String toString() {
			
			//String variable to eventually return
			String aString = "Board: \n";
			
			//Nested loops getting values
			for(int row = 0; row < locations.length; row++) {
				for(int col = 0; col < locations[row].length; col++) {
					aString += " " + locations[row][col].toString();
				}
				
				//Create a new line
				aString += "\n";
			}
			
			return aString;
	       
	}
	
	//Function that returns true if a certain square is empty
	public boolean isEmptySquare(int x, int y){
		
		//Load the piece into variable spot
		Piece spot = locations[x][y];
		
		//If the piece has a value of 0, it has to be empty
		if(spot.getWorth() == 0){
			return true;
		}else{
			return false;
		}
	}
	
	//Function that determines whether a square exists
	public boolean squareExists(int x, int y){
		
		if( x >= 0 && x < 8 && y >= 0 && y <= 8){
			return true;
		}else{
			return false;
		}
		
	}
	
	//Function that returns the team of the piece at the specified locations. 
	//True = player; False = computer
	/** Function does not validate square, so it MUST EXIST OR WILL THROW ARRAY INDEX OUT OF BOUNDS **/
	public boolean getTeam(int x, int y){
		Piece p = locations[x][y];
		return p.isPlayer;
	}
		
	//Constructor for the Board class
	public Board(ArrayList<Piece> list){
		
		//Fill the locations array with empty squares
		for(int i = 0; i < locations.length; i++){
			for(int j = 0; j < locations.length; j++){
				Empty temp = new Empty();
				locations[i][j] = temp;
			}
		}
		
		//Place the pieces passed in the list
		for(Piece piece: list){
			locations[piece.getX()][piece.getY()] = piece;
		}		
	}
	
	//Function that retrieves numeric value assigned to position. High values are good for the player, low values good for the computer
	public int evaluateBoard(){
		int score = scoreBoard(true) - scoreBoard(false);
		return score;
	}
	
	//Analyze the board, and assign a numeric value to it for the position based on how favorable it is for the designated player.
	public int scoreBoard(boolean isPlayer){
		
		//Default the score to zero
		int score = 0;
		
		//Sum the worth of all of the pieces to get a base score
		
		return score;
	}
	
}
