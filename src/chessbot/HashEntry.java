package chessbot;

public class HashEntry {
	
	public long zobrist;
	public int depthLeft;
	public int eval;
	public Move move;
	
	public HashEntry(long zobrist, int depthLeft, int eval, Move move){
		this.zobrist = zobrist;
		this.depthLeft = depthLeft;
		this.eval = eval;
		this.move = move;
	}
	
	@Override
	public String toString(){
		return "Zobrist: " + zobrist + " Depth Left: " + depthLeft + " Eval: " + eval + " Move:" + move;
	}
}
