package chessbot;
import java.util.*;

public class Utils {

	static List<Move> getVerticalMoves(Board b, Piece p){
		List<Move> moves = new ArrayList<Move>();
		Point pos = p.position;
		
		//evaluate moves if it's a pawn
		if (p.symbol.equals("p")){
			//get direction of the pawn
			int dir = p.isPlayer? 1: -1;
			Point move = new Point(pos.x, pos.y + dir);	
			if( move.squareExists()){
				if (b.isEmptySquare(move)){
					moves.add(new Move(move, p));
					//check if pawn can move 2 squares.
					if ((p.isPlayer && pos.y == 6) || (p.isPlayer && pos.y == 1)){
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
					if (b.getTeam(move) != p.isPlayer){
						moves.add(new Move(move, p));
					}
					break;
				}
			}
			for (int y = pos.y-1; y >= 0; y--){
				Point move = new Point( pos.x, y);
				if (b.isEmptySquare(move)){
					moves.add(new Move(move, p));
				}else{
					if (b.getTeam(move) != p.isPlayer){
						moves.add(new Move(move, p));
					}
					break;
				}
			}
		}
		return moves;
	}
}
