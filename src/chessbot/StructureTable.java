package chessbot;

import java.util.Hashtable;

public class StructureTable {
	
	final static int hashSize = 1000000;
	
	public static Hashtable<Integer, StructureHashEntry> pawns = new Hashtable<Integer, StructureHashEntry>(hashSize);
	
	public static void addEntry(StructureHashEntry entry){
		if (entry == null) return;
		long zobrist = entry.pawnZobrist;
		int index = Zobrist.getIndex(zobrist, hashSize);
		StructureHashEntry oldEntry = StructureTable.pawns.get(index);

		if(oldEntry == null || oldEntry.pawnZobrist != entry.pawnZobrist){
			pawns.put(index, entry);
		}
	}
}
