package chessbot;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

	@Override
	// findMoves() method which identifies possible moves
	// The Bishop Piece can move diagonally as far as it can
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
			long bbBlockers = bbAllPieces & MagicBitboards.occupancyMaskBishop[position.getIndex()];
			int databaseIndex = (int)(bbBlockers * MagicBitboards.magicNumberBishop[position.getIndex()] >>> MagicBitboards.magicNumberBishop[position.getIndex()]);
			long possibleMoves = MagicBitboards.magicMovesBishop[position.getIndex()][databaseIndex] & ~friendlyBB;

			while (possibleMoves != 0){
				int index = BitBoard.bitScanForward(possibleMoves);
				Point target = new Point(index);
				moves.add(new Move(b, target, this, null));
				possibleMoves = BitBoard.clearBit(possibleMoves, index);
			}

		}else{
			// Get Diagonal moves
			moves.addAll(Utils.getDiagonalMoves(b, this));
		}

		return moves;
	}

	// Constructor
	public Bishop(int x, int y, boolean p) {

		// Setting base values for the Queen piece
		worth = 330;
		player = p;
		symbol = "b";

		// Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
