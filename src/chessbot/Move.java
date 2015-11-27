package chessbot;

import java.util.List;

import chessbot.Board.CastleState;

public class Move implements Comparable<Move>{

	Point from;
	Point to;
	Piece piece = null;
	Piece destinationPc = null;
	boolean executed = false;
	Board board;
	Piece promotionPiece = null;
	boolean promotionMove = false;
	CastleState playerCastleO = null;
	CastleState computerCastleO = null;
	
	boolean enPassantMove = false;
	Piece enPassantCapture = null;
	
	boolean playerKSideCastleO = false;
	boolean playerQSideCastleO = false;
	boolean botKSideCastleO = false;
	boolean botQSideCastleO = false;
	Point enPassantTargetO = null;
	
	boolean castleMove = false;
	Move castleRookMove = null;
	
	public Move(Board b, Move m){
		board = b;
		from = m.from;
		to = m.to;
		
		if (m.executed){
			System.out.println("ERROR: Copying an executed move.");
			System.exit(0);
		}
		
		playerCastleO = b.playerCastle;
		computerCastleO = b.computerCastle;
		
		playerKSideCastleO = board.playerKSideCastle;
	    playerQSideCastleO = board.playerQSideCastle;
	    botKSideCastleO = board.botKSideCastle;
		botQSideCastleO = board.botQSideCastle;
		enPassantTargetO = board.enPassantTarget;
		
		promotionMove = m.promotionMove;
		castleMove = m.castleMove;
		enPassantMove = m.enPassantMove;
		enPassantCapture = m.enPassantCapture;
		
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
	
	private void makeMove(String promotionPiece){
		destinationPc = board.getPiece(to);
		playerKSideCastleO = board.playerKSideCastle;
	    playerQSideCastleO = board.playerQSideCastle;
	    botKSideCastleO = board.botKSideCastle;
		botQSideCastleO = board.botQSideCastle;
		enPassantTargetO = board.enPassantTarget;
		
		if(promotionPiece != null && !promotionPiece.equals("")){
			promotionMove = true;
			if(promotionPiece.equals("q")){
				this.promotionPiece = new Queen(to.x, to.y, piece.player);
			}
			else if(promotionPiece.equals("n")){
				this.promotionPiece = new Knight(to.x, to.y, piece.player);
			}
			else if(promotionPiece.equals("r")){
				this.promotionPiece = new Rook(to.x, to.y, piece.player);
			}
			else if(promotionPiece.equals("b")){
				this.promotionPiece = new Bishop(to.x, to.y, piece.player);
			}
			else{
				promotionMove = false;
				System.out.println("ERROR: invlaid promotion piece");
			}
		}
		if (promotionMove){
			this.promotionPiece.alive = false;
		}
		
		if (piece.symbol == "k" && Math.abs(to.x - from.x) == 2){
			castleMove = true;
		}
		
		if (piece.symbol == "p" && to.x != from.x && board.isEmptySquare(to) ){
			enPassantMove = true;
			enPassantCapture = board.getPiece(new Point(to.x, from.y));
		}
	}

	public Move(Board b, Point pt, Piece pc, String promotionPiece) {
		
		board = b;
		piece = pc;
		from = new Point(pc.position.x, pc.position.y);
		to = pt;
		
		makeMove(promotionPiece);
	}

	public Move(Board b, Point from, Point to, String promotionPiece) {
		
		board = b;
		this.from = from;
		this.to = to;
		piece = b.getPiece(from);
		
		makeMove(promotionPiece);
	}
	
	public Move(Board b, String SAN){
		board = b;
		
		String originalSAN = SAN;
		String promotionPiece = "";
		String toString = "";
		char fromRank = 0;
		char fromFile = 0;
		String pieceString = "";
		
		if (SAN.contains("O-O-O")){
			piece = board.getKing(board.playerMove);
			to = new Point(piece.getX() - 2, piece.getY());
			from = new Point(piece.position.x, piece.position.y);
			makeMove(promotionPiece);
			return;
		}else if (SAN.contains("O-O")){
			piece = board.getKing(board.playerMove);
			to = new Point(piece.getX() + 2, piece.getY());
			from = new Point(piece.position.x, piece.position.y);
			makeMove(promotionPiece);
			return;
		}
		
		if (SAN.contains("=")){
			int promotionIndex = SAN.indexOf('=') + 1;
			promotionPiece = SAN.substring(promotionIndex, promotionIndex+1).toLowerCase();
		}
		
		while (true){
			char lastChar = SAN.charAt(SAN.length()-1);
			if (!Character.isDigit(lastChar)){
				SAN = SAN.substring(0, SAN.length()-1);
			}else{
				break;
			}
		}
		
		toString = SAN.substring(SAN.length()-2, SAN.length());
		to = new Point(toString);
		SAN = SAN.substring(0, SAN.length()-2);
		
		if (SAN.length() != 0 && SAN.charAt(SAN.length()-1) == 'x'){
			SAN = SAN.substring(0, SAN.length()-1);
		}
		
		//pawn forwards
		if (SAN.length() == 0){
			int dir = board.playerMove ? 1 : -1;
			Piece p = board.getPiece(new Point(to.x, to.y-dir));
			if (p.symbol.equals("p")){
				piece = p;
				from = new Point(piece.position.x, piece.position.y);
				makeMove(promotionPiece);
				return;
			}
			p = board.getPiece(new Point(to.x, to.y-2*dir));
			if (p.symbol.equals("p")){
				piece = p;
				from = new Point(piece.position.x, piece.position.y);
				makeMove(promotionPiece);
				return;
			}
			System.out.println("SAN conversion failed pawn move: " + originalSAN + b);
		}
		
		if (SAN.length() == 1 && Character.isLowerCase(SAN.charAt(0))){
			int dir = board.playerMove ? 1 : -1;
			Piece p = board.getPiece(new Point(SAN+(to.y-dir+1)));
			if (p.symbol.equals("p") && p.player == b.playerMove){
				piece = p;
				from = new Point(piece.position.x, piece.position.y);
				makeMove(promotionPiece);
				return;
			}
			System.out.println("SAN conversion failed: pawn capture: " + originalSAN + b);
		}
		
		pieceString = SAN.substring(0, 1).toLowerCase();
		if (SAN.length() == 2){
			if (Character.isDigit(SAN.charAt(1))){
				fromRank = SAN.charAt(1);
			}else{
				fromFile = SAN.charAt(1);
			}
			
		}
		
		for(Piece p : board.pieceList){
			if (p.alive && p.player == board.playerMove && p.symbol.equals(pieceString) && (fromFile == 0 || ((int)fromFile - 97) == p.getX())  && (fromRank == 0 || ((int)fromRank - 49) == p.getY())){
				List<Move> potentialMoves = p.findMoves(b);
				for(Move m : potentialMoves){
					if (m.to.equals(to) && b.isLegal(m)){
						piece = p;
						from = new Point(piece.position.x, piece.position.y);
						makeMove(promotionPiece);
						return;
					}
				}
			}
		}
		System.out.println("SAN conversion failed final: " + originalSAN + b);
		
	}

	void execute() {
		if (!executed) {
			board.history[board.halfMoves].zobrist = Zobrist.getZobristHash(board);
			board.history[board.halfMoves].fiftyMove = board.fiftyMove;
			board.history[board.halfMoves].move = this; // Can eventually replace moveHistory
			
			board.moveHistory.add(this.toString());
			
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
				board.fiftyMove = 0;
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
			
			if(enPassantMove){
				enPassantCapture.alive = false;
				board.setSquare(enPassantCapture.position, new Empty());
			}
		
			//Change the castling variables depending on the piece being moved
			if(piece.symbol.equals("k")){
				
				if(!castleMove){
					if(piece.player) board.playerCastle = CastleState.CASTLE_RUINED; else board.computerCastle = CastleState.CASTLE_RUINED; 
				}
				
				if(piece.player){
					board.playerKSideCastle = board.playerQSideCastle = false;
				}else{
					board.botKSideCastle = board.botQSideCastle = false;
				}
			}
			
			board.enPassantTarget = null;
			if(piece.symbol.equals("r")){
				
				if (from.x == 0){
					if(piece.player){
						if(!board.playerKSideCastle){
							board.playerCastle = CastleState.CASTLE_RUINED;
						}
						board.playerQSideCastle = false;
					}else{
						if(!board.botKSideCastle){
							board.computerCastle = CastleState.CASTLE_RUINED;
						}
						board.botQSideCastle = false;
					}
				}else if (from.x == 7){
					if(piece.player){
						if(!board.playerQSideCastle){
							board.playerCastle = CastleState.CASTLE_RUINED;
						}
						board.playerKSideCastle = false;
					}else{
						if(!board.botQSideCastle){
							board.computerCastle = CastleState.CASTLE_RUINED;
						}
						board.botKSideCastle = false;
					}
				}
			}else if(piece.symbol.equals("p")){
				
				board.fiftyMove = 0;
				
				if (from.y == 1 && to.y == 3){
					board.enPassantTarget = new Point(to.x, 2);
				}else if (from.y == 6 && to.y == 4){
					board.enPassantTarget = new Point(to.x, 5);
				}
			}
			
			//If a rook is being taken, alter the castling variables appropriately
			if(destinationPc.symbol.equals("r")){
				
				//If it belongs to the player...
				if(destinationPc.player){
					if (destinationPc.position.y == 0){
						if(destinationPc.position.x == 0) board.playerQSideCastle = false;
						if(destinationPc.position.x == 7) board.playerKSideCastle = false;
					}
					
				}else{
					if (destinationPc.position.y == 7){
						if(destinationPc.position.x == 0) board.botQSideCastle = false;
						if(destinationPc.position.x == 7) board.botKSideCastle = false;
					}
					
				}
				
			}
			
			board.fiftyMove++;
			board.halfMoves++;
			
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
			
			board.halfMoves--;
			board.fiftyMove = board.history[board.halfMoves].fiftyMove;
			
			
			board.moveHistory.remove(board.moveHistory.size()-1);
			
			if(promotionMove){
				board.pieceList.remove(promotionPiece);
				promotionPiece.alive = false;
				piece.alive = true;
				
			}
			if(enPassantMove){
				enPassantCapture.alive = true;
				board.setSquare(enPassantCapture.position, enPassantCapture);
			}
			
			board.playerKSideCastle = playerKSideCastleO;
			board.playerQSideCastle = playerQSideCastleO;
			board.botKSideCastle = botKSideCastleO;
			board.botQSideCastle = botQSideCastleO;	
			board.enPassantTarget = enPassantTargetO;
			
			board.computerCastle = computerCastleO;
			board.playerCastle = playerCastleO;
			
			board.setSquare(to,  destinationPc);
			board.setSquare(from, piece);
			destinationPc.alive = true;
			executed = false;
			
			//Check if it is a castling move...
			if(castleMove){
				castleRookMove.reverse();
				board.playerMove = !board.playerMove;
			}
			
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
	
	boolean isLoud(){
		if(promotionMove || isCapture()){
			return true;
		}
		return false;
	}
	
	boolean isCheck(){
		boolean result = false;
		execute();	
		if (board.isCheck(board.playerMove)) {
			result = true;
		} 
		reverse();
		return result;
	}
	
	String getSAN() {
		String s = "";
		if (piece.symbol != "p"){
			s += piece.toString().toUpperCase();
		}
		if (isCapture() || enPassantMove){
			if(piece.symbol == "p") s += String.valueOf((char)(piece.getX() + 97));
			s += "x";
		}
		s += to.toString().toLowerCase();
		
		if (promotionMove){
			s += promotionPiece.toString().toUpperCase();
		}
		
		if (castleMove){
			if (to.x == 6){
				s += "O-O";
			}else if (to.x == 2){
				s += "O-O-O";
			}else{
				System.out.println("ERROR: Invalid castle move destination.");
			}
		}
		if (enPassantMove){
			s += "e.p.";
		}
		if (isCheck()){
			s += "+";
		}
		return s;
	}

	@Override
	public String toString() {
		String s = "";
		s += piece + "";
		s += from + "->";
		if (isCapture()){
			s += "x" + destinationPc;
		}
		if (enPassantMove){
			s += "x";
		}
		s += this.to;
		if (promotionMove){
			s += "=" + promotionPiece;
		}
		if (castleMove){
			if (to.x == 6){
				s += " O-O";
			}else if (to.x == 2){
				s += " O-O-O";
			}else{
				System.out.println("ERROR: Invalid castle move destination.");
			}
		}
		if (enPassantMove){
			s += "e.p.";
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
	
	@Override
	public int compareTo(Move other) {
		if (this.isCapture()){
			if (!other.isCapture()){
				return -1;
			}else{
				int thisValueDiff = this.destinationPc.worth - this.piece.worth;
				int otherValueDiff = other.destinationPc.worth - other.piece.worth;
				if (this.promotionMove){
					thisValueDiff = this.destinationPc.worth - this.promotionPiece.worth;
				}
				if (other.promotionMove){
					otherValueDiff = other.destinationPc.worth - other.promotionPiece.worth;
				}
				if (thisValueDiff > otherValueDiff){
					return -1;
				}else if (thisValueDiff < otherValueDiff){
					return 1;
				}
			}	
		}else{
			if (other.isCapture()){
				return 1;
			}
		}
		
		//Middle out circle pattern
		double thisDistFromCenter = Math.sqrt((this.to.x-3.5)*(this.to.x-3.5) + (this.to.y-3.5)*(this.to.y-3.5));
		double otherDistFromCenter = Math.sqrt((other.to.x-3.5)*(other.to.x-3.5) + (other.to.y-3.5)*(other.to.y-3.5));
		
		if (thisDistFromCenter > otherDistFromCenter){
			return 1;
		}else if (thisDistFromCenter < otherDistFromCenter){
			return -1;
		}
		
		return 0;
    }
}
