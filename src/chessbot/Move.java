package chessbot;

public class Move {

	Point point;
	Piece piece = new Empty();
	Piece destinationPc = new Empty();
	boolean executed = false;
	Board board;
	
	public Move(Board b, Point pt, Piece pc){
		board = b;
		point = pt;
		piece = pc;
		if (pt.squareExists()){ //checks that doesn't look out of bounds
			destinationPc = b.locations[pt.x][pt.y]; 
			
		}else{
			destinationPc.setPosition(pt.x, pt.y);//for toString() reasons
		}
	}
	
	public Move(Board b, Point from, Point to){
		board = b;
		point = to;
		if (from.squareExists()){ //checks that doesn't look out of bounds
			piece = b.locations[from.x][from.y]; 
			
		}else{
			piece.setPosition(from.x, from.y);//for toString() reasons
		}
		
		if (to.squareExists()){ //checks that doesn't look out of bounds
			destinationPc = b.locations[to.x][to.y]; 
			
		}else{
			destinationPc.setPosition(to.x, to.y);//for toString() reasons
		}
	}
	
	void execute(){
		if(!executed){
			board.locations[point.x][point.y] = piece;
			board.locations[piece.position.x][piece.position.y] = new Empty();
			destinationPc.position = piece.position; // reverse positions
			piece.position = point;
			executed = true;
		}
	}
	
	void reverse(){
		if(executed){
			board.locations[point.x][point.y] = destinationPc;
			board.locations[destinationPc.position.x][destinationPc.position.y] = piece;
			piece.position = destinationPc.position;
			destinationPc.position = point;
			executed = false;
		}
	}

	@Override
	public String toString() {
		return "Move piece " + piece + " from point " + piece.position + " to point " + point + ", which is a " + destinationPc;
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
