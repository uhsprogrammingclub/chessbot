package chessbot;

import java.util.*;

public class PV {
	
	List<HashEntry> hashList;
	
	public PV(Board b){
		hashList = new ArrayList<HashEntry>();
		findHashEntry(b);
	}
	
	void findHashEntry(Board b){
		long currentZHash = Zobrist.getZobristHash(b);
		int index = Zobrist.getIndex(currentZHash);
		//find entry with same index
		HashEntry entry = TranspositionTable.trans.get(index);			
		if(entry != null //if there is an old entry
				&& entry.zobrist == currentZHash ){ //and the boards are the same
			hashList.add(entry);
			
			if (entry.move == null){
				return;
			}

			if (entry.move.executed){
				System.out.println("PV in loop");
				return;
			}
			entry.move.execute();
			findHashEntry(b);
			entry.move.reverse();
		}
		
	}

	@Override
	public String toString() {
		String s = "";
		for (HashEntry h: hashList){
			if (h.move != null){
				s += h.move;
				s += " ("+h.eval/100.0+ " node:"+h.nodeType+")";
				s += "; ";
			}
		}
		return s;
	}

}
