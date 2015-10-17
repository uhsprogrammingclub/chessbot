package chessbot;

import java.util.ArrayList;

public class Empty extends Piece{
	
	//Empty squares have no worth
	int worth;
	//String symbol;

	@Override
	//Empty squares have no possible moves; therefore, return null.
	public ArrayList<Point> findMoves(Board b) {
		return null;
	}

	@Override
	public int getWorth() {
		return worth;
	}
	
	public Empty(){
		symbol = "-";
		worth = 0;
	}

}
