package chessbot;

//import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;
import org.hamcrest.Matcher;


public class OpeningBook implements Runnable{

	final static int hashSize = 10000000;
	
	public static Hashtable<Integer, OpeningHashEntry> book = new Hashtable<Integer, OpeningHashEntry>(hashSize);
	
	static Map<String, String> ECO = new HashMap<String, String>();
	
	static Board board = null;
	
	static final int MAX_PLY = 20;
	static final int MIN_TIMES_USED = 3;
	
	static int clashes = 0;
	
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
		int index = Zobrist.getIndex(zobrist, hashSize);
		OpeningHashEntry oldEntry = book.get(index);
		if (oldEntry == null){
			book.put(index, entry);
		}else if (zobrist == oldEntry.zobrist){
			OpeningMove newMove = entry.moves.get(0); 
			if (oldEntry.moves.contains(newMove)){
				int i = oldEntry.moves.indexOf(newMove);
				oldEntry.moves.get(i).wins += newMove.wins;
				oldEntry.moves.get(i).timesUsed += newMove.timesUsed;
			}else{
				oldEntry.moves.addAll(entry.moves);
			}
		}else{
			//System.out.println("Opening book index clash!\n" + oldEntry + " vs. "+ entry);
			clashes++;
		}
	}
	
	static void compileOpenings(){
		long startTime = System.currentTimeMillis();
		for (String gameFile: gameBooks){
			System.out.println("Hashing "+ gameFile + " openings...");
			List<String> games = new ArrayList<String>();
			List<Map<String, String>> gameInfos = new ArrayList<Map<String, String>>();
			
			String filePath = new File("").getAbsolutePath() + "/gamebooks/" + gameFile + ".pgn";
			try{
				FileReader fr = new FileReader(filePath);
				BufferedReader textReader = new BufferedReader(fr);
				
				boolean midGame = true;
				while(true){
					String line = textReader.readLine();
					if (line == null){
						break;
					}
					if (line.startsWith("[")){
						Map<String, String> parameters;
						if (midGame){
							parameters = new HashMap<String, String>();
							gameInfos.add(parameters);
							midGame = false;
						}else{
							parameters = gameInfos.get(gameInfos.size()-1);
						}
						line.trim();
						line = line.substring(1, line.length()-1);
						String[] split = line.split(" ");
						split[1] = split[1].substring(1, split[1].length()-1);
						parameters.put(split[0], split[1]);
					}else if (line.length() != 0){
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
					}
				}
				textReader.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		
			for (int i = 0; i < games.size(); i++){
				String game = games.get(i);
				Map<String, String> parameters = gameInfos.get(i);
				String[] moves = game.split(" ");
				List<String> finalMoves = new ArrayList<String>();
				int moveCount = 3;
				for (String uneditedMove: moves){
					String[] editedMoves = uneditedMove.split(moveCount/2 +"\\.");
					for (String m: editedMoves){
						if (!m.equals("")){
							moveCount++;
							finalMoves.add(m);
						}
					}
				}
				convertAndExecuteMoves(finalMoves, parameters, 1);
			}
		}
		System.out.println("Done hashing openings in "+ (System.currentTimeMillis() - startTime)/1000.0 +"s");
		//System.out.println("Clashes: "+ clashes);
		//System.out.println("Entries: "+ book.size());
		//System.out.println("Clash percent: "+ clashes*100/(book.size()+clashes) +"%");
	}
	
	private static void convertAndExecuteMoves(List<String> moves, Map<String, String> info, int ply){
		if (moves.size() == 1 || ply == MAX_PLY){
			return;
		}
		long zobrist = board.currentZobrist;
		
		Move m = new Move(board, moves.get(0));
		boolean win = false;
		if ((info.get("Result").equals("1-0") && board.playerMove) || (info.get("Result").equals("0-1") && !board.playerMove)){
			win = true;
		}else if ((info.get("Result").equals("1-0") && !board.playerMove) || info.get("Result").equals("0-1") && board.playerMove){
			//loss
		}else{
			//tie
		}
		
		OpeningMove om = new OpeningMove(m, info.get("ECO"), win);
		
		OpeningHashEntry entry = new OpeningHashEntry(zobrist, om);
		addEntry(entry);
		
		moves.remove(0);
		m.execute();
		convertAndExecuteMoves(moves, info, ply+1);
		m.reverse();
	}
	
	public static OpeningMove getOpeningMove(Board b, String prefferedECO){
		long zobrist = b.currentZobrist;
		int index = Zobrist.getIndex(zobrist, hashSize);
		OpeningHashEntry entry = book.get(index);
		OpeningMove chosenMove = null;
		int totalMovesAvailbile = 0;
		if (entry != null && entry.zobrist == zobrist && entry.moves.size() != 0){
			List<OpeningMove> movesCopy = new ArrayList<OpeningMove>();
			movesCopy.addAll(entry.moves);
	        Collections.sort(movesCopy);
	        String openingMovesString = "";
	        for (OpeningMove om: movesCopy){
	        	openingMovesString += om + "(" + om.timesUsed +" used;" + om.wins*100/om.timesUsed +"% win); ";
	        	if (om.timesUsed >= MIN_TIMES_USED){
	        		if (om.ECO.equals(prefferedECO)){
		        		chosenMove = om;
		        	}
	        		totalMovesAvailbile+=om.timesUsed;
	        	}
	        }

	        if (totalMovesAvailbile > 0){
	        	System.out.println("Possible opening moves: " + openingMovesString);
	        	if (chosenMove == null){
					int randomMoveIndex = (int)(Math.random()*totalMovesAvailbile);
					int i = 0;
					for (OpeningMove om: movesCopy){
						if (om.timesUsed >= MIN_TIMES_USED){
							chosenMove = om;
							i += om.timesUsed;
						}
						if (i >= randomMoveIndex){
							break;
						}
					}
	        	}
	        }
		}
		return chosenMove;
	}
	
	public static void loadECO(){
		
		System.out.println("Loading ECO...");
		String filePath = new File("").getAbsolutePath() + "/ECO";
		try{
			FileReader fr = new FileReader(filePath);
			BufferedReader textReader = new BufferedReader(fr);
			
			Pattern k = Pattern.compile("([A-Z][0-9]{2})");
			Pattern e = Pattern.compile("[A-Z][0-9]{2}(.*)");
			
			
			while(true){
				String line = textReader.readLine();
				if (line == null){
					break;
				}
				
				String key = "";
				String info = "";
				
				java.util.regex.Matcher m = k.matcher(line);

				if (m.find()) {
					key = m.group(1).trim();
				}else{
					System.out.println("Error parsing ECO - Match not found");
					System.exit(0);
				}
				
				m = e.matcher(line);

				if (m.find()) {
					info = m.group(1).trim();
				}else{
					System.out.println("Error parsing ECO - Match not found");
					System.exit(0);
				}
				
				ECO.put(key, info);
				
			}
			textReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public static String getECOName(String input){
		input = input.substring(0, 3);
		return ECO.get(input);
	}

	@Override
	public void run() {
		loadECO();
		compileOpenings();
	}
}
