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
			int from = BitBoard.bitScanForward(queens);
			
			long friendlyBB;
			if(player){
				friendlyBB = b.bitboard.pieceBitBoards[0];
			}else{
				friendlyBB = b.bitboard.pieceBitBoards[1];
			}
			long bbAllPieces = b.bitboard.combine(); 
			
			long bbStraightBlockers = bbAllPieces & MagicBitboards.occupancyMaskRook[from];
			int rookDatabaseIndex = (int)(bbStraightBlockers * MagicBitboards.magicNumberRook[from] >>> MagicBitboards.magicNumberShiftsRook[from]);
			long possibleMoves = MagicBitboards.magicMovesRook[from][rookDatabaseIndex];
			
			long bbDiagonalBlockers = bbAllPieces & MagicBitboards.occupancyMaskBishop[from];
			int bishopDatabaseIndex = (int)(bbDiagonalBlockers * MagicBitboards.magicNumberBishop[from] >>> MagicBitboards.magicNumberShiftsBishop[from]);
			possibleMoves |= MagicBitboards.magicMovesBishop[from][bishopDatabaseIndex];

			possibleMoves &= ~friendlyBB;
			
			while (possibleMoves != 0){
				int to = BitBoard.bitScanForward(possibleMoves);
				moves.add(new Move(b, new Point(from), new Point(to), null));
				possibleMoves = BitBoard.clearBit(possibleMoves, to);
			}
			queens = BitBoard.clearBit(queens, from);
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
