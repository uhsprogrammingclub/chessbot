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
	
	boolean playerKSideCastleO = true;
	boolean playerQSideCastleO = true;
	boolean botKSideCastleO = true;
	boolean botQSideCastleO = true;
	
	boolean castleMove = false;
	
	public Move(Move m){
		
		board = m.board;
		piece = m.piece;
		from = new Point(piece.position.x, piece.position.y);
		to = m.to;
		
		playerKSideCastleO = board.playerKSideCastle;
	    playerQSideCastleO = board.playerQSideCastle;
	    botKSideCastleO = board.botKSideCastle;
		botQSideCastleO = board.botQSideCastle;
		
		promotionMove = m.promotionMove;
		if (promotionMove){
			if(m.promotionPiece.symbol.equals("q")){
				this.promotionPiece = new Queen(m.promotionPiece.getX(), m.promotionPiece.getY(), m.promotionPiece.player);
			}
			else if(m.promotionPiece.symbol.equals("n")){
				this.promotionPiece = new Knight(m.promotionPiece.getX(), m.promotionPiece.getY(), m.promotionPiece.player);
			}
			else if(m.promotionPiece.symbol.equals("r")){
				this.promotionPiece = new Rook(m.promotionPiece.getX(), m.promotionPiece.getY(), m.promotionPiece.player);
			}
			else if(m.promotionPiece.symbol.equals("b")){
				this.promotionPiece = new Bishop(m.promotionPiece.getX(), m.promotionPiece.getY(), m.promotionPiece.player);
			}
			this.promotionPiece.alive = false;
		}else{
			promotionPiece = null;
		}
		
		if (to.squareExists()) { // checks that doesn't look out of bounds
			destinationPc = board.locations[to.x][to.y];

		} else {
			destinationPc = new Empty();
			destinationPc.setPosition(to.x, to.y);// for toString() reasons
		}
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
		if (to.squareExists()) { // checks that doesn't look out of bounds
			destinationPc = b.locations[pt.x][pt.y];

		} else {
			destinationPc = new Empty();
			destinationPc.setPosition(pt.x, pt.y);// for toString() reasons
		}
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
		if (from.squareExists()) { // checks that doesn't look out of bounds
			piece = b.getPiece(from);

		} else {
			piece = new Empty();
			piece.setPosition(from.x, from.y);// for toString() reasons
		}

		if (to.squareExists()) { // checks that doesn't look out of bounds
			destinationPc = b.getPiece(to);

		} else {
			destinationPc = new Empty();
			destinationPc.setPosition(to.x, to.y); // for toString() reasons
		}
	}

	void execute() {
		if (!executed) {
			
			//Check if it is a king-side castle move
			if(piece.symbol == "k" && (to.x - from.x)  == 2){
				castleMove = true;
				Move m = new Move(board, new Point(5, from.y), board.locations[7][from.y], null);
				m.execute();
				board.playerMove = !board.playerMove;
			}
			
			//Check if it is a queen-side castle move
			if(piece.symbol == "k" && (to.x - from.x)  == -2){
				castleMove = true;
				Move m = new Move(board, new Point(2, from.y), board.locations[0][from.y], null);
				m.execute();
				board.playerMove = !board.playerMove;
			}
			
			if (board.isEmptySquare(destinationPc.position)){
				board.locations[from.x][from.y] = destinationPc;
			}else{
				board.locations[from.x][from.y] = new Empty();
			}
			
			if(promotionMove){
				board.locations[to.x][to.y] = promotionPiece;
				board.pieceList.add(promotionPiece);
				
				promotionPiece.alive = true;
				piece.alive = false;
			}else{
		
				board.locations[to.x][to.y] = piece;
				piece.position = to;
			}
			
			//Change the castling variables depending on the piece being moved
			if(piece.symbol.equals("k")){
				if(piece.player){
					board.playerKSideCastle = board.playerQSideCastle = false;
				}else{
					board.botKSideCastle = board.botQSideCastle = false;
				}
			}
			
			destinationPc.position = from; // reverse positions
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
				//If it is a king-side castle
				if(piece.getX() > 4){
					castleMove = false;
					Move m = new Move(board, new Point(7, from.y), board.locations[5][from.y], null);
					m.execute();
					board.playerMove = !board.playerMove;
					
				}else{
					castleMove = false;
					Move m = new Move(board, new Point(0, from.y), board.locations[2][from.y], null);
					m.execute();
					board.playerMove = !board.playerMove;
					
				}
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
			
			if(board.playerKSideCastle != playerKSideCastleO){
				board.playerKSideCastle = playerKSideCastleO;
			}
			
			if(piece.symbol.equals("k")){
				if(piece.player){
					board.playerKSideCastle = board.playerQSideCastle = true;
				}else{
					board.botKSideCastle = board.botQSideCastle = true;
				}
			}	
			
			board.locations[to.x][to.y] = destinationPc;
			board.locations[from.x][from.y] = piece;
			piece.position = from;
			destinationPc.position = to;
			destinationPc.alive = true;
			executed = false;
			board.playerMove = !board.playerMove;
		}else{
			System.out.println("ERROR: Trying to reverse a move that was never called.");
			System.exit(0);
		}
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
		if (other.to == null || other.piece == null || this.to == null || this.piece == null) {
			return false;
		}
		if (this.promotionMove != other.promotionMove) {
			return false;
		}
		if (this.to.equals(other.to) && this.piece.equals(other.piece)){
			return true;
		} else {
			return false;
		}
	}
}
