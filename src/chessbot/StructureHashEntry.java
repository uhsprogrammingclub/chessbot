package chessbot;

public class StructureHashEntry {
	
	public long pawnZobrist;
	public int eval;
	
	public StructureHashEntry(long zobrist, int eval){
		this.pawnZobrist = zobrist;
		this.eval = eval;
	}
	
	@Override
	public String toString(){
		return "Pawn Zobrist: " + pawnZobrist + " Evaluation: " + eval;
	}

}
