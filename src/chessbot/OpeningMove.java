package chessbot;

public class OpeningMove implements Comparable<OpeningMove>{
	Move move;
	int timesUsed;
	int wins;
	String ECO;
	
	public OpeningMove(Move m, String ECO, boolean win){
		move = m;
		this.ECO = ECO;
		if (win){
			wins = 1;
		}
		timesUsed = 1;
	}
	
	public String getECOName(){
		return OpeningBook.ECO.get(ECO);
	}
	
	@Override
	public String toString(){
		return move + " " + ECO;
	}
	
	
	@Override
	public boolean equals(Object other) {
		OpeningMove o = (OpeningMove)other;
		return (this.move.equals(o.move) && this.ECO.equals(o.ECO));
    }

	@Override
	public int compareTo(OpeningMove o) {
		return this.timesUsed < o.timesUsed ? 1
	    	     : this.timesUsed > o.timesUsed ? -1
	    	     : 0;
	}
}
