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
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (getClass() != obj.getClass()) {
	        return false;
	    }
	    final Move other = (Move) obj;
	    if (this.point.equals(other.point) && this.piece.equals(other.piece)){
	    	return true;
	    }else{
	    	return false;
	    }
	}

}
