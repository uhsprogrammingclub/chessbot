package chessbot;

//import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class OpeningBook implements Runnable{

	final static int hashSize = 100000;
	
	public static Hashtable<Integer, OpeningHashEntry> book = new Hashtable<Integer, OpeningHashEntry>(hashSize);
	
	static Board board = null;
	
	public OpeningBook(Board b) {
		board = b;
	}

	static String[] gameBooks = { 
			//"1610-1899",
			"A02",
			"Adams - OK",
			"Alekhine",
			"Anand - OK",
			//"B00",
			"B02",
			"B10 - ok",
			//"C11",
			//"C15",
			"C41 Philidor - OK",
			"C46",
			"C47_Four_Knights",
			//"C54",
			"C63",
			"entre 2 et 15 coups - OK",
			"Ivanchuk",
			"Karpov",
			"Kasparov - OK",
			"Kosteniuk",
			"Morozevich",
			"Shirov - OK",
			//"Topalov",
			"WCC"
			};
	
	public static void addEntry(OpeningHashEntry entry){
		if (entry == null) return;
		long zobrist = entry.zobrist;
		int index = Zobrist.getIndex(zobrist);
		OpeningHashEntry oldEntry = book.get(index);
		if (oldEntry == null){
			book.put(index, entry);
		}else if (zobrist == oldEntry.zobrist){
			oldEntry.moves.addAll(entry.moves);
		}	
	}
	
	static void compileOpenings(){
		for (String gameFile: gameBooks){
			System.out.println("Hashing "+ gameFile + " openings...");
			List<String> games = new ArrayList<String>();
			String filePath = new File("").getAbsolutePath() + "/gamebooks/" + gameFile + ".pgn";
			try{
				FileReader fr = new FileReader(filePath);
				BufferedReader textReader = new BufferedReader(fr);
				
				boolean midGame = false;
				while(true){
					String line = textReader.readLine();
					if (line == null){
						break;
					}
					if (line.length() != 0 && !line.startsWith("[", 0)){
						if (midGame){
							String space = "";
							String exitingLine = games.get(games.size()-1);
							if (!Character.isDigit(line.charAt(0)) && (Character.isDigit(exitingLine.charAt(exitingLine.length()-1)) || exitingLine.charAt(exitingLine.length()-1) == 'O' || exitingLine.charAt(exitingLine.length()-1) == '+') ){
								space = " ";
							}
							line = exitingLine + space + line;
							games.remove(games.size()-1);
						}
						games.add(line);
						midGame = true;
					}else{
						midGame = false;
					}
				}
				textReader.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			for (String game: games){
				String[] moves = game.split(" ");
				List<String> finalMoves = new ArrayList<String>();
				int moveCount = 3;
				for (int i = 0; i < moves.length; i++){
					String[] editedMoves = moves[i].split(moveCount/2 +"\\.");
					for (String m: editedMoves){
						if (!m.equals("")){
							moveCount++;
							finalMoves.add(m);
						}
					}
				}
				convertAndExecuteMoves(finalMoves, 1);
			}
		}
		System.out.println("Done hashing openings.");
	}
	
	private static void convertAndExecuteMoves(List<String> moves, int ply){
		if (moves.size() == 1 || ply == 20){
			return;
		}
		Move m = new Move(board, moves.get(0));
		long zobrist = Zobrist.getZobristHash(board);
		
		OpeningHashEntry entry =  new OpeningHashEntry(zobrist, m);
		addEntry(entry);
		
		moves.remove(0);
		m.execute();
		convertAndExecuteMoves(moves, ply+1);
		m.reverse();
	}
	
	public static Move getOpeningMove(Board b){
		long zobrist = Zobrist.getZobristHash(b);
		int index = Zobrist.getIndex(zobrist);
		OpeningHashEntry entry = book.get(index);
		Move chosenMove = null;
		if (entry != null && entry.zobrist == zobrist && entry.moves.size() != 0){
			List<MoveAndScore> noDuplicates = new ArrayList<MoveAndScore>();
			List<Move> entryCopy = new ArrayList<Move>();
			entryCopy.addAll(entry.moves);
			
			Iterator<Move> iterator = entryCopy.iterator();
	        while (iterator.hasNext())
	        {
	        	Move m = (Move) iterator.next();
	        	boolean added = false;
	            for (MoveAndScore ms: noDuplicates){
	            	if (m.equals(ms.move)){
	            		ms.score += 1;
	            		added = true;
	            	}
	            }
	            if (added == false){
	            	noDuplicates.add(new MoveAndScore(m, 1));
	            }
	        }
	        Collections.sort(noDuplicates);
	        String noDuplicatesString = "";
	        for (MoveAndScore ms: noDuplicates){
	        	noDuplicatesString += ms.move + " " + (int)ms.score*100/entryCopy.size() +"%; ";
            }

			System.out.println("Possible opening moves: " + noDuplicatesString);
			
			chosenMove = entry.moves.get((int)(Math.random()*entry.moves.size()));
		}
		return chosenMove;
	}

	@Override
	public void run() {
		compileOpenings();
	}
}
