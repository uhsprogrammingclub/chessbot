package chessbot;

import java.util.ArrayList;
import java.util.List;

import chessbot.Board.Side;

public class King extends Piece {

	
	static  final int WORTH = 10000;
	
	
	@Override
	// findMoves() method which identifies possible moves
	// The King Piece can move vertically, horizontally, or diagonally 1 square
	public List<Move> findMoves(Board b) {

		List<Move> moves = new ArrayList<Move>();

		if (!AIController.useBitBoards){
			// Get castling moves
			if(b.canCastle(side, true)){
				moves.add(new Move(b, new Point(this.getX() + 2, this.getY()), this, null));
			}
			if(b.canCastle(side, false)){
				moves.add(new Move(b, new Point(this.getX() - 2, this.getY()), this, null));
			}
			// Gets vertical moves
			moves.addAll(Utils.getVerticalMoves(b, this));
			// Get Horizontal moves
			moves.addAll(Utils.getHorizontalMoves(b, this));
			// Get Diagonal moves
			moves.addAll(Utils.getDiagonalMoves(b, this));
		}else{
			moves.addAll(getMovesFromBitboard(b, 1L << position.getIndex(), side));
		}
		
		return moves;
	}
	
	static List<Move> getMovesFromBitboard(Board b, long king, Side side){
		List<Move> moves = new ArrayList<Move>();
		
		Piece kingPiece = b.getKing(side);
		if(b.canCastle(side, true)){
			moves.add(new Move(b, new Point(kingPiece.getX() + 2, kingPiece.getY()), kingPiece, null));
		}
		if(b.canCastle(side, false)){
			moves.add(new Move(b, new Point(kingPiece.getX() - 2, kingPiece.getY()), kingPiece, null));
		}
		
		int from = BB.bitScanForward(king);
		
		long possibleMoves = BB.kingAttacks[from];
		
		long friendlyBB = b.bitboard.getFriendlyBB(side);
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
	public King(int x, int y, Side s) {

		// Setting base values for the Queen piece
		worth = WORTH;
		side = s;
		symbol = "k";

		// Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
