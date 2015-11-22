package chessbot;

public class BitBoard {
	long[] pieceBitBoards = new long[14];
	static long[] setMask = new long[64];
	static long[] clearMask = new long[64];
	
	static final long RANK_1 = Long.decode("0x00000000000000FF");
	static final long FILE_A = Long.decode("0x0101010101010101");
	
	enum BitBoards {
		White(0),
		Black(1),
		WPawn(2),
		WRook(3),
		WBishop(4),
		WKnight(5),
		WQueen(6),
		WKing(7),
		BPawn(8),
		BRook(9),
		BBishop(10),
		BKnight(11),
		BQueen(12),
		BKing(13);
		public final int i;
		
		BitBoards(int i){
			this.i = i;
		}
	}
	
	public BitBoard(Board board){
		initMasks();
		for (int i = 0; i < board.locations.length; i++){
			Piece p = board.locations[i];
			if (p.worth != 0){
				setBitFromPiece(p);
				
			}
		}
	}
	
	static void initMasks(){
		for (int i = 0; i < 64; i++){
			setMask[i] = (long)1 << i;
			clearMask[i] = ~setMask[i];
		}
	}
	
	
	static int getBitBoard(String p, boolean player){
		int index = 14;
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
		if (!player) index +=6;
		
		return index;
	}
	
	void setBitFromPiece(Piece p){
		if (p.worth != 0){
			if (p.player){
				pieceBitBoards[BitBoards.White.i] = setBit(pieceBitBoards[BitBoards.White.i], p.position.getIndex());
			}else{
				pieceBitBoards[BitBoards.Black.i] = setBit(pieceBitBoards[BitBoards.Black.i], p.position.getIndex());
			}
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
	
	long pawnsAbleToPush(long pawns, boolean player){
		if (player){
			pawns <<= 8;
		}else{
			pawns >>= 8;
		}
		return (pawns & ~combine());
	}
	
	long pawnsAbleToDoublePush(long pawns, boolean player){
		long rank4;
		if (player){
			rank4 = RANK_1 << 24;
		}else{
			rank4 = RANK_1 << 32;
		}
		return (pawnsAbleToPush(pawnsAbleToPush(pawns, player), player) & ~combine() & rank4);
	}
	
	long pawnsAbleToAttack(long pawns, boolean player, long enPassant){
		long rightAttack = 0;
		long leftAttack = 0;
		if (player){
			rightAttack = (pawns << 9 & ~FILE_A) & ~RANK_1;
			leftAttack = (pawns << 7 & ~(FILE_A << 7)) & ~RANK_1;
		}else{
			rightAttack = (pawns >> 7 & ~FILE_A) & ~(RANK_1 << 56);
			leftAttack = (pawns >> 9 & ~(FILE_A << 7)) & ~(RANK_1 << 56);
		}
		///System.out.println("Right Attack "+player+":\n" + toString(rightAttack));
		//System.out.println("Left Attack "+player+":\n" + toString(leftAttack));
		long enemy;
		if (player){
			enemy = pieceBitBoards[BitBoards.Black.i];
		}else{
			enemy = pieceBitBoards[BitBoards.White.i];
		}
		enemy |= enPassant;
		long attack = (rightAttack | leftAttack) & enemy;
		//System.out.println("Attack "+player+":\n" + toString(attack));
		//System.out.println("Enemy of "+player+":\n" + toString(enemy));
		//System.out.println("White Pieces:\n" + toString(pieceBitBoards[BitBoards.White.i]));
		//System.out.println("Black Pieces:\n" + toString(pieceBitBoards[BitBoards.Black.i]));
		return attack;
	}

	
	static int bitScanForward(long bb){
		assert (bb != 0);
		
		double LS1B = (double)(bb & -bb);
		
		int exp = (int) (Double.doubleToLongBits(LS1B) >>> 52);
        return (exp & 2047) - 1023;
		
	}
	
}
