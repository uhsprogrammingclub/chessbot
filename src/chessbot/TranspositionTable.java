package chessbot;

import java.util.Hashtable;

public class TranspositionTable {
	
	final static int hashSize = 100000;
	
	public static Hashtable<Integer, HashEntry> trans = new Hashtable<Integer, HashEntry>(hashSize);
	
	public static void addEntry(HashEntry entry){
		long zobrist = entry.zobrist;
		int index = (int)(zobrist % hashSize);
		if(TranspositionTable.trans.get(index) == null //if no entry OR
				|| (TranspositionTable.trans.get(index).depthLeft < entry.depthLeft //if went to deeper depth than previous entry OR
				|| (TranspositionTable.trans.get(index).depthLeft == entry.depthLeft //went to same depth AND
					&& TranspositionTable.trans.get(index).eval < entry.eval))){ // found higher score
				trans.put(index, entry);	
		}
	}
	
}
