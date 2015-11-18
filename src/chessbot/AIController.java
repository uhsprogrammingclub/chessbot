package chessbot;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class AIController {
	
	// Constants
	final int DEPTH_LIMIT = 64;
	final double TIME_LIMIT = 10000;
	final int INFINITY = 1000000000;
	
	//Search stats
	int totalNodes = 1;
	int quiescentNodes = 0;
	int fh = 0;
	int fhf = 0;
	int researches = 0;
	double startTime = 0;
	boolean stopSearching = false;
	MoveAndScore bestRootMove = null;
	int staticComputations = 0;
	int evaluateToDepth = 0;
	
	// Speed up techniques
	boolean PVSearch = true; // Using null window search
	boolean killerHeuristic = true; // Records 3 killer moves per depth to evaluate first
	boolean TTMoveReordering = true; // Transposition entries are evaluated first
	boolean useTTEvals = true; // Use previous TT entries when encountered
	boolean iterativeDeepeningMoveReordering = true; // Evaluate the best moves at the previous depth first
	boolean quiescenceSearch = true; // Complete basic quiescence search after finishing main search to counter horizon effect
	boolean sortMoves = true;
	boolean aspirationWindow = false;
	
	//Hashtable to store number of computations at each depth (serves no functional purpose)
	Hashtable<Integer, Integer> computationsAtDepth = new Hashtable<Integer, Integer>(100);

	// To hold the principal variation at each depth (serves no functional purpose)
	List<String> depthsPV = new ArrayList<>();

	public AIController() {
		startTime = System.currentTimeMillis();
	}
	
	void checkTimeLimit(){
		if (System.currentTimeMillis() - startTime > TIME_LIMIT){
			stopSearching = true;
		}
	}
	
	@Override
	public String toString() {
		String s = "";
		s+="Root Move: " + bestRootMove + "\n";
		s+="Time expended: " + (System.currentTimeMillis() - startTime)/1000.0 +"s" + "\n";
		s+="Final Depth: " + evaluateToDepth + "\n";
		s+="Nodes Evaluated: " + totalNodes + "\n";
		s+="Nodes Per Second: " + (int)(totalNodes/((System.currentTimeMillis() - startTime)/1000)) + "\n";
		s+="Fail High Nodes: " + fh + "\n";
		s+="Fail High First Nodes: " + fhf + "\n";
		s+="Quiescent Node Ratio: " + quiescentNodes*1000/totalNodes/10.0 + "%\n";
		s+="Efficiency: " + fhf*100/fh + "%\n";
		s+="Re-searches: " + researches + "\n";
		s+="Static computations at each depth: " + computationsAtDepth + "\n";
		s+="Total static computations: " + staticComputations + "\n";
		s+="Transposition Table Size: " + TranspositionTable.trans.size() + "\n";
		return s;
	}
	

}
