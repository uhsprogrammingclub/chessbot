package chessbot;

import java.security.*;

import chessbot.Board.Side;

public class Zobrist {
	static long zArray[][][] = new long [2][6][64];
	static long zEnPassant[] = new long[8];
	static long zCastle[] = new long [4];
	static long zPlayerMove;
	
	public static long random64(){
		SecureRandom random = new SecureRandom();
		return random.nextLong();
	}
	
	public static void zobristFillArray(){
		
		for(int player = 0; player <2; player++){
			for(int pieceType = 0; pieceType < 6; pieceType++){
				for(int i = 0; i < 64; i++){
					zArray[player][pieceType][i] = random64();
				}
			}
		}
		
		for(int column = 0; column < 8; column++){
			zEnPassant[column] = random64();
		}
		
		for(int i = 0; i < 4; i++){
			zCastle[i] = random64();
		}
		
		zPlayerMove = random64();
	}
	
	public static int getIndex(long zHash, int hashSize){
		return (int)(zHash % hashSize);
	}
	
	public static long getZobristHash(Board b){
		long zKey = 0;
		for(int i = 0; i < 64; i++){
				
			//Computer Pawn
			if(b.locations[i].symbol == "p" && b.locations[i].side == Side.B){
				zKey ^= zArray[0][0][i];
			}
			//Player Pawn
			else if(b.locations[i].symbol == "p" && b.locations[i].side == Side.W){
				zKey ^= zArray[1][0][i];
			}
			//Computer Knight
			else if(b.locations[i].symbol == "n" && b.locations[i].side == Side.B){
				zKey ^= zArray[0][1][i];
			}
			//Player Knight
			else if(b.locations[i].symbol == "n" && b.locations[i].side == Side.W){
				zKey ^= zArray[1][1][i];
			}
			//Computer Bishop
			else if(b.locations[i].symbol == "b" && b.locations[i].side == Side.B){
				zKey ^= zArray[0][2][i];
			}
			//Player Bishop
			else if(b.locations[i].symbol == "b" && b.locations[i].side == Side.W){
				zKey ^= zArray[1][2][i];
			}
			//Computer Rook
			else if(b.locations[i].symbol == "r" && b.locations[i].side == Side.B){
				zKey ^= zArray[0][3][i];
			}
			//Player Rook
			else if(b.locations[i].symbol == "r" && b.locations[i].side == Side.W){
				zKey ^= zArray[1][3][i];
			}
			//Computer Queen
			else if(b.locations[i].symbol == "q" && b.locations[i].side == Side.B){
				zKey ^= zArray[0][4][i];
			}
			//Player Queen
			else if(b.locations[i].symbol == "q" && b.locations[i].side == Side.W){
				zKey ^= zArray[1][4][i];
			}
			//Computer King
			else if(b.locations[i].symbol == "k" && b.locations[i].side == Side.B){
				zKey ^= zArray[0][5][i];
			}
			//Player King
			else if(b.locations[i].symbol == "k" && b.locations[i].side == Side.W){
				zKey ^= zArray[1][5][i];
			}
		}
		if (b.sideMove == Side.W){
			zKey ^= zPlayerMove;
		}
		if((b.castleRights & b.WKCA) == 0){
			zKey ^= zCastle[0];
		}
		if((b.castleRights & b.WQCA) == 0){
			zKey ^= zCastle[1];
		}
		if((b.castleRights & b.BKCA) == 0){
			zKey ^= zCastle[2];
		}
		if((b.castleRights & b.BQCA) == 0){
			zKey ^= zCastle[3];
		}
		return zKey;
	}
	
	public static long getPawnZobristHash(Board b){
			
		long zKey = 0;
		for(int i = 0; i < 64; i++){
			//Computer Pawn
			if(b.locations[i].symbol == "p" && b.locations[i].side == Side.B){
				zKey ^= zArray[0][0][i];
			}
			//Player Pawn
			else if(b.locations[i].symbol == "p" && b.locations[i].side == Side.W){
				zKey ^= zArray[1][0][i];
			}
		}
		
		return zKey;
	
	}
}
