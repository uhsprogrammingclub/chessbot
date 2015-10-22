package chessbot;

import java.util.*;

public class PV {
	
	List<HashEntry> hashList;
	
	public PV(Board b, boolean player){
		hashList = new ArrayList<HashEntry>();
		findHashEntry(b, player);
	}
	
	void findHashEntry(Board b, boolean player){
		long currentZHash = Zobrist.getZobristHash(b);
		int index = (int)(currentZHash % TranspositionTable.hashSize);
		//find entry with same index
		HashEntry oldEntry = TranspositionTable.trans.get(index);			
		if(oldEntry != null //if there is an old entry
				&& oldEntry.zobrist == currentZHash //and the boards are the same
				&& oldEntry.player == player){ //and it's the same player's move
			hashList.add(oldEntry);
			
			if (oldEntry.move.executed){
				return;
			}
			oldEntry.move.execute();
			findHashEntry(b, !player);
			oldEntry.move.reverse();
		}
		
	}

	@Override
	public String toString() {
		String s = "";
		for (HashEntry h: hashList){
			s += h.move.piece + " " + h.move.from + "->" + (h.move.destinationPc.toString().equals("-") ? "" : h.move.destinationPc + " ") + h.move.to + ";";
		}
		return s;
	}

}
