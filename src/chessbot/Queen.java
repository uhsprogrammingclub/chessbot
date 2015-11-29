package chessbot;

import java.util.*;

import chessbot.Board.Side;

//Methods for the Queen Piece
public class Queen extends Piece {
	
	static  final int WORTH = 900;

	@Override
	// findMoves() method which identifies possible moves
	// The Queen Piece can move vertically, horizontally, or diagonally as far
	// as it can

	public List<Move> findMoves(Board b) {

		List<Move> moves = new ArrayList<Move>();

		if(AIController.useBitBoards){
			moves.addAll(getMovesFromBitboard(b, 1L << position.getIndex(), side));
		}else{
			// Gets vertical moves
			moves.addAll(Utils.getVerticalMoves(b, this));
			// Get Horizontal moves
			moves.addAll(Utils.getHorizontalMoves(b, this));
			// Get Diagonal moves
			moves.addAll(Utils.getDiagonalMoves(b, this));
		}

		return moves;
	}
	
	static List<Move> getMovesFromBitboard(Board b, long queens, Side side){
		List<Move> moves = new ArrayList<Move>();
		while (queens != 0){
			int from = BB.bitScanForward(queens);
			
			long possibleMoves = b.bitboard.bishopAttack(b.bitboard.combine(), from, side);
			possibleMoves |= b.bitboard.rookAttack(b.bitboard.combine(), from, side);
			
			while (possibleMoves != 0){
				int to = BB.bitScanForward(possibleMoves);
				moves.add(new Move(b, new Point(from), new Point(to), null));
				possibleMoves = BB.clearBit(possibleMoves, to);
			}
			queens = BB.clearBit(queens, from);
		}
		return moves;
	}

	// Constructor
	public Queen(int x, int y, Side s) {

		// Setting base values for the Queen piece
		worth = WORTH;
		side = s;
		symbol = "q";

		// Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
