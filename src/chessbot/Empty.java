package chessbot;

import java.util.*;

public class Empty extends Piece{
	
	//Empty squares have no worth
	int worth;
	//String symbol;

	@Override
	//Empty squares have no possible moves; therefore, return null.
	public List<Move> findMoves(Board b) {
		return new ArrayList<Move>(); // empty list
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
