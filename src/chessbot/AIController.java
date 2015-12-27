package chessbot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class AIController {
	
	// Constants
	final int DEPTH_LIMIT = 64;
	final int INFINITY = 1000000000;
	
	// Time controller variables
	static int timeLeft = 60000;
	static int plyFromOpening = -1;
	
	//Set the default time limit - if using standard moves/second
	static double timeLimit = 0;
	
	//Variables for time function
	double dampeningFactor = 0.03; // Must be positive - negated in function
	int upperGameBound = 130; // Try 207 or 171 if issues persist, 171 is 98% confidence
	static double extensionConstant = 1.3;	
	
	//Search stats
	int usedTTCount = 0;
	int totalNodes = 1;
	int quiescentNodes = 0;
	int fh = 1;
	int fhf = 0;
	int researches = 0;
	double startTime = 0;
	boolean stopSearching = false;
	MoveAndScore bestRootMove = null;
	int staticComputations = 0;
	int evaluateToDepth = 0;
	String currentECO = "";
	
	// Speed up techniques
	static boolean PVSearch = true; // Using null window search
	static boolean killerHeuristic = true; // Records 3 killer moves per depth to evaluate first
	static boolean TTMoveReordering = true; // Transposition entries are evaluated first
	static boolean useTTEvals = true; // Use previous TT entries when encountered
	static boolean iterativeDeepeningMoveReordering = true; // Evaluate the best moves at the previous depth first
	static boolean quiescenceSearch = true; // Complete basic quiescence search after finishing main search to counter horizon effect
	static boolean sortMoves = true;
	static boolean aspirationWindow = false;
	static boolean useOpeningBook = true;
	static boolean useBitBoards = true;
	static boolean usePawnEvaluations = true;
	static boolean useTimeControls = true;
	
	//Hashtable to store number of computations at each depth (serves no functional purpose)
	Hashtable<Integer, Integer> computationsAtPly = new Hashtable<Integer, Integer>(100);

	// To hold the principal variation at each depth (serves no functional purpose)
	List<String> depthsPV = new ArrayList<>();

	public AIController() {
		startTime = System.currentTimeMillis();
	}
	
	int getAllottedTime(){
		
		if(plyFromOpening < 0){ return -1;}
		
		// for n != 0
		// https://www.wolframalpha.com/input/?i=ke%5E%28-n*l%29%2F%28-n%29-ke%5E%28-n*x%29%2F%28-n%29%3D+z+solve+for+k
		double scalar = (dampeningFactor * timeLeft * Math.pow(Math.E, dampeningFactor * (upperGameBound + plyFromOpening))) / (Math.pow(Math.E, upperGameBound * dampeningFactor) - Math.pow(Math.E, dampeningFactor * plyFromOpening));
		int answer = (int)(scalar * Math.pow(Math.E, -dampeningFactor * plyFromOpening));
		System.out.println("Ply: " + plyFromOpening + " Allowed Time: " + answer + " Time Left: " + timeLeft);
		return answer;
	}
	
	void checkTimeLimit(){
		if (System.currentTimeMillis() - startTime > timeLimit){
			stopSearching = true;
		}
	}
	
	static void setComputationTime(double time){
		timeLimit = time;
	}
	
	public void printInfo(){
		
		List<String> info = this.searchInfo();
		
		System.out.format("%-18s | %-14s | %-11s | %-15s | %-7s | %-7s | %-7s | %s",
				"\nRoot Move And Eval", "Time Expended", "Final Depth", "Nodes Evaluated", "NPS", "FH", "FHF", "Efficiency\n" 
		);
		System.out.format("%-18s | %-14s | %-11s | %-15s | %-7s | %-7s | %-7s | %s",
				info.get(0), info.get(1), info.get(2), info.get(3), info.get(4), info.get(5), info.get(6), info.get(7) + "\n"
		
		);
		System.out.println("─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
		System.out.format("%-22s | %-11s | %-18s | %-7s | %-13s | %s",
				"Quiescent Node Percent", "Re-searches", "Total Computations", "TT Size", "TT Evals Used", "Computations at Depths\n"
		);
		System.out.format("%-22s | %-11s | %-18s | %-7s | %-13s | %s",
				info.get(8), info.get(9), info.get(10), info.get(11), info.get(12), info.get(13) + "\n"
		);
		
	}
	
	@Override
	public String toString() {
		
		printInfo();
		return "";
		
	}
	
	public List<String> searchInfo(){
		List<String> info = new ArrayList<>();

		String[] infoList = new String[] {
				bestRootMove.toString(),
				(System.currentTimeMillis() - startTime)/1000.0 +"s",
				Integer.toString(evaluateToDepth),
				Integer.toString(totalNodes),
				Integer.toString((int)(totalNodes/((System.currentTimeMillis() - startTime)/1000))),
				Integer.toString(fh),
				Integer.toString(fhf),
				(fhf*100/fh) + "%",
				quiescentNodes*1000/totalNodes/10.0 + "%",
				Integer.toString(researches),
				Integer.toString(staticComputations),
				TranspositionTable.trans.size() + "",
				Integer.toString(usedTTCount),
				computationsAtPly.toString()
		};
	
		info.addAll(Arrays.asList(infoList));
		return info;
	}
}
