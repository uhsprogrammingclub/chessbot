package chessbot;

import java.util.*;

public class Knight extends Piece {
	
	static  final int WORTH = 320;
	
	
	@Override
	// findMoves() method which identifies possible moves
	// The Knight Piece can move ...
	public List<Move> findMoves(Board b) {

		List<Move> moves = new ArrayList<Move>();
		
		if (!AIController.useBitBoards){
			moves.addAll(Utils.getKnightMoves(b, this));
		}else{
			moves.addAll(getMovesFromBitboard(b, 1L << position.getIndex(), player));
		}

		return moves;
	}
	
	static List<Move> getMovesFromBitboard(Board b, long knights, boolean player){
		List<Move> moves = new ArrayList<Move>();
		
		while (knights != 0){
			int from = BB.bitScanForward(knights);
					
			long possibleMoves = BB.knightAttacks[from];
			
			long friendlyBB = b.bitboard.getFriendlyBB(player);
			possibleMoves &= ~friendlyBB;
			
			while (possibleMoves != 0){
				int to = BB.bitScanForward(possibleMoves);
				Point target = new Point(to);
				moves.add(new Move(b, new Point(from), target, null));
				possibleMoves = BB.clearBit(possibleMoves, to);
			}
			knights = BB.clearBit(knights, from);
		}
		return moves;
	}

	// Constructor
	public Knight(int x, int y, boolean p) {

		// Setting base values for the Queen piece
		worth = 320;
		player = p;
		symbol = "n";

		// Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
