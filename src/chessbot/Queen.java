package chessbot;

import java.util.*;

//Methods for the Queen Piece
public class Queen extends Piece {

	@Override
	// findMoves() method which identifies possible moves
	// The Queen Piece can move vertically, horizontally, or diagonally as far
	// as it can

	public List<Move> findMoves(Board b) {

		List<Move> moves = new ArrayList<Move>();

		if(AIController.useBitBoards){

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
	
	static List<Move> getMovesFromBitboard(Board b, long queens, boolean player){
		List<Move> moves = new ArrayList<Move>();
		while (queens != 0){
			int from = BB.bitScanForward(queens);
			
			long possibleMoves = b.bitboard.bishopAttack(from, player);
			possibleMoves |= b.bitboard.rookAttack(from, player);
			
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
	public Queen(int x, int y, boolean p) {

		// Setting base values for the Queen piece
		worth = 900;
		player = p;
		symbol = "q";

		// Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
