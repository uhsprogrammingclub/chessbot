package chessbot;

import java.util.*;

public class PV {
	
	List<HashEntry> hashList;
	String PVString = "";
	
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
			if (entry.move == null){
				return;
			}
			hashList.add(entry);

			if (entry.move.executed){
				System.out.println("PV in loop");
				return;
			}
			PVString += entry.move.getSAN();
			PVString += " ("+entry.eval/100.0+ " node:"+entry.nodeType+")";
			PVString += "; ";
			entry.move.execute();
			findHashEntry(b);
			entry.move.reverse();
		}
		
	}

	@Override
	public String toString() {
		return PVString;
	}

}
