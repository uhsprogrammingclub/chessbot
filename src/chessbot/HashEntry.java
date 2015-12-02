package chessbot;

public class HashEntry {
	
	public long zobrist;
	public int depth;
	public int eval;
	public int nodeType;
	public Move move;
	
	static int PV_NODE = 1;
	static int CUT_NODE = 2;
	static int ALL_NODE = 3;
	
	public HashEntry(long zobrist, int depth, int eval, int nodeType, Move move){
		this.zobrist = zobrist;
		this.depth = depth;
		this.eval = eval;
		this.nodeType = nodeType;
		this.move = move;
	}
	
	@Override
	public String toString(){
		return "Zobrist: " + zobrist + " Depth: " + depth + " Eval: " + eval + " Node: " + nodeType + " Move:" + move;
	}
}
