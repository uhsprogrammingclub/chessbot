package chessbot;

import java.util.Hashtable;

public class TranspositionTable {
	
	final static int hashSize = 100000;
	
	public static Hashtable<Integer, HashEntry> trans = new Hashtable<Integer, HashEntry>(hashSize);
	
	public static void addEntry(HashEntry entry){
		long zobrist = entry.zobrist;
		int index = Zobrist.getIndex(zobrist);
		if(TranspositionTable.trans.get(index) == null 
				|| TranspositionTable.trans.get(index).depthLeft < entry.depthLeft
				|| (TranspositionTable.trans.get(index).depthLeft == entry.depthLeft
				&& TranspositionTable.trans.get(index).alpha >= entry.alpha
				&& TranspositionTable.trans.get(index).beta <= entry.beta)
				){
						trans.put(index, entry);	
		}
	}
	
}
