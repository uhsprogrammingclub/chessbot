package chessbot;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
	@Override
	// findMoves() method which identifies possible moves
	// The Knight Piece can move ...
	public List<Move> findMoves(Board b) {

		List<Move> moves = new ArrayList<Move>();

		for (int i = -1; i <= 1; i = i + 2) {
			for (int j = -2; j <= 2; j = j + 4) {
				Point move = new Point(this.position.x + i, this.position.y + j);
				if (move.squareExists() && b.getTeam(move) != this.player) {
					moves.add(new Move(b, move, this));
				}
				Point move2 = new Point(this.position.x + j, this.position.y + i);
				if (move2.squareExists() && b.getTeam(move2) != this.player) {
					moves.add(new Move(b, move2, this));
				}
			}
		}
		return moves;
	}

	// Constructor
	public Knight(int x, int y, boolean p) {

		// Setting base values for the Queen piece
		worth = 3;
		player = p;
		symbol = "n";

		// Using accessory method for clarity; not strictly necessary
		position.setXY(x, y);
	}
}
