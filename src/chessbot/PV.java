package chessbot;

import java.util.*;

public class PV {
	
	List<HashEntry> hashList;
	List<Long> zobristList;
	String PVString = "";
	
	public PV(Board b){
		hashList = new ArrayList<HashEntry>();
		zobristList = new ArrayList<Long>();
		findHashEntry(b);
	}
	
	void findHashEntry(Board b){
		long currentZHash = b.currentZobrist;
		int index = Zobrist.getIndex(currentZHash, TranspositionTable.hashSize);
		//find entry with same index
		HashEntry entry = TranspositionTable.trans.get(index);			
		if(entry != null //if there is an old entry
				&& entry.zobrist == currentZHash ){ //and the boards are the same
			if (entry.move == null || entry.nodeType != HashEntry.PV_NODE){
				return;
			}

			if (zobristList.contains(entry.zobrist)){
				//System.out.println("PV in loop");
				zobristList.add(entry.zobrist);
				return;
			}
			hashList.add(entry);
			zobristList.add(entry.zobrist);
			
			Move PVMove = new Move(b, entry.move);
			PVString += PVMove.getSAN();
			PVString += " ("+entry.eval/100.0+")";
			PVString += "; ";
			PVMove.execute();
			findHashEntry(b);
			PVMove.reverse();
		}
		
	}

	@Override
	public String toString() {
		return PVString;
	}

}
