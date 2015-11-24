package chessbot;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

	@Override
	// findMoves() method which identifies possible moves
	// The King Piece can move vertically, horizontally, or diagonally 1 square
	public List<Move> findMoves(Board b) {

		List<Move> moves = new ArrayList<Move>();

		if (!AIController.useBitBoards){
			// Get castling moves
			if(b.canCastleKSide(this) && !b.isCheck(player)){
				moves.add(new Move(b, new Point(this.getX() + 2, this.getY()), this, null));
			}
			if(b.canCastleQSide(this) && !b.isCheck(player)){
				moves.add(new Move(b, new Point(this.getX() - 2, this.getY()), this, null));
			}
			// Gets vertical moves
			moves.addAll(Utils.getVerticalMoves(b, this));
			// Get Horizontal moves
			moves.addAll(Utils.getHorizontalMoves(b, this));
			// Get Diagonal moves
			moves.addAll(Utils.getDiagonalMoves(b, this));
		}
		
		return moves;
	}
	
	static List<Move> getMovesFromBitboard(Board b, long king, boolean player){
		List<Move> moves = new ArrayList<Move>();
		
		Piece kingPiece = b.getKing(player);
		if(b.canCastleKSide(kingPiece)){
			moves.add(new Move(b, new Point(kingPiece.getX() + 2, kingPiece.getY()), kingPiece, null));
		}
		if(b.canCastleQSide(kingPiece)){
			moves.add(new Move(b, new Point(kingPiece.getX() - 2, kingPiece.getY()), kingPiece, null));
		}
		
		int from = BitBoard.bitScanForward(king);
		
		long possibleMoves = BitBoard.kingAttacks[from];
		
		long friendlyBB;
		if(player){
			friendlyBB = b.bitboard.pieceBitBoards[0];
		}else{
			friendlyBB = b.bitboard.pieceBitBoards[1];
		}
		possibleMoves &= ~friendlyBB;
		
		while (possibleMoves != 0){
			int to = BitBoard.bitScanForward(possibleMoves);
			Point target = new Point(to);
			moves.add(new Move(b, new Point(from), target, null));
			possibleMoves = BitBoard.clearBit(possibleMoves, to);
		}
		return moves;
	}

	// Constructor
	public King(int x, int y, boolean p) {

		// Setting base values for the Queen piece
		worth = 10000;
		player = p;
		symbol = "k";

		// Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
