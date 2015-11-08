package chessbot;

public class MoveAndScore implements Comparable<MoveAndScore>{
	Move move;
	double score;
	
	public MoveAndScore(Move m, double s){
		move = m;
		score = s;
	}
	
	@Override
	public String toString(){
		return move + " " + Math.round(score*100)/100.0;
	}
	
	@Override
	public int compareTo(MoveAndScore other) {
		return this.score < other.score ? 1
    	     : this.score > other.score ? -1
    	     : 0;
    }
	
	@Override
	public boolean equals(Object other) {
		MoveAndScore o = (MoveAndScore)other;
		return this.move.equals(o.move);
    }
}
