package chessbot;

import java.util.ArrayList;

//Methods for the Queen Piece
public class Queen extends Pieces{
	
	@Override
	//findMoves() method which identifies possible moves
	//The Queen Piece can move vertically, horizontally, or diagonally as far as it can
	public ArrayList<int[]> findMoves(Board b){
		
		ArrayList<int[]> moves = new ArrayList<int[]>();
		
		//Variables to keep track of pieces blocking
		Boolean upBlock = false;
		Boolean downBlock = false;
		Boolean leftBlock = false;
		Boolean rightBlock = false;
		Boolean upRightBlock = false;
		Boolean upLeftBlock = false;
		Boolean downRightBlock = false;
		Boolean downLeftBlock = false;
		
		//Find moves using for-loop 
		for(int i = 1; i < 8; i++){
			
			//Ensure no array index out of bounds for moving UP
			if( (this.x + i < 8) && !upBlock){
				
				//Create move - may or may not add
				int[] move = {this.x + i, this.y};
				
				//Check if the square is empty
				if(b.isEmptySquare(this.x + i, this.y)){
					moves.add(move);
				}else{
					upBlock = true;
					moves.add(move);
				}
				
			}
			
			//Ensure no array index out of bounds for moving DOWN
			if( (this.x - i >= 0)  && !downBlock) { 
				
				//Create move - may or may not add
				int[] move = {this.x - i, this.y};
				
				//Check if the square is empty
				if(b.isEmptySquare(this.x - i, this.y)){
					moves.add(move);
				}else{
					downBlock = true;
					moves.add(move);
				}
			}
			
			
			//Ensure no array index out of bounds for moving RIGHT
			if( (this.y + i < 8)  && !rightBlock) { 
				
				//Create move - may or may not add
				int[] move = {this.x, this.y + i};
				
				//Check if the square is empty
				if(b.isEmptySquare(this.x, this.y + i)){
					moves.add(move);
				}else{
					rightBlock = true;
					moves.add(move);
				}
			}
			
			//Ensure no array index out of bounds for moving LEFT
			if( (this.y - i >= 0)  && !leftBlock) { 
				
				//Create move - may or may not add
				int[] move = {this.x, this.y - i};
				
				//Check if the square is empty
				if(b.isEmptySquare(this.x, this.y - i)){
					moves.add(move);
				}else{
					leftBlock = true;
					moves.add(move);
				}
			}
			
			//Ensure no array index out of bounds for UP-RIGHT
			if( (this.y + i < 8)  && (this.x + i < 8) && !upRightBlock) { 
				
				//Create move - may or may not add
				int[] move = {this.x + i, this.y + i};
				
				//Check if the square  is empty
				if(b.isEmptySquare(this.x + i, this.y + i)){
					moves.add(move);
				}else{
					upRightBlock = true;
					moves.add(move);
				}
			}
			
			//Ensure no array index out of bounds for UP-LEFT
			if( (this.y - i >= 0)  && (this.x + i < 8) && !upLeftBlock) { 
				
				//Create move - may or may not add
				int[] move = {this.x + i, this.y - i};
				
				//Check if the square  is empty
				if(b.isEmptySquare(this.x + i, this.y - i)){
					moves.add(move);
				}else{
					upLeftBlock = true;
					moves.add(move);
				}
			}
			
			//Ensure no array index out of bounds for DOWN-RIGHT
			if( (this.y + i < 8)  && (this.x - i >= 0) && !downRightBlock) { 
				
				//Create move - may or may not add
				int[] move = {this.x - i, this.y + i};
				
				//Check if the square  is empty
				if(b.isEmptySquare(this.x - i, this.y + i)){
					moves.add(move);
				}else{
					downRightBlock = true;
					moves.add(move);
				}
			}
			
			//Ensure no array index out of bounds for DOWN-LEFT
			if( (this.y - i >= 0)  && (this.x - i >= 0) && !downLeftBlock) { 
				
				//Create move - may or may not add
				int[] move = {this.x - i, this.y - i};
				
				//Check if the square  is empty
				if(b.isEmptySquare(this.x - i, this.y - i)){
					moves.add(move);
				}else{
					downLeftBlock = true;
					moves.add(move);
				}
			}
		}
		
		return moves;
	}
	
	@Override
	//getWorth() method which returns the worth of the piece
	public int getWorth(){
		return worth;
	}
	
	@Override
	//getSymbol() method which returns the symbol for the piece
	public String getSymbol(){
		return symbol;
	}
	
	//Constructor
	public Queen(int x, int y, Boolean player){
		
		//Setting base values for the Queen piece
		worth = 9;
		isPlayer = player;
		
		//Draw distinctions between players
		if(player){
			symbol = "Q";
		}else{
			symbol = "q";
		}
		
		//Using accessory method for clarity; not strictly necessary
		setPosition(x, y);
	}
}
