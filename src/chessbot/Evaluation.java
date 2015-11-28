package chessbot;

public class Evaluation {
	
	public static enum Value { 
		ISOLATED_PAWN, 
		DOUBLED_PAWN,
		HALF_OPEN_FILE,
		PAWN_CHAIN,
		HOLE,
		PASSED_PAWN
	}
	
	static int isolatedPawnValue = 10;
	static int doubledPawnValue = 10;
	static int halfOpenFileValue = 10;
	static int pawnChainValue = 10;
	static int holeValue = 10;
	static int passedPawnValue = 60;
	
	final static int CHECKMATE = 1000000;
	
	static int pawnPhase = 0;
	static int knightPhase = 1;
	static int bishopPhase = 1;
	static int rookPhase = 2;
	static int queenPhase = 4;
	static int totalPhase = 8 * pawnPhase + 4 * knightPhase + 4 * bishopPhase + 4 * rookPhase + 2 * queenPhase; 
	
	static int[] pawnPieceSquaresE = {		
		  0,  0,  0,  0,  0,  0,  0,  0,
		 50, 50, 50, 50, 50, 50, 50, 50,
		 10, 10, 20, 30, 30, 20, 10, 10,
		  5,  5, 10, 25, 25, 10,  5,  5,
		  0,  0,  0, 20, 20,  0,  0,  0,
		  5, -5,-10,  0,  0,-10, -5,  5,
		  5, 10, 10,-20,-20, 10, 10,  5,
		  0,  0,  0,  0,  0,  0,  0,  0	  
	};
	
	static int[] knightPieceSquaresE = {		
		-50,-40,-30,-30,-30,-30,-40,-50,
		-40,-20,  0,  0,  0,  0,-20,-40,
		-30,  0, 10, 15, 15, 10,  0,-30,
		-30,  5, 15, 20, 20, 15,  5,-30,
		-30,  0, 15, 20, 20, 15,  0,-30,
		-30,  5, 10, 15, 15, 10,  5,-30,
		-40,-20,  0,  5,  5,  0,-20,-40,
		-50,-40,-30,-30,-30,-30,-40,-50
	};
	
	static int[] bishopPieceSquaresE = {
		-20,-10,-10,-10,-10,-10,-10,-20,
		-10,  0,  0,  0,  0,  0,  0,-10,
		-10,  0,  5, 10, 10,  5,  0,-10,
		-10,  5,  5, 10, 10,  5,  5,-10,
		-10,  0, 10, 10, 10, 10,  0,-10,
		-10, 10, 10, 10, 10, 10, 10,-10,
		-10,  5,  0,  0,  0,  0,  5,-10,
		-20,-10,-10,-10,-10,-10,-10,-20
	};
	
	static int[] rookPieceSquaresE = {	
		  0,  0,  0,  0,  0,  0,  0,  0,
		  5, 10, 10, 10, 10, 10, 10,  5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		  0,  0,  0,  5,  5,  0,  0,  0
	};
	
	static int[] queenPieceSquaresE = {	
		-20,-10,-10, -5, -5,-10,-10,-20,
		-10,  0,  0,  0,  0,  0,  0,-10,
		-10,  0,  5,  5,  5,  5,  0,-10,
		 -5,  0,  5,  5,  5,  5,  0, -5,
		  0,  0,  5,  5,  5,  5,  0, -5,
		-10,  5,  5,  5,  5,  5,  0,-10,
		-10,  0,  5,  0,  0,  0,  0,-10,
		-20,-10,-10, -5, -5,-10,-10,-20	
	};
	
	static int[] kingPieceSquaresE = {	
		-30,-40,-40,-50,-50,-40,-40,-30,
		-30,-40,-40,-50,-50,-40,-40,-30,
		-30,-40,-40,-50,-50,-40,-40,-30,
		-30,-40,-40,-50,-50,-40,-40,-30,
		-20,-30,-30,-40,-40,-30,-30,-20,
		-10,-20,-20,-20,-20,-20,-20,-10,
		 20, 20,  0,  0,  0,  0, 20, 20,
		 20, 30, 10,  0,  0, 10, 30, 20
	};
	
	static int[] kingPieceSquaresL = {
		-50,-40,-30,-20,-20,-30,-40,-50,
		-30,-20,-10,  0,  0,-10,-20,-30,
		-30,-10, 20, 30, 30, 20,-10,-30,
		-30,-10, 30, 40, 40, 30,-10,-30,
		-30,-10, 30, 40, 40, 30,-10,-30,
		-30,-10, 20, 30, 30, 20,-10,-30,
		-30,-30,  0,  0,  0,  0,-30,-30,
		-50,-30,-30,-30,-30,-30,-30,-50
	};
		
}
