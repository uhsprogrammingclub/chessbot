package chessbot;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

	@Override
	// findMoves() method which identifies possible moves
	// The Rook Piece can move vertically, or horizontally as far as it can
	public List<Move> findMoves(Board b) {

		List<Move> moves = new ArrayList<Move>();
		
		if(AIController.useBitBoards){
			

		}else{

			// Gets vertical moves
			moves.addAll(Utils.getVerticalMoves(b, this));
			// Get Horizontal moves
			moves.addAll(Utils.getHorizontalMoves(b, this));
		}

		return moves;
	}

	
	static List<Move> getMovesFromBitboard(Board b, long rooks, boolean player){
		List<Move> moves = new ArrayList<Move>();
		while (rooks != 0){
			int from = BitBoard.bitScanForward(rooks);
			long possibleMoves = b.bitboard.rookAttack(from, player);
			
			while (possibleMoves != 0){
				int to = BitBoard.bitScanForward(possibleMoves);
				moves.add(new Move(b, new Point(from), new Point(to), null));
				possibleMoves = BitBoard.clearBit(possibleMoves, to);
			}
			rooks = BitBoard.clearBit(rooks, from);
		}
		return moves;
	}
	
	// Constructor
	public Rook(int x, int y, boolean p) {

		// Setting base values for the Queen piece
		worth = 500;
		player = p;
		symbol = "r";

		// Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
