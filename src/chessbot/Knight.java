package chessbot;

import java.util.*;

public class Knight extends Piece {
	@Override
	// findMoves() method which identifies possible moves
	// The Knight Piece can move ...
	public List<Move> findMoves(Board b) {

		List<Move> moves = new ArrayList<Move>();
		
		if (!AIController.useBitBoards){
			moves.addAll(Utils.getKnightMoves(b, this));
		}

		return moves;
	}
	
	static List<Move> getMovesFromBitboard(Board b, long knights, boolean player){
		List<Move> moves = new ArrayList<Move>();
		
		while (knights != 0){
			int from = BitBoard.bitScanForward(knights);
					
			long possibleMoves = BitBoard.knightAttacks[from];
			
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
			knights = BitBoard.clearBit(knights, from);
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
