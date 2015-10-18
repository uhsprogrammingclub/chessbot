package chessbot;

public class Move {

	Point point;
	Piece piece;
	
	public Move(Point pt, Piece pc){
		point = pt;
		piece = pc;
	}

	@Override
	public String toString() {
		return "Move piece " + piece + " from point " + piece.position + " to point " + point;
	}

}
