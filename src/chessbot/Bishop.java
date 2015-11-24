package chessbot;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

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
			
			long friendlyBB;
			if(player){
				friendlyBB = b.bitboard.pieceBitBoards[0];
			}else{
				friendlyBB = b.bitboard.pieceBitBoards[1];
			}
			long bbAllPieces = b.bitboard.combine(); 
			long bbBlockers = bbAllPieces & MagicBitboards.occupancyMaskBishop[from];
			int databaseIndex = (int)(bbBlockers * MagicBitboards.magicNumberBishop[from] >>> MagicBitboards.magicNumberShiftsBishop[from]);
			long possibleMoves = MagicBitboards.magicMovesBishop[from][databaseIndex];
			possibleMoves &= ~friendlyBB;
			
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
		worth = 330;
		player = p;
		symbol = "b";

		// Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
