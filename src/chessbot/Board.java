package chessbot;

import java.util.ArrayList;

//Class to hold board variables and methods
public class Board {
	
	//Two-dimensional array to hold the locations of all of the pieces
	Pieces[][] locations = new Pieces[8][8];
	
	//Override java.lang.Object.toString method to create easier to read output in the form of a table
	public String toString() {
			
			//String variable to eventually return
			String aString = "Board: \n";
			
			//Nested loops getting values
			for(int row = 0; row < locations.length; row++) {
				for(int col = 0; col < locations[row].length; col++) {
					aString += " " + locations[row][col].getSymbol();
				}
				
				//Create a new line
				aString += "\n";
			}
			
			return aString;
	       
	}
		
	//Constructor for the Board class
	public Board(ArrayList<Pieces> list){
		
		//Fill the locations array with empty squares
		for(int i = 0; i < locations.length; i++){
			for(int j = 0; j < locations.length; j++){
				Empty temp = new Empty();
				locations[i][j] = temp;
			}
		}
		
		//Place the pieces passed in the list
		for(Pieces piece: list){
			locations[piece.x][piece.y] = piece;
		}
		
	}
}
