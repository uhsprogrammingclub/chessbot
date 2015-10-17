package chessbot;

import java.util.ArrayList;

public class Empty extends Pieces{
	
	//Empty squares have no worth
	int worth;
	String symbol;

	@Override
	//Empty squares have no possible moves; therefore, return null.
	public ArrayList<int[]> findMoves(Board b) {
		return null;
	}

	@Override
	public int getWorth() {
		return worth;
	}
	
	@Override
	//getSymbol() method which returns the symbol for the piece
	public String getSymbol(){
		return symbol;
	}
	
	public Empty(){
		symbol = "-";
		worth = 0;
	}

}
