package chessbot;

public class HashEntry {
	
	public long zobrist;
	public int depthLeft;
	public int eval;
	public int sp;
	public int nodeType;
	public Move move;
	
	static int PV_NODE = 1;
	static int CUT_NODE = 2;
	static int ALL_NODE = 3;
	
	public HashEntry(long zobrist, int depthLeft, int eval, int nodeType, Move move, int sp){
		this.zobrist = zobrist;
		this.depthLeft = depthLeft;
		this.eval = eval;
		this.sp = sp;
		this.nodeType = nodeType;
		this.move = move;
	}
	
	@Override
	public String toString(){
		return "Zobrist: " + zobrist + " Depth Left: " + depthLeft + " Eval: " + eval + " Node: " + nodeType + " Move:" + move;
	}
}
