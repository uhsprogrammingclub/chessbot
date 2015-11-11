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
		HashEntry oldEntry = TranspositionTable.trans.get(index);			
		if(oldEntry != null //if there is an old entry
				&& oldEntry.zobrist == currentZHash){ //and the boards are the same
			hashList.add(oldEntry);

			if (oldEntry.move.executed){
				System.out.println("PV in loop");
				return;
			}
			oldEntry.move.execute();
			findHashEntry(b);
			oldEntry.move.reverse();
		}
		
	}

	@Override
	public String toString() {
		String s = "";
		for (HashEntry h: hashList){
			s += h.move;
			s += " ("+h.eval/100.0+ " node:"+h.nodeType+")";
			s += "; ";
		}
		return s;
	}

}
