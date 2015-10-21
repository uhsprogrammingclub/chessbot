package chessbot;

public class HashEntry {
	
	public long zobrist;
	public int depthLeft;
	public double eval;
	public Move move;
	public Boolean player;
	
	public HashEntry(long zobrist, int depthLeft, double eval, Move move, Boolean player){
		this.zobrist = zobrist;
		this.depthLeft = depthLeft;
		this.eval = eval;
		this.move = move;
		this.player = player;
	}
	
	@Override
	public String toString(){
		return "Zobrist: " + zobrist + " Depth Left: " + depthLeft + " Eval: " + eval + " Move:" + move + " Player: " + player;
	}
}
