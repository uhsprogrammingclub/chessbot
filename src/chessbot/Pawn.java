package chessbot;

import java.util.*;

public class Pawn extends Piece {

	// Finds all moves for the Pawn piece
	// Looks directly in front, diagonally left, and diagonally right. Needs to
	// include En Passant still.

	public List<Move> findMoves(Board b) {

		List<Move> moves = new ArrayList<Move>();
		
		if (!AIController.useBitBoards){

			// Checks the square directly in front of it
			moves.addAll(Utils.getVerticalMoves(b, this));
	
			// Check diagonal
			moves.addAll(Utils.getDiagonalMoves(b, this));
		
		}
		// Return moves
		return moves;
	}
	
	static List<Move> getMovesFromBitboard(Board b, long pawns, boolean player){
		List<Move> moves = new ArrayList<Move>();
		while (pawns != 0){
			int from = BB.bitScanForward(pawns);
			long pawn = (long)1 << from;
			long possibleMoves = 0;
			
			long enPassantBit = 0;
			if (b.enPassantTarget != null){
				enPassantBit = (long)1 << b.enPassantTarget.getIndex();
			}
			possibleMoves |= b.bitboard.pawnsAttack(pawn, player, enPassantBit);
			possibleMoves |= b.bitboard.pawnPush(pawn, player);
			
			while (possibleMoves != 0){
				int to = BB.bitScanForward(possibleMoves);
				Point target = new Point(to);
				if(target.y == 0 || target.y == 7){
					moves.add(new Move(b, new Point(from), target, "q"));
					moves.add(new Move(b, new Point(from), target, "n"));
					moves.add(new Move(b, new Point(from), target, "r"));
					moves.add(new Move(b, new Point(from), target, "b"));
				}else{
					moves.add(new Move(b, new Point(from), target, null));
				}
				possibleMoves = BB.clearBit(possibleMoves, to);
			}
			pawns = BB.clearBit(pawns, from);
		}
		return moves;
	}
	
	

	// Constructor
	public Pawn(int x, int y, boolean p) {

		// Setting base values for the Pawn piece
		worth = 100;
		player = p;
		symbol = "p";

		// Using accessory method for clarity; not strictly necessary
		setPosition(x, y);
	}
}
