package chessbot;

public class MovesAndScores {
	Move move;
	int score;
	
	public MovesAndScores(Move m, int s){
		move = m;
		score = s;
	}
	
	@Override
	public String toString(){
		return "[ " + move + " Score: " + score + "]";
	}
}
