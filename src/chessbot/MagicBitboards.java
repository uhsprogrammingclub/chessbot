package chessbot;

import java.util.Arrays;
import java.util.Random;

public class MagicBitboards {
	
	public static final long occupancyMaskRook[] = {
			0x101010101017eL, 0x202020202027cL, 0x404040404047aL, 0x8080808080876L, 0x1010101010106eL, 0x2020202020205eL, 0x4040404040403eL, 0x8080808080807eL, 0x1010101017e00L, 0x2020202027c00L, 0x4040404047a00L, 0x8080808087600L, 0x10101010106e00L, 0x20202020205e00L, 0x40404040403e00L, 0x80808080807e00L, 0x10101017e0100L, 0x20202027c0200L, 0x40404047a0400L, 0x8080808760800L, 0x101010106e1000L, 0x202020205e2000L, 0x404040403e4000L, 0x808080807e8000L, 0x101017e010100L, 0x202027c020200L, 0x404047a040400L, 0x8080876080800L, 0x1010106e101000L, 0x2020205e202000L, 0x4040403e404000L, 0x8080807e808000L, 0x1017e01010100L, 0x2027c02020200L, 0x4047a04040400L, 0x8087608080800L, 0x10106e10101000L, 0x20205e20202000L, 0x40403e40404000L, 0x80807e80808000L, 0x17e0101010100L, 0x27c0202020200L, 0x47a0404040400L, 0x8760808080800L, 0x106e1010101000L, 0x205e2020202000L, 0x403e4040404000L, 0x807e8080808000L, 0x7e010101010100L, 0x7c020202020200L, 0x7a040404040400L, 0x76080808080800L, 0x6e101010101000L, 0x5e202020202000L, 0x3e404040404000L, 0x7e808080808000L, 0x7e01010101010100L, 0x7c02020202020200L, 0x7a04040404040400L, 0x7608080808080800L, 0x6e10101010101000L, 0x5e20202020202000L, 0x3e40404040404000L, 0x7e80808080808000L 
    };
 
	public static final long occupancyMaskBishop[] = {
			0x40201008040200L, 0x402010080400L, 0x4020100a00L, 0x40221400L, 0x2442800L, 0x204085000L, 0x20408102000L, 0x2040810204000L, 0x20100804020000L, 0x40201008040000L, 0x4020100a0000L, 0x4022140000L, 0x244280000L, 0x20408500000L, 0x2040810200000L, 0x4081020400000L, 0x10080402000200L, 0x20100804000400L, 0x4020100a000a00L, 0x402214001400L, 0x24428002800L, 0x2040850005000L, 0x4081020002000L, 0x8102040004000L, 0x8040200020400L, 0x10080400040800L, 0x20100a000a1000L, 0x40221400142200L, 0x2442800284400L, 0x4085000500800L, 0x8102000201000L, 0x10204000402000L, 0x4020002040800L, 0x8040004081000L, 0x100a000a102000L, 0x22140014224000L, 0x44280028440200L, 0x8500050080400L, 0x10200020100800L, 0x20400040201000L, 0x2000204081000L, 0x4000408102000L, 0xa000a10204000L, 0x14001422400000L, 0x28002844020000L, 0x50005008040200L, 0x20002010080400L, 0x40004020100800L, 0x20408102000L, 0x40810204000L, 0xa1020400000L, 0x142240000000L, 0x284402000000L, 0x500804020000L, 0x201008040200L, 0x402010080400L, 0x2040810204000L, 0x4081020400000L, 0xa102040000000L, 0x14224000000000L, 0x28440200000000L, 0x50080402000000L, 0x20100804020000L, 0x40201008040200L     	
	};

	public static long magicNumberRook[] = {
			0xa180022080400230L, 0x40100040022000L, 0x80088020001002L, 0x80080280841000L, 0x4200042010460008L, 0x4800a0003040080L, 0x400110082041008L, 0x8000a041000880L, 0x10138001a080c010L, 0x804008200480L, 0x10011012000c0L, 0x22004128102200L, 0x200081201200cL, 0x202a001048460004L, 0x81000100420004L, 0x4000800380004500L, 0x208002904001L, 0x90004040026008L, 0x208808010002001L, 0x2002020020704940L, 0x8048010008110005L, 0x6820808004002200L, 0xa80040008023011L, 0xb1460000811044L, 0x4204400080008ea0L, 0xb002400180200184L, 0x2020200080100380L, 0x10080080100080L, 0x2204080080800400L, 0xa40080360080L, 0x2040604002810b1L, 0x8c218600004104L, 0x8180004000402000L, 0x488c402000401001L, 0x4018a00080801004L, 0x1230002105001008L, 0x8904800800800400L, 0x42000c42003810L, 0x8408110400b012L, 0x18086182000401L, 0x2240088020c28000L, 0x1001201040c004L, 0xa02008010420020L, 0x10003009010060L, 0x4008008008014L, 0x80020004008080L, 0x282020001008080L, 0x50000181204a0004L, 0x102042111804200L, 0x40002010004001c0L, 0x19220045508200L, 0x20030010060a900L, 0x8018028040080L, 0x88240002008080L, 0x10301802830400L, 0x332a4081140200L, 0x8080010a601241L, 0x1008010400021L, 0x4082001007241L, 0x211009001200509L, 0x8015001002441801L, 0x801000804000603L, 0xc0900220024a401L, 0x1000200608243L
    };
	
	public static long magicNumberBishop[] = {
			0x2910054208004104L, 0x2100630a7020180L, 0x5822022042000000L, 0x2ca804a100200020L, 0x204042200000900L, 0x2002121024000002L, 0x80404104202000e8L, 0x812a020205010840L, 0x8005181184080048L, 0x1001c20208010101L, 0x1001080204002100L, 0x1810080489021800L, 0x62040420010a00L, 0x5028043004300020L, 0xc0080a4402605002L, 0x8a00a0104220200L, 0x940000410821212L, 0x1808024a280210L, 0x40c0422080a0598L, 0x4228020082004050L, 0x200800400e00100L, 0x20b001230021040L, 0x90a0201900c00L, 0x4940120a0a0108L, 0x20208050a42180L, 0x1004804b280200L, 0x2048020024040010L, 0x102c04004010200L, 0x20408204c002010L, 0x2411100020080c1L, 0x102a008084042100L, 0x941030000a09846L, 0x244100800400200L, 0x4000901010080696L, 0x280404180020L, 0x800042008240100L, 0x220008400088020L, 0x4020182000904c9L, 0x23010400020600L, 0x41040020110302L, 0x412101004020818L, 0x8022080a09404208L, 0x1401210240484800L, 0x22244208010080L, 0x1105040104000210L, 0x2040088800c40081L, 0x8184810252000400L, 0x4004610041002200L, 0x40201a444400810L, 0x4611010802020008L, 0x80000b0401040402L, 0x20004821880a00L, 0x8200002022440100L, 0x9431801010068L, 0x1040c20806108040L, 0x804901403022a40L, 0x2400202602104000L, 0x208520209440204L, 0x40c000022013020L, 0x2000104000420600L, 0x400000260142410L, 0x800633408100500L, 0x2404080a1410L, 0x138200122002900L    
	};
	
	public static int magicNumberShiftsRook[] = {
	    52,53,53,53,53,53,53,52,53,54,54,54,54,54,54,53,
	    53,54,54,54,54,54,54,53,53,54,54,54,54,54,54,53,
	    53,54,54,54,54,54,54,53,53,54,54,54,54,54,54,53,
	    53,54,54,54,54,54,54,53,52,53,53,53,53,53,53,52
	};
	
	public static int magicNumberShiftsBishop[] = {
	    58,59,59,59,59,59,59,58,59,59,59,59,59,59,59,59,
	    59,59,57,57,57,57,59,59,59,59,57,55,55,57,59,59,
	    59,59,57,55,55,57,59,59,59,59,57,57,57,57,59,59,
	    59,59,59,59,59,59,59,59,58,59,59,59,59,59,59,58
	};
	
	public static long magicMovesRook[][] = new long[64][4096];
	public static long magicMovesBishop[][] = new long[64][4096];
	public static long occupancyVariation[][] = new long[64][4096];
	public static long occupancyAttackSet[][] = new long[64][4096];
	
	// Generate occupancy variations
	public static void generateOccupancyVariations(boolean isRook)
    {
        int i, j, bitRef;
        long mask;
        int variationCount;
        int[] setBitsInMask, setBitsInIndex;
 
        for (bitRef = 0; bitRef < 64; bitRef++)
        {
            mask = isRook ? occupancyMaskRook[bitRef] : occupancyMaskBishop[bitRef];
            setBitsInMask = BitBoard.getSetBits(mask);
            int bitCount = BitBoard.countSetBits(mask);
            variationCount = (int)(1L << bitCount);
            for (i=0; i<variationCount; i++)
            {
                occupancyVariation[bitRef][i] = 0; 
 
                // find bits set in index "i" and map them to bits in the 64 bit "occupancyVariation"
 
                setBitsInIndex = BitBoard.getSetBits(i); // an array of integers showing which bits are set
              
                for (j=0; j < setBitsInIndex.length; j++)
                {
                    occupancyVariation[bitRef][i] |= 
                    		(1L << 
                    				setBitsInMask[
                    				              setBitsInIndex[j]
                    				            		  ]
                    				            				  );
                }
 
                if (isRook)
                {
                	//add up moves to attack set
                    for (j=bitRef+8; j<=55 && (occupancyVariation[bitRef][i] & (1L << j)) == 0; j+=8){
                    	if (j>=0 && j<=63){
                    		occupancyAttackSet[bitRef][i] |= (1L << j);
                    	}
                    }
                    
                    //add down moves to attack set
                    for (j=bitRef-8; j>=8 && (occupancyVariation[bitRef][i] & (1L << j)) == 0; j-=8){
                    	if (j>=0 && j<=63) {
                    		occupancyAttackSet[bitRef][i] |= (1L << j);
                    	}
                    }
                   
                    //add right moves to attack set
                    for (j=bitRef+1; j%8!=7 && j%8!=0 && (occupancyVariation[bitRef][i] & (1L << j)) == 0; j++){
                    	if (j>=0 && j<=63){
                    		occupancyAttackSet[bitRef][i] |= (1L << j);
                    	}
                    }
                    
                    //add left moves to attack set
                    for (j=bitRef-1; j%8!=7 && j%8!=0 && j>=0 && (occupancyVariation[bitRef][i] & (1L << j)) == 0; j--){
                    	if (j>=0 && j<=63){
                        	occupancyAttackSet[bitRef][i] |= (1L << j);
                    	}
                    }
                
                    
                }
                else
                {
                	//add moves up right
                    for (j=bitRef+9; j%8!=7 && j%8!=0 && j<=55 && (occupancyVariation[bitRef][i] & (1L << j)) == 0; j+=9){
                    	if (j>=0 && j<=63){
                    		occupancyAttackSet[bitRef][i] |= (1L << j);
                    	}
                    }
                    
                    //add moves down left
                    for (j=bitRef-9; j%8!=7 && j%8!=0 && j>=8 && (occupancyVariation[bitRef][i] & (1L << j)) == 0; j-=9){
                    	 if (j>=0 && j<=63){
                    		 occupancyAttackSet[bitRef][i] |= (1L << j);
                    	 }
                    }
                   
                    //add moves up left
                    for (j=bitRef+7; j%8!=7 && j%8!=0 && j<=55 && (occupancyVariation[bitRef][i] & (1L << j)) == 0; j+=7){
                    	if (j>=0 && j<=63){
                    		occupancyAttackSet[bitRef][i] |= (1L << j);
                    	}
                    }
                    
                    //add moves down right
                    for (j=bitRef-7; j%8!=7 && j%8!=0 && j>=8 && (occupancyVariation[bitRef][i] & (1L << j)) == 0; j-=7){
                    	if (j>=0 && j<=63){
                    		occupancyAttackSet[bitRef][i] |= (1L << j);
                    	}
                    }
                }
            }
        }
    }
	
	// Generate magic numbers
	public static void generateMagicNumbers(boolean isRook)
    {
        int i, j, bitRef, variationCount;
 
        Random r = new Random();
        long magicNumber = 0;
        int index;
        long attackSet = 0;
 
        for (bitRef = 0; bitRef < 64; bitRef++)
        {
            int bitCount = BitBoard.countSetBits(isRook ? occupancyMaskRook[bitRef] : occupancyMaskBishop[bitRef]);
            variationCount = (int)(1L << bitCount);
            boolean fail;
            long usedBy[] = new long[(int)(1L << bitCount)];
 
            int attempts = 0;
 
            do
            {
                magicNumber = r.nextLong() & r.nextLong() & r.nextLong(); // generate a random number with not many bits set
                for (j=0; j<variationCount; j++) usedBy[j] = 0;
                attempts ++;
 
                for (i=0, fail=false; i<variationCount && !fail; i++)
                {
                    index = (int)((occupancyVariation[bitRef][i] * magicNumber) >>> (64-bitCount));
 
                    // fail if this index is used by an attack set that is incorrect for this occupancy variation
                    fail = usedBy[index] != 0 && usedBy[index] != occupancyAttackSet[bitRef][i];
 
                    usedBy[index] = attackSet;
                }
            } 
            while (fail);
 
            if (isRook)
            {
                magicNumberRook[bitRef] = magicNumber;
                magicNumberShiftsRook[bitRef] = (64-bitCount);
            }
            else
            {
                magicNumberBishop[bitRef] = magicNumber;
                magicNumberShiftsBishop[bitRef] = (64-bitCount);
            }
        }
    }
	
	//Generate magic moves
	public static void generateMoveDatabase(boolean isRook)
    {
        long validMoves;
        int variations, bitCount;
        int bitRef, i, j, magicIndex;
 
        for (bitRef = 0; bitRef < 64; bitRef++)
        {
            bitCount = isRook ? BitBoard.countSetBits(occupancyMaskRook[bitRef]) : BitBoard.countSetBits(occupancyMaskBishop[bitRef]);
            variations = (int)(1L << bitCount);
 
            for (i = 0; i < variations; i++)
            {
                validMoves = 0;
                if (isRook)
                {
                    magicIndex = (int)((occupancyVariation[bitRef][i] * magicNumberRook[bitRef]) >>> magicNumberShiftsRook[bitRef]);
 
                    //add Moves up
                    for (j=bitRef+8; j<=63; j+=8) {
                    	validMoves |= (1L << j);
                    	if ((occupancyVariation[bitRef][i] & (1L << j)) != 0){
                    		break; 
                    	}
                    }
                    //add Moves down
                    for (j=bitRef-8; j>=0; j-=8){
                    	validMoves |= (1L << j);
                    	if ((occupancyVariation[bitRef][i] & (1L << j)) != 0){
                    		break;
                    	}
                    }
                    
                    //add Moves right
                    for (j=bitRef+1; j%8!=0; j++) {
                    	validMoves |= (1L << j);
                    	if ((occupancyVariation[bitRef][i] & (1L << j)) != 0){
                    		break;
                    	}
                    }
                    
                    //add moves left
                    for (j=bitRef-1; j%8!=7 && j>=0; j--) {
                    	validMoves |= (1L << j); 
                    	if ((occupancyVariation[bitRef][i] & (1L << j)) != 0){
                    		break;
                    	}
                    }
 
                    magicMovesRook[bitRef][magicIndex] = validMoves;
                }
                else
                {
                    magicIndex = (int)((occupancyVariation[bitRef][i] * magicNumberBishop[bitRef]) >>> magicNumberShiftsBishop[bitRef]);
 
                    //add moves up right
                    for (j=bitRef+9; j%8!=0 && j<=63; j+=9) {
                    	validMoves |= (1L << j);
                    	if ((occupancyVariation[bitRef][i] & (1L << j)) != 0){
                    		break;
                    	}
                    }
                    
                    //add moves down left
                    for (j=bitRef-9; j%8!=7 && j>=0; j-=9) {
                    	validMoves |= (1L << j);
                    	if ((occupancyVariation[bitRef][i] & (1L << j)) != 0){
                    		break;
                    	}
                    }
                    
                    //add moves up left
                    for (j=bitRef+7; j%8!=7 && j<=63; j+=7) { 
                        validMoves |= (1L << j); 
                        if ((occupancyVariation[bitRef][i] & (1L << j)) != 0) 
                            break; 
                    }
                    
                    //add moves down right
                    for (j=bitRef-7; j%8!=0 && j>=0; j-=7) { 
                        validMoves |= (1L << j); 
                        if ((occupancyVariation[bitRef][i] & (1L << j)) != 0) 
                            break; 
                    }
 
                    magicMovesBishop[bitRef][magicIndex] = validMoves;
                }
            }
        }
    }
	
    
	
	

}
