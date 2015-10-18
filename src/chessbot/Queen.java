package chessbot;

import java.util.*;

//Methods for the Queen Piece
public class Queen extends Piece{
	
	@Override
	//findMoves() method which identifies possible moves
	//The Queen Piece can move vertically, horizontally, or diagonally as far as it can
	public List<Move> findMoves(Board b){
		
		List<Move> moves = new ArrayList<Move>();
		/*
		//Variables to keep track of pieces blocking
		boolean upBlock = false;
		boolean downBlock = false;
		boolean leftBlock = false;
		boolean rightBlock = false;
		boolean upRightBlock = false;
		boolean upLeftBlock = false;
		boolean downRightBlock = false;
		boolean downLeftBlock = false;
		
		//Find moves using for-loop 
		for(int i = 1; i < 8; i++){
			
			//Ensure no array index out of bounds for moving UP
			if( (this.getX() + i < 8) && !upBlock){
				
				//Create move - may or may not add
				Point move = new Point(this.getX() + i, this.getY());
				
				//Check if the square is empty
				if(b.isEmptySquare(this.getX() + i, this.getY())){
					moves.add(move);
				}else{
					upBlock = true;
					moves.add(move);
				}
				
			}
			
			//Ensure no array index out of bounds for moving DOWN
			if( (this.getX() - i >= 0)  && !downBlock) { 
				
				//Create move - may or may not add
				Point move = new Point(this.getX() - i, this.getY());
				
				//Check if the square is empty
				if(b.isEmptySquare(this.getX() - i, this.getY())){
					moves.add(move);
				}else{
					downBlock = true;
					moves.add(move);
				}
			}
			
			
			//Ensure no array index out of bounds for moving RIGHT
			if( (this.getY() + i < 8)  && !rightBlock) { 
				
				//Create move - may or may not add
				Point move = new Point(this.getX(), this.getY() + i);
				
				//Check if the square is empty
				if(b.isEmptySquare(this.getX(), this.getY() + i)){
					moves.add(move);
				}else{
					rightBlock = true;
					moves.add(move);
				}
			}
			
			//Ensure no array index out of bounds for moving LEFT
			if( (this.getY() - i >= 0)  && !leftBlock) { 
				
				//Create move - may or may not add
				Point move = new Point(this.getX(), this.getY() - i);
				
				//Check if the square is empty
				if(b.isEmptySquare(this.getX(), this.getY() - i)){
					moves.add(move);
				}else{
					leftBlock = true;
					moves.add(move);
				}
			}
			
			//Ensure no array index out of bounds for UP-RIGHT
			if( (this.getY() + i < 8)  && (this.getX() + i < 8) && !upRightBlock) { 
				
				//Create move - may or may not add
				Point move = new Point(this.getX() + i, this.getY() + i);
				
				//Check if the square  is empty
				if(b.isEmptySquare(this.getX() + i, this.getY() + i)){
					moves.add(move);
				}else{
					upRightBlock = true;
					moves.add(move);
				}
			}
			
			//Ensure no array index out of bounds for UP-LEFT
			if( (this.getY() - i >= 0)  && (this.getX() + i < 8) && !upLeftBlock) { 
				
				//Create move - may or may not add
				Point move = new Point(this.getX() + i, this.getY() - i);
				
				//Check if the square  is empty
				if(b.isEmptySquare(this.getX() + i, this.getY() - i)){
					moves.add(move);
				}else{
					upLeftBlock = true;
					moves.add(move);
				}
			}
			
			//Ensure no array index out of bounds for DOWN-RIGHT
			if( (this.getY() + i < 8)  && (this.getX() - i >= 0) && !downRightBlock) { 
				
				//Create move - may or may not add
				Point move = new Point(this.getX() - i, this.getY() + i);
				
				//Check if the square  is empty
				if(b.isEmptySquare(this.getX() - i, this.getY() + i)){
					moves.add(move);
				}else{
					downRightBlock = true;
					moves.add(move);
				}
			}
			
			//Ensure no array index out of bounds for DOWN-LEFT
			if( (this.getY() - i >= 0)  && (this.getX() - i >= 0) && !downLeftBlock) { 
				
				//Create move - may or may not add
				Point move = new Point(this.getX() - i, this.getY() - i);
				
				//Check if the square  is empty
				if(b.isEmptySquare(this.getX() - i, this.getY() - i)){
					moves.add(move);
				}else{
					downLeftBlock = true;
					moves.add(move);
				}
			}
		}
		*/
		return moves;
	}
	
	@Override
	//getWorth() method which returns the worth of the piece
	public int getWorth(){
		return worth;
	}
	
	//Constructor
	public Queen(int x, int y, boolean player){
		
		//Setting base values for the Queen piece
		worth = 9;
		isPlayer = player;
		symbol = "q";
		
		//Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
