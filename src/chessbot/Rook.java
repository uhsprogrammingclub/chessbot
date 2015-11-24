package chessbot;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

	@Override
	// findMoves() method which identifies possible moves
	// The Rook Piece can move vertically, or horizontally as far as it can
	public List<Move> findMoves(Board b) {

		List<Move> moves = new ArrayList<Move>();
		
		if(b.useBitBoards){
			long friendlyBB;
			if(this.player){
				friendlyBB = b.bitboard.pieceBitBoards[0];
			}else{
				friendlyBB = b.bitboard.pieceBitBoards[1];
			}
			long bbAllPieces = b.bitboard.combine(); 
			long bbBlockers = bbAllPieces & MagicBitboards.occupancyMaskRook[position.getIndex()];
			int databaseIndex = (int)(bbBlockers * MagicBitboards.magicNumberRook[position.getIndex()] >>> MagicBitboards.magicNumberShiftsRook[position.getIndex()]);
			long possibleMoves = MagicBitboards.magicMovesRook[position.getIndex()][databaseIndex] & ~friendlyBB;

			while (possibleMoves != 0){
				int index = BitBoard.bitScanForward(possibleMoves);
				Point target = new Point(index);
				moves.add(new Move(b, target, this, null));
				possibleMoves = BitBoard.clearBit(possibleMoves, index);
			}

		}else{

			// Gets vertical moves
			moves.addAll(Utils.getVerticalMoves(b, this));
			// Get Horizontal moves
			moves.addAll(Utils.getHorizontalMoves(b, this));
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
