package chessbot;

public class HashEntry {
	
	public long zobrist;
	public int depthLeft;
	public double eval;
	public double alpha;
	public double beta;
	public Move move;
	
	public HashEntry(long zobrist, int depthLeft, double eval, double alpha, double beta, Move move){
		this.zobrist = zobrist;
		this.depthLeft = depthLeft;
		this.eval = eval;
		this.alpha = alpha;
		this.beta = beta;
		this.move = move;
	}
	
	@Override
	public String toString(){
		return "Zobrist: " + zobrist + " Depth Left: " + depthLeft + " Eval: " + eval + " Alpha: " + alpha + " Move:" + move;
	}
}
