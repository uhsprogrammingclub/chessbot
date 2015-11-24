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

		if(Board.useBitBoards){
			long friendlyBB;
			if(this.player){
				friendlyBB = b.bitboard.pieceBitBoards[0];
			}else{
				friendlyBB = b.bitboard.pieceBitBoards[1];
			}
			long bbAllPieces = b.bitboard.combine(); 
			long bbStraightBlockers = bbAllPieces & MagicBitboards.occupancyMaskRook[position.getIndex()];
			int rookDatabaseIndex = (int)(bbStraightBlockers * MagicBitboards.magicNumberRook[position.getIndex()] >>> MagicBitboards.magicNumberShiftsRook[position.getIndex()]);
			long possibleMoves = MagicBitboards.magicMovesRook[position.getIndex()][rookDatabaseIndex];
			
			long bbDiagonalBlockers = bbAllPieces & MagicBitboards.occupancyMaskBishop[position.getIndex()];
			int bishopDatabaseIndex = (int)(bbDiagonalBlockers * MagicBitboards.magicNumberBishop[position.getIndex()] >>> MagicBitboards.magicNumberShiftsBishop[position.getIndex()]);
			possibleMoves |= MagicBitboards.magicMovesBishop[position.getIndex()][bishopDatabaseIndex];

			possibleMoves &= ~friendlyBB;
			
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
			// Get Diagonal moves
			moves.addAll(Utils.getDiagonalMoves(b, this));
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
