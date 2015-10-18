package chessbot;
import java.util.*;

public class Utils {

	static List<Move> getVerticalMoves(Board b, Piece p){
		List<Move> moves = new ArrayList<Move>();
		Point pos = p.position;
		
		//evaluate moves if it's a pawn
		if (p.symbol.equals("p")){
			//get direction of the pawn

			int dir = p.player ? -1: 1;
			Point move = new Point(pos.x, pos.y + dir);

			if( move.squareExists()){
				if (b.isEmptySquare(move)){
					moves.add(new Move(move, p));
					//check if pawn can move 2 squares.
					if ((p.player && pos.y == 6) || (p.player && pos.y == 1)){
						Point move2 = new Point(pos.x, pos.y + dir*2);
						if( move2.squareExists() && b.isEmptySquare(move2)){
							moves.add(new Move(move2, p));
						}
					}
				}
			}
		}else{
			//forward vertical moves
			for (int y = pos.y+1; y < 8; y++){
				Point move = new Point( pos.x, y);
				if (b.isEmptySquare(move)){
					moves.add(new Move(move, p));
				}else{
					if (b.getTeam(move) != p.player){
						moves.add(new Move(move, p));
					}
					break;
				}
				if (p.symbol.equals("k")) break; //limit to one square if it's king
			}
			//backwards vertical moves
			for (int y = pos.y-1; y >= 0; y--){
				Point move = new Point( pos.x, y);
				if (b.isEmptySquare(move)){
					moves.add(new Move(move, p));
				}else{
					if (b.getTeam(move) != p.player){
						moves.add(new Move(move, p));
					}
					break;
				}
				if (p.symbol.equals("k")) break; //limit to one square if it's king
			}
		}
		return moves;
	}
	
	
	static List<Move> getHorizontalMoves(Board b, Piece p){
		List<Move> moves = new ArrayList<Move>();
		Point pos = p.position;
		
		//right horizontal moves
		for (int x = pos.x+1; x < 8; x++){
			Point move = new Point( x, pos.y);
			if (b.isEmptySquare(move)){
				moves.add(new Move(move, p));
			}else{
				if (b.getTeam(move) != p.player){
					moves.add(new Move(move, p));
				}
				break;
			}
			if (p.symbol.equals("k")) break; //limit to one square if it's king
		}
		// left horizontal moves
		for (int x = pos.x-1; x >= 0; x--){
			Point move = new Point( x, pos.y);
			if (b.isEmptySquare(move)){
				moves.add(new Move(move, p));
			}else{
				if (b.getTeam(move) != p.player){
					moves.add(new Move(move, p));
				}
				break;
			}
			if (p.symbol.equals("k")) break; //limit to one square if it's king
		}
		return moves;
	}

	static List<Move> getDiagonalMoves(Board b, Piece p){
		List<Move> moves = new ArrayList<Move>();
		Point pos = p.position;
		
		//evaluate moves if it's a pawn
		if (p.symbol.equals("p")){
			//get direction of the pawn
			int dir = p.player ? -1: 1;
			
			//check if spot diagonally to the right
			Point move = new Point(pos.x + 1, pos.y + dir);
			if( move.squareExists() && !b.isEmptySquare(move) && b.getTeam(move) != p.player){
				moves.add(new Move(move, p));
			}
			
			//check if spot diagonally to the left
			Point move2 = new Point(pos.x -1, pos.y + dir);
			if( move2.squareExists() && !b.isEmptySquare(move2) && b.getTeam(move) != p.player){
				moves.add(new Move(move, p));
			}
		}else{
			//diagonal moves right forward
			for (int i = 1; i < 8; i++){
				Point move = new Point( pos.x+i, pos.y+i);
				if (!move.squareExists()) break;
				
				if (b.isEmptySquare(move)){
					moves.add(new Move(move, p));
				}else{
					if (b.getTeam(move) != p.player){
						moves.add(new Move(move, p));
					}
					break;
				}
				
				if (p.symbol.equals("k")) break; //limit to one square if it's king
			}
			//diagonal moves left forward
			for (int i = 1; i < 8; i++){
				Point move = new Point( pos.x-i, pos.y+i);
				if (!move.squareExists()) break;
				
				if (b.isEmptySquare(move)){
					moves.add(new Move(move, p));
				}else{
					if (b.getTeam(move) != p.player){
						moves.add(new Move(move, p));
					}
					break;
				}
				if (p.symbol.equals("k")) break; //limit to one square if it's king
			}
			//diagonal moves right backward
			for (int i = 1; i < 8; i++){
				Point move = new Point( pos.x+i, pos.y-i);
				if (!move.squareExists()) break;
				
				if (b.isEmptySquare(move)){
					moves.add(new Move(move, p));
				}else{
					if (b.getTeam(move) != p.player){
						moves.add(new Move(move, p));
					}
					break;
				}
				if (p.symbol.equals("k")) break; //limit to one square if it's king
			}
			//diagonal moves left backward
			for (int i = 1; i < 8; i++){
				Point move = new Point( pos.x-i, pos.y-i);
				if (!move.squareExists()) break;
				
				if (b.isEmptySquare(move)){
					moves.add(new Move(move, p));
				}else{
					if (b.getTeam(move) != p.player){
						moves.add(new Move(move, p));
					}
					break;
				}
				if (p.symbol.equals("k")) break; //limit to one square if it's king
			}
		}
		return moves;
	}

}
