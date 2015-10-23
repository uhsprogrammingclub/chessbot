package chessbot;

import java.util.*;

public class Utils {

	static List<Move> getVerticalMoves(Board b, Piece p) {
		List<Move> moves = new ArrayList<Move>();
		Point pos = p.position;

		// evaluate moves if it's a pawn
		if (p.symbol.equals("p")) {
			
			// get direction of the pawn
			int dir = p.player ? 1 : -1;
			Point move = new Point(pos.x, pos.y + dir);

			if (move.squareExists()) {
				if (b.isEmptySquare(move)) {
					if(move.y == 0 || move.y == 7){
						moves.add(new Move(b, move, p, "q"));
						moves.add(new Move(b, move, p, "n"));
						moves.add(new Move(b, move, p, "r"));
						moves.add(new Move(b, move, p, "b"));
					}else{
						moves.add(new Move(b, move, p, null));
					}
					
					// check if pawn can move 2 squares.
					if ((p.player && pos.y == 1) || (!p.player && pos.y == 6)) {
						Point move2 = new Point(pos.x, pos.y + dir * 2);
						if (move2.squareExists() && b.isEmptySquare(move2)) {
							moves.add(new Move(b, move2, p, null));
						}
					}
				}
			}
		} else {
			// forward vertical moves
			for (int y = pos.y + 1; y < 8; y++) {
				Point move = new Point(pos.x, y);
				if (b.isEmptySquare(move)) {
					moves.add(new Move(b, move, p, null));
				} else {
					if (b.getTeam(move) != p.player) {
						moves.add(new Move(b, move, p, null));
					}
					break;
				}
				if (p.symbol.equals("k"))
					break; // limit to one square if it's king
			}
			// backwards vertical moves
			for (int y = pos.y - 1; y >= 0; y--) {
				Point move = new Point(pos.x, y);
				if (b.isEmptySquare(move)) {
					moves.add(new Move(b, move, p, null));
				} else {
					if (b.getTeam(move) != p.player) {
						moves.add(new Move(b, move, p, null));
					}
					break;
				}
				if (p.symbol.equals("k"))
					break; // limit to one square if it's king
			}
		}
		return moves;
	}

	static List<Move> getHorizontalMoves(Board b, Piece p) {
		List<Move> moves = new ArrayList<Move>();
		Point pos = p.position;

		// right horizontal moves
		for (int x = pos.x + 1; x < 8; x++) {
			Point move = new Point(x, pos.y);
			if (b.isEmptySquare(move)) {
				moves.add(new Move(b, move, p, null));
			} else {
				if (b.getTeam(move) != p.player) {
					moves.add(new Move(b, move, p, null));
				}
				break;
			}
			if (p.symbol.equals("k"))
				break; // limit to one square if it's king
		}
		// left horizontal moves
		for (int x = pos.x - 1; x >= 0; x--) {
			Point move = new Point(x, pos.y);
			if (b.isEmptySquare(move)) {
				moves.add(new Move(b, move, p, null));
			} else {
				if (b.getTeam(move) != p.player) {
					moves.add(new Move(b, move, p, null));
				}
				break;
			}
			if (p.symbol.equals("k"))
				break; // limit to one square if it's king
		}
		return moves;
	}

	static List<Move> getDiagonalMoves(Board b, Piece p) {
		List<Move> moves = new ArrayList<Move>();
		Point pos = p.position;

		// evaluate moves if it's a pawn
		if (p.symbol.equals("p")) {
			// get direction of the pawn
			int dir = p.player ? 1 : -1;

			// check if spot diagonally to the right
			Point move = new Point(pos.x + 1, pos.y + dir);
			if (move.squareExists() && !b.isEmptySquare(move) && b.getTeam(move) != p.player) {
				
				if(move.y == 0 || move.y == 7){
					moves.add(new Move(b, move, p, "q"));
					moves.add(new Move(b, move, p, "b"));
					moves.add(new Move(b, move, p, "r"));
					moves.add(new Move(b, move, p, "n"));
				}else{
					moves.add(new Move(b, move, p, null));
				}	
			}

			// check if spot diagonally to the left
			Point move2 = new Point(pos.x - 1, pos.y + dir);

			if (move2.squareExists() && !b.isEmptySquare(move2) && b.getTeam(move2) != p.player) {
				if(move2.y == 0 || move2.y == 7){
					moves.add(new Move(b, move2, p, "q"));
					moves.add(new Move(b, move2, p, "n"));
					moves.add(new Move(b, move2, p, "r"));
					moves.add(new Move(b, move2, p, "b"));
				}else{
					moves.add(new Move(b, move2, p, null));
				}	
			}
		} else {
			// diagonal moves right forward
			for (int i = 1; i < 8; i++) {
				Point move = new Point(pos.x + i, pos.y + i);
				if (!move.squareExists())
					break;

				if (b.isEmptySquare(move)) {
					moves.add(new Move(b, move, p, null));
				} else {
					if (b.getTeam(move) != p.player) {
						moves.add(new Move(b, move, p, null));
					}
					break;
				}

				if (p.symbol.equals("k"))
					break; // limit to one square if it's king
			}
			// diagonal moves left forward
			for (int i = 1; i < 8; i++) {
				Point move = new Point(pos.x - i, pos.y + i);
				if (!move.squareExists())
					break;

				if (b.isEmptySquare(move)) {
					moves.add(new Move(b, move, p, null));
				} else {
					if (b.getTeam(move) != p.player) {
						moves.add(new Move(b, move, p, null));
					}
					break;
				}
				if (p.symbol.equals("k"))
					break; // limit to one square if it's king
			}
			// diagonal moves right backward
			for (int i = 1; i < 8; i++) {
				Point move = new Point(pos.x + i, pos.y - i);
				if (!move.squareExists())
					break;

				if (b.isEmptySquare(move)) {
					moves.add(new Move(b, move, p, null));
				} else {
					if (b.getTeam(move) != p.player) {
						moves.add(new Move(b, move, p, null));
					}
					break;
				}
				if (p.symbol.equals("k"))
					break; // limit to one square if it's king
			}
			// diagonal moves left backward
			for (int i = 1; i < 8; i++) {
				Point move = new Point(pos.x - i, pos.y - i);
				if (!move.squareExists())
					break;

				if (b.isEmptySquare(move)) {
					moves.add(new Move(b, move, p, null));
				} else {
					if (b.getTeam(move) != p.player) {
						moves.add(new Move(b, move, p, null));
					}
					break;
				}
				if (p.symbol.equals("k"))
					break; // limit to one square if it's king
			}
		}
		return moves;
	}
	
	static List<Move> getKnightMoves(Board b, Piece p) {
		List<Move> moves = new ArrayList<Move>();

		for (int i = -1; i <= 1; i = i + 2) {
			for (int j = -2; j <= 2; j = j + 4) {
				Point move = new Point(p.position.x + i, p.position.y + j);
				if (move.squareExists() && (b.getTeam(move) != p.player || b.isEmptySquare(move))) {
					moves.add(new Move(b, move, p, null));
				}
				Point move2 = new Point(p.position.x + j, p.position.y + i);
				if (move2.squareExists() && (b.getTeam(move2) != p.player || b.isEmptySquare(move2))) {
					moves.add(new Move(b, move2, p, null));
				}
			}
		}
		return moves;
	}
	
	static boolean isChecked(Board b, Piece king) {
		Point pos = king.position;
		boolean pl = king.player;
		
		//up the board
		for (int y = pos.y + 1; y < 8; y++) { 
			Point move = new Point(pos.x, y);
			if (!b.isEmptySquare(move)) {
				if (b.getTeam(move) != pl 
						&&(b.getPiece(move).symbol == "q" 
						|| b.getPiece(move).symbol == "r" 
						|| (b.getPiece(move).symbol == "k" && y == pos.y + 1))){
						return true; //if king queen or rook on opposing team.
				}else{
					break;
				}
			}
		}
		
		//down the board
		for (int y = pos.y - 1; y >= 0; y--) {
			Point move = new Point(pos.x, y);
			if (!b.isEmptySquare(move)) {
				if (b.getTeam(move) != pl 
						&&(b.getPiece(move).symbol == "q" 
						|| b.getPiece(move).symbol == "r" 
						|| (b.getPiece(move).symbol == "k" && y == pos.y - 1))){
						return true; //if king queen or rook on opposing team.
				}else{
					break;
				}
			}
		}
		
		// to the right
		for (int x = pos.x + 1; x < 8; x++) {
			Point move = new Point(x, pos.y);
			if (!b.isEmptySquare(move)) {
				if (b.getTeam(move) != pl 
						&&(b.getPiece(move).symbol == "q" 
						|| b.getPiece(move).symbol == "r" 
						|| (b.getPiece(move).symbol == "k" && x == pos.x + 1))){
						return true; //if king queen or rook on opposing team.
				}else{
					break;
				}
			}
		}
		
		// to the left
		for (int x = pos.x - 1; x >= 0; x--) {
			Point move = new Point(x, pos.y);
			if (!b.isEmptySquare(move)) {
				if (b.getTeam(move) != pl 
						&&(b.getPiece(move).symbol == "q" 
						|| b.getPiece(move).symbol == "r" 
						|| (b.getPiece(move).symbol == "k" && x == pos.x - 1))){
						return true; //if king queen or rook on opposing team.
				}else{
					break;
				}
			}
		}

		
		for (int i = 1; i < 8; i++) { //up the board, right
			Point move = new Point(pos.x + i, pos.y + i);
			if (!move.squareExists())
				break;

			if (!b.isEmptySquare(move)) {
				if (b.getTeam(move) != pl 
						&&(b.getPiece(move).symbol == "q" 
						|| b.getPiece(move).symbol == "b" 
						|| (b.getPiece(move).symbol == "k" && i == 1)
						|| (b.getPiece(move).symbol == "p" && i == 1 && pl))){
						return true; //if king queen or rook on opposing team.
				}else{
					break;
				}
			}
		}
		
		for (int i = 1; i < 8; i++) { //up the board, left
			Point move = new Point(pos.x - i, pos.y + i);
			if (!move.squareExists())
				break;

			if (!b.isEmptySquare(move)) {
				if (b.getTeam(move) != pl 
						&&(b.getPiece(move).symbol == "q" 
						|| b.getPiece(move).symbol == "b" 
						|| (b.getPiece(move).symbol == "k" && i == 1)
						|| (b.getPiece(move).symbol == "p" && i == 1 && pl))){
						return true; //if king queen or rook on opposing team.
				}else{
					break;
				}
			}
		}
		
		for (int i = 1; i < 8; i++) { //down the board, right
			Point move = new Point(pos.x + i, pos.y - i);
			if (!move.squareExists())
				break;

			if (!b.isEmptySquare(move)) {
				if (b.getTeam(move) != pl 
						&&(b.getPiece(move).symbol == "q" 
						|| b.getPiece(move).symbol == "b" 
						|| (b.getPiece(move).symbol == "k" && i == 1)
						|| (b.getPiece(move).symbol == "p" && i == 1 && !pl))){
						return true; //if king queen or rook on opposing team.
				}else{
					break;
				}
			}
		}
		
		for (int i = 1; i < 8; i++) { //down the board, left
			Point move = new Point(pos.x - i, pos.y - i);
			if (!move.squareExists())
				break;

			if (!b.isEmptySquare(move)) {
				if (b.getTeam(move) != pl 
						&&(b.getPiece(move).symbol == "q" 
						|| b.getPiece(move).symbol == "b" 
						|| (b.getPiece(move).symbol == "k" && i == 1)
						|| (b.getPiece(move).symbol == "p" && i == 1 && !pl))){
						return true; //if king queen or rook on opposing team.
				}else{
					break;
				}
			}
		}
		
		for (int i = -1; i <= 1; i = i + 2) {
			for (int j = -2; j <= 2; j = j + 4) {
				Point move = new Point(pos.x + i, pos.y + j);
				if (move.squareExists() && !b.isEmptySquare(move) && b.getTeam(move) != pl && b.getPiece(move).symbol == "n") {
					return true;
				}
				Point move2 = new Point(pos.x + j, pos.y + i);
				if (move2.squareExists() && !b.isEmptySquare(move2) && b.getTeam(move2) != pl && b.getPiece(move2).symbol == "n") {
					return true;
				}
			}
		}
		return false;
	}

}
