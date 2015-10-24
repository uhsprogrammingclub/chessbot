package chessbot;

import java.security.*;

public class Zobrist {
	static long zArray[][][][] = new long [2][6][8][8];
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
				for(int y = 0; y < 8; y++){
					for(int x = 0; x < 8; x++){
						zArray[player][pieceType][y][x] = random64();
					}
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
	
	public static int getIndex(long zHash){
		return (int)(zHash % TranspositionTable.hashSize);
	}
	
	public static long getZobristHash(Board b){
		long zKey = 0;
		for(int y = 0; y < 8; y++){
			for(int x = 0; x < 8; x++){
				
				//Computer Pawn
				if(b.locations[y][x].symbol == "p" && b.locations[y][x].player == false){
					zKey ^= zArray[0][0][x][y];
				}
				//Player Pawn
				else if(b.locations[y][x].symbol == "p" && b.locations[y][x].player == true){
					zKey ^= zArray[1][0][x][y];
				}
				//Computer Knight
				else if(b.locations[y][x].symbol == "n" && b.locations[y][x].player == false){
					zKey ^= zArray[0][1][x][y];
				}
				//Player Knight
				else if(b.locations[y][x].symbol == "n" && b.locations[y][x].player == true){
					zKey ^= zArray[1][1][x][y];
				}
				//Computer Bishop
				else if(b.locations[y][x].symbol == "b" && b.locations[y][x].player == false){
					zKey ^= zArray[0][2][x][y];
				}
				//Player Bishop
				else if(b.locations[y][x].symbol == "b" && b.locations[y][x].player == true){
					zKey ^= zArray[1][2][x][y];
				}
				//Computer Rook
				else if(b.locations[y][x].symbol == "r" && b.locations[y][x].player == false){
					zKey ^= zArray[0][3][x][y];
				}
				//Player Rook
				else if(b.locations[y][x].symbol == "r" && b.locations[y][x].player == true){
					zKey ^= zArray[1][3][x][y];
				}
				//Computer Queen
				else if(b.locations[y][x].symbol == "q" && b.locations[y][x].player == false){
					zKey ^= zArray[0][4][x][y];
				}
				//Player Queen
				else if(b.locations[y][x].symbol == "q" && b.locations[y][x].player == true){
					zKey ^= zArray[1][4][x][y];
				}
				//Computer King
				else if(b.locations[y][x].symbol == "k" && b.locations[y][x].player == false){
					zKey ^= zArray[0][5][x][y];
				}
				//Player King
				else if(b.locations[y][x].symbol == "k" && b.locations[y][x].player == true){
					zKey ^= zArray[1][5][x][y];
				}
			}
		}
		if (b.playerMove){
			zKey ^= zPlayerMove;
		}
		if(b.playerKSideCastle){
			zKey ^= zCastle[0];
		}
		if(b.playerQSideCastle){
			zKey ^= zCastle[1];
		}
		if(b.botKSideCastle){
			zKey ^= zCastle[2];
		}
		if(b.botQSideCastle){
			zKey ^= zCastle[3];
		}
		return zKey;
	}
}
