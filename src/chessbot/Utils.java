package chessbot;

import java.util.*;

import chessbot.Board.Side;

public class Utils {

	static List<Move> getVerticalMoves(Board b, Piece p) {
		List<Move> moves = new ArrayList<Move>();
		Point pos = p.position;

		// evaluate moves if it's a pawn
		if (p.symbol.equals("p")) {
			
			// get direction of the pawn
			int dir = p.side.evalFactor();;
			Point move = new Point(pos.x, pos.y + dir);

			if (move.squareExists()) {
				if (b.isEmptySquare(move)) {
					if(move.y == 0 || move.y == 7){
						moves.add(new Move(b, move, p, "q"));
						moves.add(new Move(b, move, p, "n"));
						moves.add(new Move(b, move, p, "r"));
						moves.add(new Move(b, move, p, "b"));
					}else{
						moves.add(new Move(b, move, p, null));
					}
					
					// check if pawn can move 2 squares.
					if (((p.side == Side.W) && pos.y == 1) || ((p.side == Side.B) && pos.y == 6)) {
						Point move2 = new Point(pos.x, pos.y + dir * 2);
						if (move2.squareExists() && b.isEmptySquare(move2)) {
							moves.add(new Move(b, move2, p, null));
						}
					}
				}
			}
		} else {
			// forward vertical moves
			for (int y = pos.y + 1; y < 8; y++) {
				Point move = new Point(pos.x, y);
				if (b.isEmptySquare(move)) {
					moves.add(new Move(b, move, p, null));
				} else {
					if (b.getSide(move) != p.side) {
						moves.add(new Move(b, move, p, null));
					}
					break;
				}
				if (p.symbol.equals("k"))
					break; // limit to one square if it's king
			}
			// backwards vertical moves
			for (int y = pos.y - 1; y >= 0; y--) {
				Point move = new Point(pos.x, y);
				if (b.isEmptySquare(move)) {
					moves.add(new Move(b, move, p, null));
				} else {
					if (b.getSide(move) != p.side) {
						moves.add(new Move(b, move, p, null));
					}
					break;
				}
				if (p.symbol.equals("k"))
					break; // limit to one square if it's king
			}
		}
		return moves;
	}

	static List<Move> getHorizontalMoves(Board b, Piece p) {
		List<Move> moves = new ArrayList<Move>();
		Point pos = p.position;

		// right horizontal moves
		for (int x = pos.x + 1; x < 8; x++) {
			Point move = new Point(x, pos.y);
			if (b.isEmptySquare(move)) {
				moves.add(new Move(b, move, p, null));
			} else {
				if (b.getSide(move) != p.side) {
					moves.add(new Move(b, move, p, null));
				}
				break;
			}
			if (p.symbol.equals("k"))
				break; // limit to one square if it's king
		}
		// left horizontal moves
		for (int x = pos.x - 1; x >= 0; x--) {
			Point move = new Point(x, pos.y);
			if (b.isEmptySquare(move)) {
				moves.add(new Move(b, move, p, null));
			} else {
				if (b.getSide(move) != p.side) {
					moves.add(new Move(b, move, p, null));
				}
				break;
			}
			if (p.symbol.equals("k"))
				break; // limit to one square if it's king
		}
		return moves;
	}

	static List<Move> getDiagonalMoves(Board b, Piece p) {
		List<Move> moves = new ArrayList<Move>();
		Point pos = p.position;

		// evaluate moves if it's a pawn
		if (p.symbol.equals("p")) {
			// get direction of the pawn
			int dir = p.side.evalFactor();;

			// check if spot diagonally to the right
			Point move = new Point(pos.x + 1, pos.y + dir);
			if (move.squareExists() && (!b.isEmptySquare(move) && b.getSide(move) != p.side|| move.equals(b.enPassantTarget))) {
				if(move.y == 0 || move.y == 7){
					moves.add(new Move(b, move, p, "q"));
					moves.add(new Move(b, move, p, "b"));
					moves.add(new Move(b, move, p, "r"));
					moves.add(new Move(b, move, p, "n"));
				}else{
					moves.add(new Move(b, move, p, null));
				}	
			}

			// check if spot diagonally to the left
			Point move2 = new Point(pos.x - 1, pos.y + dir);

			if (move2.squareExists() && (!b.isEmptySquare(move2) && b.getSide(move2) != p.side || move2.equals(b.enPassantTarget))) {
				if(move2.y == 0 || move2.y == 7){
					moves.add(new Move(b, move2, p, "q"));
					moves.add(new Move(b, move2, p, "n"));
					moves.add(new Move(b, move2, p, "r"));
					moves.add(new Move(b, move2, p, "b"));
				}else{
					moves.add(new Move(b, move2, p, null));
				}	
			}
		} else {
			// diagonal moves right forward
			for (int i = 1; i < 8; i++) {
				Point move = new Point(pos.x + i, pos.y + i);
				if (!move.squareExists())
					break;

				if (b.isEmptySquare(move)) {
					moves.add(new Move(b, move, p, null));
				} else {
					if (b.getSide(move) != p.side) {
						moves.add(new Move(b, move, p, null));
					}
					break;
				}

				if (p.symbol.equals("k"))
					break; // limit to one square if it's king
			}
			// diagonal moves left forward
			for (int i = 1; i < 8; i++) {
				Point move = new Point(pos.x - i, pos.y + i);
				if (!move.squareExists())
					break;

				if (b.isEmptySquare(move)) {
					moves.add(new Move(b, move, p, null));
				} else {
					if (b.getSide(move) != p.side) {
						moves.add(new Move(b, move, p, null));
					}
					break;
				}
				if (p.symbol.equals("k"))
					break; // limit to one square if it's king
			}
			// diagonal moves right backward
			for (int i = 1; i < 8; i++) {
				Point move = new Point(pos.x + i, pos.y - i);
				if (!move.squareExists())
					break;

				if (b.isEmptySquare(move)) {
					moves.add(new Move(b, move, p, null));
				} else {
					if (b.getSide(move) != p.side) {
						moves.add(new Move(b, move, p, null));
					}
					break;
				}
				if (p.symbol.equals("k"))
					break; // limit to one square if it's king
			}
			// diagonal moves left backward
			for (int i = 1; i < 8; i++) {
				Point move = new Point(pos.x - i, pos.y - i);
				if (!move.squareExists())
					break;

				if (b.isEmptySquare(move)) {
					moves.add(new Move(b, move, p, null));
				} else {
					if (b.getSide(move) != p.side) {
						moves.add(new Move(b, move, p, null));
					}
					break;
				}
				if (p.symbol.equals("k"))
					break; // limit to one square if it's king
			}
		}
		return moves;
	}
	
	static List<Move> getKnightMoves(Board b, Piece p) {
		List<Move> moves = new ArrayList<Move>();

		for (int i = -1; i <= 1; i = i + 2) {
			for (int j = -2; j <= 2; j = j + 4) {
				Point move = new Point(p.position.x + i, p.position.y + j);
				if (move.squareExists() && (b.getSide(move) != p.side || b.isEmptySquare(move))) {
					moves.add(new Move(b, move, p, null));
				}
				Point move2 = new Point(p.position.x + j, p.position.y + i);
				if (move2.squareExists() && (b.getSide(move2) != p.side || b.isEmptySquare(move2))) {
					moves.add(new Move(b, move2, p, null));
				}
			}
		}
		return moves;
	}
	
	static boolean isChecked(Board b, Piece king) {
		
		Point pos = king.position;
		Side si = king.side;
		
		//up the board
		for (int y = pos.y + 1; y < 8; y++) { 
			Point move = new Point(pos.x, y);
			if (!b.isEmptySquare(move)) {
				if (b.getSide(move) != si 
						&&(b.getPiece(move).symbol == "q" 
						|| b.getPiece(move).symbol == "r" 
						|| (b.getPiece(move).symbol == "k" && y == pos.y + 1))){
						return true; //if king queen or rook on opposing team.
				}else{
					break;
				}
			}
		}
		
		//down the board
		for (int y = pos.y - 1; y >= 0; y--) {
			Point move = new Point(pos.x, y);
			if (!b.isEmptySquare(move)) {
				if (b.getSide(move) != si 
						&&(b.getPiece(move).symbol == "q" 
						|| b.getPiece(move).symbol == "r" 
						|| (b.getPiece(move).symbol == "k" && y == pos.y - 1))){
						return true; //if king queen or rook on opposing team.
				}else{
					break;
				}
			}
		}
		
		// to the right
		for (int x = pos.x + 1; x < 8; x++) {
			Point move = new Point(x, pos.y);
			if (!b.isEmptySquare(move)) {
				if (b.getSide(move) != si 
						&&(b.getPiece(move).symbol == "q" 
						|| b.getPiece(move).symbol == "r" 
						|| (b.getPiece(move).symbol == "k" && x == pos.x + 1))){
						return true; //if king queen or rook on opposing team.
				}else{
					break;
				}
			}
		}
		
		// to the left
		for (int x = pos.x - 1; x >= 0; x--) {
			Point move = new Point(x, pos.y);
			if (!b.isEmptySquare(move)) {
				if (b.getSide(move) != si 
						&&(b.getPiece(move).symbol == "q" 
						|| b.getPiece(move).symbol == "r" 
						|| (b.getPiece(move).symbol == "k" && x == pos.x - 1))){
						return true; //if king queen or rook on opposing team.
				}else{
					break;
				}
			}
		}

		
		for (int i = 1; i < 8; i++) { //up the board, right
			Point move = new Point(pos.x + i, pos.y + i);
			if (!move.squareExists())
				break;

			if (!b.isEmptySquare(move)) {
				if (b.getSide(move) != si 
						&&(b.getPiece(move).symbol == "q" 
						|| b.getPiece(move).symbol == "b" 
						|| (b.getPiece(move).symbol == "k" && i == 1)
						|| (b.getPiece(move).symbol == "p" && i == 1 && (si == Side.W)))){
						return true; //if king queen or rook on opposing team.
				}else{
					break;
				}
			}
		}
		
		for (int i = 1; i < 8; i++) { //up the board, left
			Point move = new Point(pos.x - i, pos.y + i);
			if (!move.squareExists())
				break;

			if (!b.isEmptySquare(move)) {
				if (b.getSide(move) != si 
						&&(b.getPiece(move).symbol == "q" 
						|| b.getPiece(move).symbol == "b" 
						|| (b.getPiece(move).symbol == "k" && i == 1)
						|| (b.getPiece(move).symbol == "p" && i == 1 && (si == Side.W)))){
						return true; //if king queen or rook on opposing team.
				}else{
					break;
				}
			}
		}
		
		for (int i = 1; i < 8; i++) { //down the board, right
			Point move = new Point(pos.x + i, pos.y - i);
			if (!move.squareExists())
				break;

			if (!b.isEmptySquare(move)) {
				if (b.getSide(move) != si 
						&&(b.getPiece(move).symbol == "q" 
						|| b.getPiece(move).symbol == "b" 
						|| (b.getPiece(move).symbol == "k" && i == 1)
						|| (b.getPiece(move).symbol == "p" && i == 1 && (si == Side.B)))){
						return true; //if king queen or rook on opposing team.
				}else{
					break;
				}
			}
		}
		
		for (int i = 1; i < 8; i++) { //down the board, left
			Point move = new Point(pos.x - i, pos.y - i);
			if (!move.squareExists())
				break;

			if (!b.isEmptySquare(move)) {
				if (b.getSide(move) != si 
						&&(b.getPiece(move).symbol == "q" 
						|| b.getPiece(move).symbol == "b" 
						|| (b.getPiece(move).symbol == "k" && i == 1)
						|| (b.getPiece(move).symbol == "p" && i == 1 && (si == Side.B)))){
						return true; //if king queen or rook on opposing team.
				}else{
					break;
				}
			}
		}
		
		for (int i = -1; i <= 1; i = i + 2) {
			for (int j = -2; j <= 2; j = j + 4) {
				
				Point move = new Point(pos.x + i, pos.y + j);
				if (move.squareExists() && !b.isEmptySquare(move) && b.getSide(move) != si && b.getPiece(move).symbol == "n") {
					return true;
				}
				Point move2 = new Point(pos.x + j, pos.y + i);
				if (move2.squareExists() && !b.isEmptySquare(move2) && b.getSide(move2) != si && b.getPiece(move2).symbol == "n") {
					return true;
				}
			}
		}
		
		return false;
	}
	
	static Board boardFromFEN(String str){
		
		//Array list to hold all of the pieces objects
		List<Piece> list = new ArrayList<>();
		
		//Split the input string at all spaces and map that to an array
		String[] strSplit = str.split(" ");
		
		//Map array to correct variables
		String piecePlacement = strSplit[0];
		String sideToMove = strSplit[1];
		String castlingRights = strSplit[2];
		String enPassantTarget = strSplit[3];
		int halfMoveClock = Integer.parseInt(strSplit[4]);
		int fullMoveCounter = Integer.parseInt(strSplit[5]);
		
		//Read piece input
		String[] piecesSplitRows = piecePlacement.split("/");
		
		int y = 7;
		
		for(String s : piecesSplitRows){
			
			int x = 0;
			
			//Map the pieces to an individual array
			char[] piecesArray = s.toCharArray();
			
			for(char c : piecesArray){
				
				Piece p = null;
				
				//Check if the entry is numeric - signifies empty spaces
				if(Character.isDigit(c)){
					
					x += Character.getNumericValue(c);
					
				}else{
					
					if(Character.toUpperCase(c) == 'P'){
						p = new Pawn(x, y, Character.isUpperCase(c)? Side.W : Side.B);
					}
					else if(Character.toUpperCase(c) == 'B'){
						p = new Bishop(x, y, Character.isUpperCase(c)? Side.W : Side.B);
					}
					else if(Character.toUpperCase(c) == 'N'){
						p = new Knight(x, y, Character.isUpperCase(c)? Side.W : Side.B);
					}
					else if(Character.toUpperCase(c) == 'R'){
						p = new Rook(x, y, Character.isUpperCase(c)? Side.W : Side.B);
					}
					else if(Character.toUpperCase(c) == 'Q'){
						p = new Queen(x, y, Character.isUpperCase(c)? Side.W : Side.B);
					}
					else if(Character.toUpperCase(c) == 'K'){
						p = new King(x, y, Character.isUpperCase(c)? Side.W : Side.B);
					}
					
				}
				
				//Add the piece to the array, unless it is an empty space (null), increment only if it is a piece (avoid double increments)
				if(p != null){
					list.add(p);
					x++;
				}
		
			}
			
			y--;
	
		}
		
		//Assign the player to move
		Side firstSideToMove = sideToMove.equals("w") ? Side.W : Side.B;
		
		//Create the board
		Board b = new Board(list, firstSideToMove);
		
		//Default the castling rights to false
		b.castleRights = 0;
		
		//Map the castling rights to an individual array
		char[] rightsArray = castlingRights.toCharArray();
		
		for(char c : rightsArray){
			if(c == 'K'){
				b.castleRights |= b.WKCA;
			}
			else if(c == 'Q'){
				b.castleRights |= b.WQCA;
			}
			else if(c == 'k'){
				b.castleRights |= b.BKCA;
			}
			else if(c == 'q'){
				b.castleRights |= b.BKCA;
			}
		}
		
		if (!enPassantTarget.equals("-")){
			b.enPassantTarget = new Point(enPassantTarget);
		}
		
		b.halfMoveClock = halfMoveClock;
		b.fullMoveCounter = fullMoveCounter;
		
		return b;
	}
	
	static String boardToFEN(Board b){
		//String to ultimately return
		String FEN = "";
		
		//Adding the pieces to the FEN string
		
		for (int y = 7; y >= 0; y--){
			int adjEmpty = 0;
			for (int x = 0; x < 8; x++){
				Piece p = b.locations[x + y*8];
				
				//If the piece is not empty add it to the string
				if(p.worth != 0){
					
					if(adjEmpty > 0){
						FEN += Integer.toString(adjEmpty);
					}
					
					FEN += (p.side == Side.W) ? p.symbol.toUpperCase() : p.symbol;
					
					adjEmpty = 0;
					
				}else{
					adjEmpty++;
				}
			}
			if(adjEmpty > 0){
				FEN += Integer.toString(adjEmpty);
			}
			FEN += "/";
		}

		
		//Chop off the last slash from the piece placement
		FEN = FEN.substring(0, FEN.length()-1) + " ";
		
		//Adding side to move
		FEN += (b.sideMove == Side.W) ? "w" : "b";
		FEN += " ";
		
		//Adding castling abilities
		
		FEN += (b.castleRights & b.WKCA) == 0 ? "K" : "";
		FEN += (b.castleRights & b.WQCA) == 0 ? "Q" : "";
		FEN += (b.castleRights & b.BKCA) == 0 ? "k" : "";
		FEN += (b.castleRights & b.BQCA) == 0 ? "q" : "";
		if (b.castleRights == 0){
			FEN += "-";
		}
		FEN += " ";
		
		//This is where we will define what happens with En Passant when that's done
		if (b.enPassantTarget == null){
			FEN += "-";
		}else{
			FEN += b.enPassantTarget;	
		}
		FEN += " ";
		//Add the half move clock
		FEN += Integer.toString(b.halfMoveClock) + " ";
		
		//Add the full move counter
		FEN += Integer.toString(b.fullMoveCounter);
		
		return FEN;
	}
	
	static Map<String, String[]> edpGetInfo(String edp){
		
		//There might be a more elegant method here...
		
		String info = edp.split (" ", 5) [4];
		
		Map<String, String[]> infoMap = new HashMap<String, String[]>();

		for(String part : info.split(";")){
			
			part = part.trim();
			
			if(part.equals("")){
				continue;
			}

			String[] split = part.split("\\s+", 2);
			String[] arguments = split[1].split(" ");
			
			//Confirmation
			if(split.length != 2 || arguments.length < 1){
				System.out.println("edpGetInfo functions has EDP input which is not compatible!");
				System.exit(0);
			}
			
			infoMap.put(split[0], arguments);
		}
		
		/*for (Map.Entry<String, String[]> entry : infoMap.entrySet()) {
		       String key = entry.getKey().toString();;
		       String[] value = entry.getValue();
		       System.out.println("key: " + key + " value: " + Arrays.toString(value) );
		}*/
		
		return infoMap;
		
	}
	
	static String edpGetFEN(String edp){
		return edp.replace(edp.split (" ", 5) [4], "") + "0" + " 0";
	}
	
	
	

}
