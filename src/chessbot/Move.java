package chessbot;

public class Move {

	Point from;
	Point to;
	Piece piece = null;
	Piece destinationPc = null;
	boolean executed = false;
	Board board;
	Piece promotionPiece = null;
	
	public Move(Move m){
		
		board = m.board;
		piece = m.piece;
		from = new Point(piece.position.x, piece.position.y);
		to = m.to;
		promotionPiece = m.promotionPiece;
		
		if (to.squareExists()) { // checks that doesn't look out of bounds
			destinationPc = board.locations[to.x][to.y];

		} else {
			destinationPc = new Empty();
			destinationPc.setPosition(to.x, to.y);// for toString() reasons
		}
	}

	public Move(Board b, Point pt, Piece pc, String promotionPiece) {
		
		if(promotionPiece != null){
			if(promotionPiece.equals("q")){
				this.promotionPiece = new Queen(pc.getX(), pc.getY(), pc.player);
			}
			else if(promotionPiece.equals("n")){
				this.promotionPiece = new Knight(pc.getX(), pc.getY(), pc.player);
			}
			else if(promotionPiece.equals("r")){
				this.promotionPiece = new Rook(pc.getX(), pc.getY(), pc.player);
			}
			else if(promotionPiece.equals("b")){
				this.promotionPiece = new Bishop(pc.getX(), pc.getY(), pc.player);
			}
		}
		
		board = b;
		piece = pc;
		from = new Point(pc.position.x, pc.position.y);
		to = pt;
		if (to.squareExists()) { // checks that doesn't look out of bounds
			destinationPc = b.locations[pt.x][pt.y];

		} else {
			destinationPc = new Empty();
			destinationPc.setPosition(pt.x, pt.y);// for toString() reasons
		}
	}

	public Move(Board b, Point from, Point to, String promotionPiece) {
		
		if(promotionPiece != null){
			if(promotionPiece.equals("q")){
				this.promotionPiece = new Queen(from.x, from.y, b.locations[from.x][from.y].player);
			}
			else if(promotionPiece.equals("n")){
				this.promotionPiece = new Knight(from.x, from.y, b.locations[from.x][from.y].player);
			}
			else if(promotionPiece.equals("r")){
				this.promotionPiece = new Rook(from.x, from.y, b.locations[from.x][from.y].player);
			}
			else if(promotionPiece.equals("b")){
				this.promotionPiece = new Bishop(from.x, from.y, b.locations[from.x][from.y].player);
			}
			
		}
		
		board = b;
		this.from = from;
		this.to = to;
		if (from.squareExists()) { // checks that doesn't look out of bounds
			piece = b.locations[from.x][from.y];

		} else {
			piece = new Empty();
			piece.setPosition(from.x, from.y);// for toString() reasons
		}

		if (to.squareExists()) { // checks that doesn't look out of bounds
			destinationPc = b.locations[to.x][to.y];

		} else {
			destinationPc = new Empty();
			destinationPc.setPosition(to.x, to.y); // for toString() reasons
		}
	}

	void execute() {
		if (!executed) {
			
			if (board.isEmptySquare(destinationPc.position)){
				board.locations[from.x][from.y] = destinationPc;
			}else{
				board.locations[from.x][from.y] = new Empty();
			}
			
			if(promotionPiece != null){
				board.locations[to.x][to.y] = promotionPiece;
				promotionPiece.position = to;
				board.pieceList.add(promotionPiece);
				board.pieceList.remove(piece);
				piece.alive = false;
			}else{
		
				board.locations[to.x][to.y] = piece;
				piece.position = to;
			}
			
			destinationPc.position = from; // reverse positions
			destinationPc.alive = false;
			executed = true;
			
		}else{
			System.out.println("ERROR: Trying to execute a move that is no longer viable.");
			System.exit(0);
		}
	}

	void reverse() {
		if (executed) {
			if(promotionPiece != null){
				piece = new Pawn(from.x, from.y, piece.player);
			}
			board.locations[to.x][to.y] = destinationPc;
			board.locations[from.x][from.y] = piece;
			piece.position = from;
			destinationPc.position = to;
			destinationPc.alive = true;
			executed = false;
		}else{
			System.out.println("ERROR: Trying to reverse a move that was never called.");
			System.exit(0);
		}
	}

	@Override
	public String toString() {
		return "Move piece " + piece + " from point " + from + " to point " + to + ", which is a "
				+ destinationPc;
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
		if (other.to == null || other.piece == null || this.to == null || this.piece == null) {
			return false;
		}
		if (this.to.equals(other.to) && this.piece.equals(other.piece)){
			return true;
		} else {
			return false;
		}
	}
}
