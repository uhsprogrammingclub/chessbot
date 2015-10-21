package chessbot;

import java.util.Hashtable;

public class TranspositionTable {
	
	final static int hashSize = 10000;
	
	public static Hashtable<Integer, HashEntry> trans = new Hashtable<Integer, HashEntry>(hashSize);
	
	public static void addEntry(HashEntry entry){
		long zobrist = entry.zobrist;
		int index = (int)(zobrist % hashSize);
		if(TranspositionTable.trans.get(index) == null || TranspositionTable.trans.get(index).depthLeft < entry.depthLeft){
				trans.put(index, entry);	
		}
	}
	
}
