package chessbot;

public class Move {

	Point point;
	Piece piece = new Empty();
	
	public Move(Point pt, Piece pc){
		point = pt;
		piece = pc;
	}
	
	public Move(Board b, Point from, Point to){
		point = to;
		if (from.squareExists()){ //checks that doesn't look out of bounds
			piece = b.locations[from.x][from.y]; 
			
		}else{
			piece.setPosition(from.x, from.y);//for toString() reasons
		}
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
	    if (other.point == null || other.piece == null || this.point == null || this.piece == null) {
	        return false;
	    }
	    if (this.point.equals(other.point) && this.piece.equals(other.piece)){
	    	return true;
	    }else{
	    	return false;
	    }
	}

}
