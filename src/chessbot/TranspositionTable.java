package chessbot;

import java.util.Hashtable;

public class TranspositionTable {
	
	final static int hashSize = 100000;
	
	public static Hashtable<Integer, HashEntry> trans = new Hashtable<Integer, HashEntry>(hashSize);
	
	public static void addEntry(HashEntry entry){
		if (entry == null) return;
		long zobrist = entry.zobrist;
		int index = Zobrist.getIndex(zobrist);
		HashEntry oldEntry = TranspositionTable.trans.get(index);
		
		//if (oldEntry != null && (oldEntry.move.toString().equals("bF8->B4") || entry.move.toString().equals("bF8->B4"))){
			//System.out.println("!!!. "+oldEntry + "vs."+entry );
		//}
		
		if(oldEntry == null 
				|| oldEntry.depthLeft < entry.depthLeft || oldEntry.zobrist != entry.zobrist){
			trans.put(index, entry);
		}else if (oldEntry.nodeType != HashEntry.PV_NODE && entry.nodeType == HashEntry.PV_NODE){
				trans.put(index, entry);
		}else if (oldEntry.depthLeft == entry.depthLeft){
			if (oldEntry.nodeType == entry.nodeType){
				if (oldEntry.nodeType == HashEntry.CUT_NODE && oldEntry.eval < entry.eval
					|| oldEntry.nodeType == HashEntry.ALL_NODE && oldEntry.eval > entry.eval){
					trans.put(index, entry);
				}else if (oldEntry.nodeType == HashEntry.PV_NODE){
					System.out.println("1. "+oldEntry + "vs."+entry );
				}
			}
			if(entry.nodeType == HashEntry.PV_NODE){
				trans.put(index, entry);
			}
			if (oldEntry.nodeType == HashEntry.PV_NODE && entry.nodeType == HashEntry.PV_NODE && oldEntry.zobrist == entry.zobrist && oldEntry.eval != entry.eval){
				System.out.println("3. "+oldEntry + "vs."+entry );
			}
		}else{
			//System.out.println("4. "+oldEntry + "vs."+entry );
		}
		
		if (oldEntry != null && (oldEntry.move.toString().equals("qF6->xQD4") || entry.move.toString().equals("bF8->B4"))){
			//System.out.println("!!!. "+oldEntry + "vs."+entry );
			//System.out.println("new. "+TranspositionTable.trans.get(index) );
		}
	}
	
}
