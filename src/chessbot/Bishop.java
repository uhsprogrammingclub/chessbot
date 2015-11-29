package chessbot;

import java.util.ArrayList;
import java.util.List;

import chessbot.Board.Side;

public class Bishop extends Piece {

	static  final int WORTH = 330;
	@Override
	// findMoves() method which identifies possible moves
	// The Bishop Piece can move diagonally as far as it can
	public List<Move> findMoves(Board b) {

		List<Move> moves = new ArrayList<Move>();

		if(AIController.useBitBoards){
			moves.addAll(getMovesFromBitboard(b, 1L << position.getIndex(), side));
		}else{
			// Get Diagonal moves
			moves.addAll(Utils.getDiagonalMoves(b, this));
		}

		return moves;
	}
	
	static List<Move> getMovesFromBitboard(Board b, long bishops, Side side){
		List<Move> moves = new ArrayList<Move>();
		while (bishops != 0){
			int from = BB.bitScanForward(bishops);

			long possibleMoves = b.bitboard.bishopAttack(b.bitboard.combine(), from, side);

			while (possibleMoves != 0){
				int to = BB.bitScanForward(possibleMoves);
				moves.add(new Move(b, new Point(from), new Point(to), null));
				possibleMoves = BB.clearBit(possibleMoves, to);
			}
			bishops = BB.clearBit(bishops, from);
		}
		return moves;
	}

	// Constructor
	public Bishop(int x, int y, Side s) {

		// Setting base values for the Queen piece
		worth = WORTH;
		side = s;
		symbol = "b";

		// Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
