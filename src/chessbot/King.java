package chessbot;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

	
	static  final int WORTH = 10000;
	
	
	@Override
	// findMoves() method which identifies possible moves
	// The King Piece can move vertically, horizontally, or diagonally 1 square
	public List<Move> findMoves(Board b) {

		List<Move> moves = new ArrayList<Move>();

		if (!AIController.useBitBoards){
			// Get castling moves
			if(b.canCastle(player, true)){
				moves.add(new Move(b, new Point(this.getX() + 2, this.getY()), this, null));
			}
			if(b.canCastle(player, false)){
				moves.add(new Move(b, new Point(this.getX() - 2, this.getY()), this, null));
			}
			// Gets vertical moves
			moves.addAll(Utils.getVerticalMoves(b, this));
			// Get Horizontal moves
			moves.addAll(Utils.getHorizontalMoves(b, this));
			// Get Diagonal moves
			moves.addAll(Utils.getDiagonalMoves(b, this));
		}else{
			moves.addAll(getMovesFromBitboard(b, 1L << position.getIndex(), player));
		}
		
		return moves;
	}
	
	static List<Move> getMovesFromBitboard(Board b, long king, boolean player){
		List<Move> moves = new ArrayList<Move>();
		
		Piece kingPiece = b.getKing(player);
		if(b.canCastle(player, true)){
			moves.add(new Move(b, new Point(kingPiece.getX() + 2, kingPiece.getY()), kingPiece, null));
		}
		if(b.canCastle(player, false)){
			moves.add(new Move(b, new Point(kingPiece.getX() - 2, kingPiece.getY()), kingPiece, null));
		}
		
		int from = BB.bitScanForward(king);
		
		long possibleMoves = BB.kingAttacks[from];
		
		long friendlyBB = b.bitboard.getFriendlyBB(player);
		possibleMoves &= ~friendlyBB;
		
		while (possibleMoves != 0){
			int to = BB.bitScanForward(possibleMoves);
			Point target = new Point(to);
			moves.add(new Move(b, new Point(from), target, null));
			possibleMoves = BB.clearBit(possibleMoves, to);
		}
		return moves;
	}

	// Constructor
	public King(int x, int y, boolean p) {

		// Setting base values for the Queen piece
		worth = WORTH;
		player = p;
		symbol = "k";

		// Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
