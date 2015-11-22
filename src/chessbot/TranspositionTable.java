package chessbot;

import java.util.Hashtable;

public class TranspositionTable {
	
	final static int hashSize = 1000000;
	
	public static Hashtable<Integer, HashEntry> trans = new Hashtable<Integer, HashEntry>(hashSize);
	
	public static void addEntry(HashEntry entry){
		if (entry == null) return;
		long zobrist = entry.zobrist;
		int index = Zobrist.getIndex(zobrist, hashSize);
		HashEntry oldEntry = TranspositionTable.trans.get(index);

		if(oldEntry == null 
				|| oldEntry.depthLeft < entry.depthLeft || (oldEntry.zobrist != entry.zobrist && oldEntry.nodeType != HashEntry.PV_NODE) || entry.nodeType == HashEntry.PV_NODE){
			trans.put(index, entry);
		}else if (oldEntry.depthLeft == entry.depthLeft){
			if (oldEntry.nodeType == entry.nodeType){
				if (oldEntry.nodeType == HashEntry.CUT_NODE && oldEntry.eval < entry.eval
					|| oldEntry.nodeType == HashEntry.ALL_NODE && oldEntry.eval > entry.eval){
					trans.put(index, entry);
				}
			}
		}
		
	}
	
}
