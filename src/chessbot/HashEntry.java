package chessbot;

public class HashEntry {
	
	public long zobrist;
	public int depthLeft;
	public double eval;
	public int nodeType;
	public Move move;
	
	static int PV_NODE = 1;
	static int CUT_NODE = 2;
	static int ALL_NODE = 3;
	
	public HashEntry(long zobrist, int depthLeft, double eval, int nodeType, Move move){
		this.zobrist = zobrist;
		this.depthLeft = depthLeft;
		this.eval = eval;
		this.nodeType = nodeType;
		this.move = move;
	}
	
	@Override
	public String toString(){
		return "Zobrist: " + zobrist + " Depth Left: " + depthLeft + " Eval: " + eval + " Node: " + nodeType + " Move:" + move;
	}
}
