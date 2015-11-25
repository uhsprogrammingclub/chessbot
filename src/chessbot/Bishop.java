package chessbot;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

	static  final int WORTH = 330;
	@Override
	// findMoves() method which identifies possible moves
	// The Bishop Piece can move diagonally as far as it can
	public List<Move> findMoves(Board b) {

		List<Move> moves = new ArrayList<Move>();

		if(AIController.useBitBoards){

		}else{
			// Get Diagonal moves
			moves.addAll(Utils.getDiagonalMoves(b, this));
		}

		return moves;
	}
	
	static List<Move> getMovesFromBitboard(Board b, long bishops, boolean player){
		List<Move> moves = new ArrayList<Move>();
		while (bishops != 0){
			int from = BitBoard.bitScanForward(bishops);
			long possibleMoves = b.bitboard.bishopAttack(b.bitboard.combine(), from, player);
			
			while (possibleMoves != 0){
				int to = BitBoard.bitScanForward(possibleMoves);
				moves.add(new Move(b, new Point(from), new Point(to), null));
				possibleMoves = BitBoard.clearBit(possibleMoves, to);
			}
			bishops = BitBoard.clearBit(bishops, from);
		}
		return moves;
	}

	// Constructor
	public Bishop(int x, int y, boolean p) {

		// Setting base values for the Queen piece
		worth = WORTH;
		player = p;
		symbol = "b";

		// Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
