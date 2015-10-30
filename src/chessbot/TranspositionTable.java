package chessbot;

import java.util.Hashtable;

public class TranspositionTable {
	
	final static int hashSize = 100000;
	
	public static Hashtable<Integer, HashEntry> trans = new Hashtable<Integer, HashEntry>(hashSize);
	
	public static void addEntry(HashEntry entry){
		long zobrist = entry.zobrist;
		int index = Zobrist.getIndex(zobrist);
		HashEntry oldEntry = TranspositionTable.trans.get(index);
		if(oldEntry == null 
				|| oldEntry.depthLeft < entry.depthLeft){
			trans.put(index, entry);
		}else if (oldEntry.depthLeft == entry.depthLeft){
			if (oldEntry.nodeType == entry.nodeType){
				if (oldEntry.nodeType == HashEntry.CUT_NODE && oldEntry.eval < entry.eval
					|| oldEntry.nodeType == HashEntry.ALL_NODE && oldEntry.eval > entry.eval){
					trans.put(index, entry);
				}
			}else if(oldEntry.nodeType != HashEntry.PV_NODE){
				trans.put(index, entry);
			}
		}else if (oldEntry.nodeType != HashEntry.PV_NODE && entry.nodeType == HashEntry.PV_NODE){
			trans.put(index, entry);
		}
		/*if(oldEntry == null 
				|| oldEntry.depthLeft < entry.depthLeft){
			trans.put(index, entry);
		}else if (oldEntry.depthLeft == entry.depthLeft){
			if (oldEntry.nodeType == HashEntry.CUT_NODE && oldEntry.eval < entry.eval
				|| oldEntry.nodeType == HashEntry.ALL_NODE && oldEntry.eval > entry.eval){
				trans.put(index, entry);
			}
		}*/
		
	}
	
}
