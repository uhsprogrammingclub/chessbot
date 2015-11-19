package chessbot;
import java.util.*;

public class OpeningHashEntry {

	public long zobrist;
	public List<Move> moves = new ArrayList<Move>();
	
	static int PV_NODE = 1;
	static int CUT_NODE = 2;
	static int ALL_NODE = 3;
	
	public OpeningHashEntry(long zobrist, Move move){
		this.zobrist = zobrist;
		this.moves.add(move);
	}
	
	@Override
	public String toString(){
		return "Zobrist: " + zobrist + " Moves: " + moves;
	}
}
