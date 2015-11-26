package chessbot;

public class BB {
	long[] pieceBitBoards = new long[8];
	static long[] setMask = new long[64];
	static long[] clearMask = new long[64];
	static long[] kingAttacks = new long[64];
	static long[] knightAttacks = new long[64];
	static long[] neighborFiles = new long[8];

	static final long RANK_1 = 0x00000000000000FFL;
	static final long FILE_A = 0x0101010101010101L;

	static final int WHITE = 0;
	static final int BLACK = 1;
	static final int PAWNS = 2;
	static final int KNIGHTS = 3;
	static final int BISHOPS = 4;
	static final int ROOKS = 5;
	static final int QUEENS = 6;
	static final int KINGS = 7;

	public BB(Board board) {
		if (setMask[0] == 0) {
			initPresets();
		}
		for (int i = 0; i < board.locations.length; i++) {
			Piece p = board.locations[i];
			if (p.worth != 0) {
				setBitFromPiece(p);

			}
		}
	}

	static void initPresets() {
		for (int i = 0; i < 64; i++) {
			setMask[i] = (long) 1 << i;
			clearMask[i] = ~setMask[i];
			long kingAttack = setMask[i] | right(setMask[i]) | left(setMask[i]);
			kingAttack |= up(kingAttack) | down(kingAttack);
			kingAttacks[i] = kingAttack & clearMask[i];

			long l1 = left(setMask[i]);
			long l2 = left(left(setMask[i]));
			long r1 = right(setMask[i]);
			long r2 = right(right(setMask[i]));

			long h1 = l2 | r2;
			long h2 = l1 | r1;

			knightAttacks[i] = up(h1) | down(h1) | up(up(h2)) | down(down(h2));
		}
	}

	static int getBitBoard(String p) {
		int index = 8;
		if (p == "p") {
			index = 2;
		} else if (p == "n") {
			index = 3;
		} else if (p == "b") {
			index = 4;
		} else if (p == "r") {
			index = 5;
		} else if (p == "q") {
			index = 6;
		} else if (p == "k") {
			index = 7;
		}

		return index;
	}

	void setBitFromPiece(Piece p) {
		if (p.worth != 0) {
			if (p.player) {
				pieceBitBoards[WHITE] = setBit(pieceBitBoards[WHITE], p.position.getIndex());
			} else {
				pieceBitBoards[BLACK] = setBit(pieceBitBoards[BLACK], p.position.getIndex());
			}
			int pieceBoard = getBitBoard(p.symbol);
			pieceBitBoards[pieceBoard] = setBit(pieceBitBoards[pieceBoard], p.position.getIndex());
		}

	}

	static long setBit(long b, int index) {
		b |= setMask[index];
		return b;
	}

	static long clearBit(long b, int index) {
		b &= clearMask[index];
		return b;
	}

	static String toString(long bitboard) {
		String aString = "";
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < 8; x++) {
				if ((bitboard & ((long) 1 << (x + y * 8))) != 0) {
					aString += "X ";
				} else {
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
		for (int i = 0; i < 2; i++) {
			combinedBitboard |= pieceBitBoards[i];
		}
		return combinedBitboard;
	}

	long pawnPush(long pawns, boolean player) {
		long rank4;
		long pushBB = 0;
		if (player) {
			rank4 = RANK_1 << 24;
			pawns = up(pawns);
			pawns &= ~combine();
			pushBB |= pawns;
			pawns = up(pawns);
		} else {
			rank4 = RANK_1 << 32;
			pawns = down(pawns);
			pawns &= ~combine();
			pushBB |= pawns;
			pawns = down(pawns);
		}
		pushBB |= pawns & ~combine() & rank4;

		return (pushBB);
	}

	long pawnsAttack(long pawns, boolean player, long enPassant) {
		long rightAttack = 0;
		long leftAttack = 0;
		if (player) {
			rightAttack = upRight(pawns);
			leftAttack = upLeft(pawns);
		} else {
			rightAttack = downRight(pawns);
			leftAttack = downLeft(pawns);
		}
		long enemy;
		if (player) {
			enemy = pieceBitBoards[BLACK];
		} else {
			enemy = pieceBitBoards[WHITE];
		}
		enemy |= enPassant;
		long attack = (rightAttack | leftAttack) & enemy;
		return attack;
	}

	long rookAttack(long consideredPiecesBB, int from, boolean player) {
		long friendlyBB;
		if (player) {
			friendlyBB = pieceBitBoards[0];
		} else {
			friendlyBB = pieceBitBoards[1];
		}
		long bbBlockers = consideredPiecesBB & MagicBitboards.occupancyMaskRook[from];
		int databaseIndex = (int) (bbBlockers
				* MagicBitboards.magicNumberRook[from] >>> MagicBitboards.magicNumberShiftsRook[from]);
		long possibleMoves = MagicBitboards.magicMovesRook[from][databaseIndex];
		possibleMoves &= ~friendlyBB;

		return possibleMoves;
	}

	long bishopAttack(long consideredPiecesBB, int from, boolean player) {
		long friendlyBB;
		if (player) {
			friendlyBB = pieceBitBoards[0];
		} else {
			friendlyBB = pieceBitBoards[1];
		}
		long bbBlockers = consideredPiecesBB & MagicBitboards.occupancyMaskBishop[from];
		int databaseIndex = (int) (bbBlockers
				* MagicBitboards.magicNumberBishop[from] >>> MagicBitboards.magicNumberShiftsBishop[from]);
		long possibleMoves = MagicBitboards.magicMovesBishop[from][databaseIndex];
		possibleMoves &= ~friendlyBB;

		return possibleMoves;
	}

	long attacksTo(long consideredPiecesBB, int to, boolean player) {
		long knights, kings, bishopsQueens, rooksQueens, pawns;
		long enemyBB;
		if (player) {
			enemyBB = pieceBitBoards[1];
		} else {
			enemyBB = pieceBitBoards[0];
		}
		knights = knightAttacks[to] & pieceBitBoards[KNIGHTS];
		kings = kingAttacks[to] & pieceBitBoards[KINGS];
		bishopsQueens = bishopAttack(consideredPiecesBB, to, player)
				& (pieceBitBoards[BISHOPS] | pieceBitBoards[QUEENS]);
		rooksQueens = rookAttack(consideredPiecesBB, to, player) & (pieceBitBoards[ROOKS] | pieceBitBoards[QUEENS]);
		pawns = pawnsAttack(1L << to, player, 0) & pieceBitBoards[PAWNS];

		long allPieces = knights | kings | bishopsQueens | rooksQueens | pawns;
		return enemyBB & allPieces;
	}

	int getValue(int index) {
		long piece = 1L << index;
		if ((piece & pieceBitBoards[PAWNS]) != 0) {
			return Pawn.WORTH;
		} else if ((piece & pieceBitBoards[KNIGHTS]) != 0) {
			return Knight.WORTH;
		} else if ((piece & pieceBitBoards[BISHOPS]) != 0) {
			return Bishop.WORTH;
		} else if ((piece & pieceBitBoards[ROOKS]) != 0) {
			return Rook.WORTH;
		} else if ((piece & pieceBitBoards[QUEENS]) != 0) {
			return Queen.WORTH;
		} else if ((piece & pieceBitBoards[KINGS]) != 0) {
			return King.WORTH;
		}
		return 0;
	}

	int SEE(int to, int from, boolean player) {
		player = !player;
		int[] gain = new int[32];
		int d = 0;
		long occ = combine();
		long attacks = attacksTo(occ, to, player);
		long fromSet = 1L << from;
		if (fromSet == 0) {
			return 0;
		}
		gain[d] = getValue(to);
		while (fromSet != 0) {
			d++; // next depth and side
			gain[d] = getValue(bitScanForward(fromSet)) - gain[d - 1]; // speculative store, if defended
			if (Math.max(-gain[d - 1], gain[d]) < 0)
				break; // pruning does not influence the result
			occ ^= fromSet; // reset bit in temporary occupancy (for x-Rays)
			player = !player;
			attacks = attacksTo(occ, to, player);
			attacks &= occ;
			fromSet = getLeastValuablePiece(attacks);
		}
		while (--d != 0) {
			gain[d - 1] = -Math.max(-gain[d - 1], gain[d]);
		}
		return gain[0];
	}

	static int bitScanForward(long bb) {
		assert (bb != 0);

		double LS1B = (double) (bb & -bb);

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

	static int[] getSetBits(long bb) {

		int[] setBits = new int[countSetBits(bb)];

		int i = 0;
		while (bb != 0) {
			int index = BB.bitScanForward(bb);
			setBits[i] = index;
			bb = BB.clearBit(bb, index);

			if (bb == 0 && i != setBits.length - 1) {
				System.out.println("ERROR: getSetBits did not get index of all bits.");
			}
			i++;
		}

		return setBits;
	}

	long getLeastValuablePiece(long bb) {
		for (int piece = PAWNS; piece <= KINGS; piece += 1) {
			long subset = bb & pieceBitBoards[piece];
			if (subset != 0) {
				return subset & -subset; // single bit
			}
		}
		return 0; // empty set
	}

	// helper methods
	static long up(long bb) {
		return bb << 8;
	}

	static long down(long bb) {
		return bb >>> 8;
	}

	static long right(long bb) {
		return bb << 1 & ~FILE_A;
	}

	static long left(long bb) {
		return bb >> 1 & ~(FILE_A << 7);
	}

	static long upRight(long bb) {
		return up(right(bb));
	}

	static long upLeft(long bb) {
		return up(left(bb));
	}

	static long downRight(long bb) {
		return down(right(bb));
	}

	static long downLeft(long bb) {
		return down(left(bb));
	}
	
	static long upFill(long bb){
		bb |= (bb << 8);
		bb |= (bb << 16);
		bb |= (bb << 32);
		return bb;
	}
	
	static long downFill(long bb){
		bb |= (bb >>> 8);
		bb |= (bb >>> 16);
		bb |= (bb >>> 32);
		return bb;
	}
	
	static long fillColumn(long bb){
		return downFill(bb) | upFill(bb);
	}
}