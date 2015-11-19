package chessbot;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ChessbotTest {
	
	Game g;
	
	@Before
	public void init(){
		g = new Game();
	}
	
	@Test
	public void testSuicides(){
		
		//Set the position to a suicide position
		g.setFEN("rnbqkbnr/pppp1ppp/8/4p3/3PP3/8/PPP2PPP/RNBQKBNR b KQkq - 0 2");
		g.setBoard(Game.setup);
		
		//assertTrue(g.getBotMove(g.b) >= -10000);
		
	}
	
	@Test
	public void testPV(){
		
		g.setFEN("rnbqkbnr/pppp1ppp/8/4p3/3PP3/8/PPP2PPP/RNBQKBNR b KQkq - 0 2");
		
		//Tests things about the PV to make sure it is working properly
		
	}
	
	/** Testing Positions **/
	//Forsyth-Edwards Notation (FEN) game setup default
	//static String setup = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"; 
		
	//Suicide setup
	//static String setup = "rnbqkbnr/pppp1ppp/8/4p3/3PP3/8/PPP2PPP/RNBQKBNR b KQkq - 0 2";
	//static String setup = "rnb1kbnr/pppp1ppp/8/4p3/3PP2q/5N2/PPP2PPP/RNBQKB1R b KQkq - 0 1";
		
	//Exception setup
	//static String setup = "rnb1k1nr/pppp1ppp/8/4p3/1b1Pq3/P1N5/1PP1QPPP/R1B1KBNR b KQkq - 0 3";
		
	//Forsyth-Edwards Notation (FEN) game setup test
	//static String setup = "r1bqkbnr/pppp1ppp/4p3/2P5/3Pn3/5N2/PP3PPP/RNBQK2R b KQkq - 0 7"; 

	//Horizon Effect Test #1
	//static String setup = "7K/8/8/R7/R7/8/P1rr4/7k b - - 0 1"; 
		
	//Horizon Effect Test #2
	//static String setup = "7k/1n1n4/2P5/8/5b2/8/7P/7K b - - 0 1"; 

	//static String setup = "rnbqkbnr/pppp1ppp/8/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 0 2"; 

}
