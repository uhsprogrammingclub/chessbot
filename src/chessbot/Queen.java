package chessbot;

//Methods for the Queen Piece
public class Queen extends Pieces{
	
	//Value of the piece; can potentially change depending on context
	int worth;
	String symbol;
	
	@Override
	//findMoves() method which identifies possible moves
	public int[][] findMoves(){
		
		//NOTE: for testing purposes; obviously not actual moves
		int[][] moves = new int[][]{
			{5, 4},
			{3, 4}
		};	
		
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
	public Queen(int x, int y){
		worth = 9;
		symbol = "Q";
		setPosition(x, y);
	}
}
