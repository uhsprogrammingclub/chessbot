package chessbot;

public class MoveAndScore implements Comparable<MoveAndScore>{
	Move move;
	int score;
	
	public MoveAndScore(Move m, int s){
		move = m;
		score = s;
	}
	
	@Override
	public String toString(){
		return "[ " + move + " Score: " + score + "]";
	}
	
	@Override
	public int compareTo(MoveAndScore other) {
		return this.score < other.score ? 1
    	     : this.score > other.score ? -1
    	     : 0;
    }
}
