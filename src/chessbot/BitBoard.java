package chessbot;

public class BitBoard {
	long[] pieceBitBoards = new long[8];
	static long[] setMask = new long[64];
	static long[] clearMask = new long[64];
	static long[] kingAttacks = new long[64];
	
	static final long RANK_1 = 0x00000000000000FFL;
	static final long FILE_A = 0x0101010101010101L;
	
	enum BitBoards {
		White(0),
		Black(1),
		Pawn(2),
		Rook(3),
		Bishop(4),
		Knight(5),
		Queen(6),
		King(7);
		public final int i;
		
		BitBoards(int i){
			this.i = i;
		}
	}
	
	public BitBoard(Board board){
		if (setMask[0] == 0){
			initPresets();
		}
 		for (int i = 0; i < board.locations.length; i++){
			Piece p = board.locations[i];
			if (p.worth != 0){
				setBitFromPiece(p);
				
			}
		}
	}
	
	static void initPresets(){
		for (int i = 0; i < 64; i++){
			setMask[i] = (long)1 << i;
			clearMask[i] = ~setMask[i];
		}
	}
	
	
	static int getBitBoard(String p){
		int index = 8;
		if (p == "p"){
			index = 2;
		}else if (p == "r"){
			index = 3;
		}else if (p == "b"){
			index = 4;
		}else if (p == "n"){
			index = 5;
		}else if (p == "q"){
			index = 6;
		}else if (p == "k"){
			index = 7;
		}
		
		return index;
	}
	
	void setBitFromPiece(Piece p){
		if (p.worth != 0){
			if (p.player){
				pieceBitBoards[BitBoards.White.i] = setBit(pieceBitBoards[BitBoards.White.i], p.position.getIndex());
			}else{
				pieceBitBoards[BitBoards.Black.i] = setBit(pieceBitBoards[BitBoards.Black.i], p.position.getIndex());
			}
			int pieceBoard = getBitBoard(p.symbol);
			pieceBitBoards[pieceBoard] = setBit(pieceBitBoards[pieceBoard], p.position.getIndex());
		}
		
	}
	
	static long setBit(long b, int index){
		b |= setMask[index];
		return b;
	}
	
	static long clearBit(long b, int index){
		b &= clearMask[index];
		return b;
	}

	
	static String toString(long bitboard){
		String aString = "";
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < 8; x++) {
				if ((bitboard & ((long)1 << (x+y*8))) != 0){
					aString += "X ";
				}else{
					aString += "- ";
				}
			}
			// Create a new line
			aString += "\n";
		}
		return aString;
	}
	
	long combine() {
		long combinedBitboard = 0;
		for (int i = 0; i < 2; i++){
			combinedBitboard |= pieceBitBoards[i];
		}
		return combinedBitboard;
	}
	
	long pawnPushTo(long pawns, boolean player){
		long rank4;
		long pushBB = 0;
		if (player){
			rank4 = RANK_1 << 24;
			pawns = up(pawns);
			pawns &= ~combine();
			pushBB |= pawns;
			pawns = up(pawns);
		}else{
			rank4 = RANK_1 << 32;
			pawns = down(pawns);
			pawns &= ~combine();
			pushBB |= pawns;
			pawns = down(pawns);
		}
		pushBB |= pawns & ~combine() & rank4;
		
		return (pushBB);
	}
	

	
	long pawnsAttackTo(long pawns, boolean player, long enPassant){
		long rightAttack = 0;
		long leftAttack = 0;
		if (player){
			rightAttack = upRight(pawns);
			leftAttack = upLeft(pawns);
		}else{
			rightAttack = downRight(pawns);
			leftAttack = downLeft(pawns);
		}
		long enemy;
		if (player){
			enemy = pieceBitBoards[BitBoards.Black.i];
		}else{
			enemy = pieceBitBoards[BitBoards.White.i];
		}
		enemy |= enPassant;
		long attack = (rightAttack | leftAttack) & enemy;
		return attack;
	}

	
	static int bitScanForward(long bb){
		assert (bb != 0);
		
		double LS1B = (double)(bb & -bb);
		
		int exp = (int) (Double.doubleToLongBits(LS1B) >>> 52);
		
		int index = (exp & 2047) - 1023;
        return index;
		
	}
	
	static int countSetBits(long bb) {
		int set = 0;
		while (bb != 0) {
			bb &= (bb - 1);
			set++;
		}
		return set;
	}
	
	static int[] getSetBits(long bb){
		
		int[] setBits = new int[countSetBits(bb)];
		
		int i = 0;
		while (bb != 0){
			int index = BitBoard.bitScanForward(bb);
			setBits[i] = index;
			bb = BitBoard.clearBit(bb, index);
			
			if (bb == 0 && i != setBits.length-1){
				System.out.println("ERROR: getSetBits did not get index of all bits.");
			}
			i++;
		}
		
		return setBits;
	}
	
	
	//helper methods
	static long up(long bb){return bb << 8;}
	static long down(long bb){ return bb >>> 8; }
	static long right(long bb){ return bb << 1 & ~FILE_A; }
	static long left(long bb){ return bb >> 1 & ~(FILE_A<<7); }
	static long upRight(long bb){ return up(right(bb)); }
	static long upLeft(long bb){ return up(left(bb)); }
	static long downRight(long bb){ return down(right(bb)); }
	static long downLeft(long bb){ return down(left(bb)); }
}
