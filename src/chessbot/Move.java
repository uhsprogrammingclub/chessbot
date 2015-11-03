package chessbot;

public class Move {

	Point from;
	Point to;
	Piece piece = null;
	Piece destinationPc = null;
	boolean executed = false;
	Board board;
	Piece promotionPiece = null;
	boolean promotionMove = false;
	
	boolean playerKSideCastleO = false;
	boolean playerQSideCastleO = false;
	boolean botKSideCastleO = false;
	boolean botQSideCastleO = false;
	
	boolean castleMove = false;
	Move castleRookMove = null;
	
	//boolean checkMove = false; //if the move causes a check
	
	public Move(Move m){
		
		board = m.board;
		from = m.from;
		to = m.to;
		
		playerKSideCastleO = board.playerKSideCastle;
	    playerQSideCastleO = board.playerQSideCastle;
	    botKSideCastleO = board.botKSideCastle;
		botQSideCastleO = board.botQSideCastle;
		
		promotionMove = m.promotionMove;
		castleMove = m.castleMove;
		if (promotionMove){
			if(m.promotionPiece.symbol.equals("q")){
				this.promotionPiece = new Queen(m.to.x, m.to.y, m.promotionPiece.player);
			}
			else if(m.promotionPiece.symbol.equals("n")){
				this.promotionPiece = new Knight(m.to.x, m.to.y, m.promotionPiece.player);
			}
			else if(m.promotionPiece.symbol.equals("r")){
				this.promotionPiece = new Rook(m.to.x, m.to.y, m.promotionPiece.player);
			}
			else if(m.promotionPiece.symbol.equals("b")){
				this.promotionPiece = new Bishop(m.to.x, m.to.y, m.promotionPiece.player);
			}
			this.promotionPiece.alive = false;
		}else{
			promotionPiece = null;
		}
		
		piece = board.getPiece(from);
		destinationPc = board.getPiece(to);
	}

	public Move(Board b, Point pt, Piece pc, String promotionPiece) {
		
		playerKSideCastleO = b.playerKSideCastle;
	    playerQSideCastleO = b.playerQSideCastle;
	    botKSideCastleO = b.botKSideCastle;
		botQSideCastleO = b.botQSideCastle;
		
		if(promotionPiece != null && !promotionPiece.equals("")){
			promotionMove = true;
			if(promotionPiece.equals("q")){
				this.promotionPiece = new Queen(pt.x, pt.y, pc.player);
			}
			else if(promotionPiece.equals("n")){
				this.promotionPiece = new Knight(pt.x, pt.y, pc.player);
			}
			else if(promotionPiece.equals("r")){
				this.promotionPiece = new Rook(pt.x, pt.y, pc.player);
			}
			else if(promotionPiece.equals("b")){
				this.promotionPiece = new Bishop(pt.x, pt.y, pc.player);
			}
			else{
				promotionMove = false;
				System.out.println("ERROR: invlaid promotion piece");
			}
		}
		if (promotionMove){
			this.promotionPiece.alive = false;
		}
		
		
		board = b;
		piece = pc;
		from = new Point(pc.position.x, pc.position.y);
		to = pt;
		
		if (piece.symbol == "k" && Math.abs(to.x - from.x) == 2){
			castleMove = true;
		}
		destinationPc = b.getPiece(pt);
	}

	public Move(Board b, Point from, Point to, String promotionPiece) {
		
		playerKSideCastleO = b.playerKSideCastle;
	    playerQSideCastleO = b.playerQSideCastle;
	    botKSideCastleO = b.botKSideCastle;
		botQSideCastleO = b.botQSideCastle;
		
		if(promotionPiece != null && !promotionPiece.equals("")){
			promotionMove = true;
			if(promotionPiece.equals("q")){
				this.promotionPiece = new Queen(to.x, to.y, b.getTeam(from));
			}
			else if(promotionPiece.equals("n")){
				this.promotionPiece = new Knight(to.x, to.y, b.getTeam(from));
			}
			else if(promotionPiece.equals("r")){
				this.promotionPiece = new Rook(to.x, to.y, b.getTeam(from));
			}
			else if(promotionPiece.equals("b")){
				this.promotionPiece = new Bishop(to.x, to.y, b.getTeam(from));
			}
			else{
				promotionMove = false;
				System.out.println("ERROR: invlaid promotion piece");
			}			
		}
		
		if (promotionMove){
			this.promotionPiece.alive = false;
		}
		
		board = b;
		this.from = from;
		this.to = to;
		
		
		piece = b.getPiece(from);
		destinationPc = b.getPiece(to);
		
		if (piece.symbol == "k" && Math.abs(to.x - from.x) == 2){
			castleMove = true;
		}
		
	}

	void execute() {
		if (!executed) {
			
			if (castleMove){
				if (castleRookMove == null){
					//Check if it is a king-side castle move
					if((to.x - from.x) == 2){
						castleRookMove = new Move(board, new Point(5, from.y), board.getPiece(new Point(7, from.y)), null);
					}
					//Check if it is a queen-side castle move
					else{
						castleRookMove = new Move(board, new Point(3, from.y), board.getPiece(new Point(0, from.y)), null);
					}
				}
				castleRookMove.execute();
				board.playerMove = !board.playerMove;
			}
			
			if (board.isEmptySquare(destinationPc.position)){
				board.setSquare(from, destinationPc);
			}else{
				board.setSquare(from, new Empty());
			}
			
			if(promotionMove){
				board.setSquare(to,  promotionPiece);
				board.pieceList.add(promotionPiece);
				
				promotionPiece.alive = true;
				piece.alive = false;
			}else{
		
				board.setSquare(to, piece);
			}
			
			//Change the castling variables depending on the piece being moved
			if(piece.symbol.equals("k")){
				if(piece.player){
					board.playerKSideCastle = board.playerQSideCastle = false;
				}else{
					board.botKSideCastle = board.botQSideCastle = false;
				}
			}
			
			if(piece.symbol.equals("r")){
				if (from.x == 0){
					if(piece.player){
						board.playerQSideCastle = false;
					}else{
						board.botQSideCastle = false;
					}
				}else if (from.x == 7){
					if(piece.player){
						board.playerKSideCastle = false;
					}else{
						board.botKSideCastle = false;
					}
				}
			}
			
			destinationPc.alive = false;
			executed = true;
			board.playerMove = !board.playerMove;
		}else{
			System.out.println("ERROR: Trying to execute a move that is no longer viable.");
			System.exit(0);
		}
	}

	void reverse() {
		if (executed) {
			
			//Check if it is a castling move...
			if(castleMove){
				castleRookMove.reverse();
				board.playerMove = !board.playerMove;
			}
			
			if(promotionMove){
				board.pieceList.remove(promotionPiece);
				
				promotionPiece.alive = false;
				piece.alive = true;
				
			}
			
			board.playerKSideCastle = playerKSideCastleO;
			board.playerQSideCastle = playerQSideCastleO;
			board.botKSideCastle = botKSideCastleO;
			board.botQSideCastle = botQSideCastleO;	
			
			board.setSquare(to,  destinationPc);
			board.setSquare(from, piece);
			destinationPc.alive = true;
			executed = false;
			board.playerMove = !board.playerMove;
		}else{
			System.out.println("ERROR: Trying to reverse a move that was never called.");
			System.exit(0);
		}
	}
	
	boolean isCapture(){
		if (destinationPc.worth == 0){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		String s = "";
		s += piece + "";
		s += from + "->";
		if (!destinationPc.toString().equals("-")){
			s += "x" + destinationPc;
		}
		s += this.to;
		if (promotionMove){
			s += "=" + promotionPiece;
		}
		return s;
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
		if (other.to == null || other.from == null || this.to == null || this.from == null) {
			return false;
		}
		if (this.promotionMove != other.promotionMove || this.castleMove != other.castleMove) {
			return false;
		}
		if (this.to.equals(other.to) && this.from.equals(other.from)){
			return true;
		} else {
			return false;
		}
	}
}
