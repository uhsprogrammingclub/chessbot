package chessbot;

import java.util.*;

public class Pawn extends Piece {

	// Finds all moves for the Pawn piece
	// Looks directly in front, diagonally left, and diagonally right. Needs to
	// include En Passant still.

	public List<Move> findMoves(Board b) {

		List<Move> moves = new ArrayList<Move>();
		
		if (!b.useBitBoards){

			// Checks the square directly in front of it
			moves.addAll(Utils.getVerticalMoves(b, this));
	
			// Check diagonal
			moves.addAll(Utils.getDiagonalMoves(b, this));
		
		}else{
			long pawn = (long)1 << position.getIndex();
			
			long possibleMoves = 0;
			
			long enPassantBit = 0;
			if (b.enPassantTarget != null){
				enPassantBit = (long)1 << b.enPassantTarget.getIndex();
			}
			possibleMoves |= b.bitboard.pawnsAbleToAttack(pawn, player, enPassantBit);
			possibleMoves |= b.bitboard.pawnsAbleToDoublePush(pawn, player);
			possibleMoves |= b.bitboard.pawnsAbleToPush(pawn, player);
			
			while (possibleMoves != 0){
				int index = BitBoard.bitScanForward(possibleMoves);
				Point target = new Point(index);
				if(target.y == 0 || target.y == 7){
					moves.add(new Move(b, target, this, "q"));
					moves.add(new Move(b, target, this, "n"));
					moves.add(new Move(b, target, this, "r"));
					moves.add(new Move(b, target, this, "b"));
				}else{
					moves.add(new Move(b, target, this, null));
				}
				possibleMoves = BitBoard.clearBit(possibleMoves, index);
			}
		}
		// Return moves
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
