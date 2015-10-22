package chessbot;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class GridLayoutManager extends JFrame{
	
	/**
	 * 
	 */

	private Container contents;
	
	//Components
	private JButton[][] squares = new JButton[8][8];

	//Colors
	private Color colorBlack = Color.DARK_GRAY;
	
	//Variable that says whether the board is active for the player
	private static boolean active = true;
	
	//Activate or deactivate the board
    public static void setActive(boolean active){
        GridLayoutManager.active = active;
    }
	
	//Images:
	private ImageIcon BPawn = new ImageIcon("src/ChessPieces/BPawn.png");
	private ImageIcon BBishop = new ImageIcon("src/ChessPieces/BBishop.png");
	private ImageIcon BKnight = new ImageIcon("src/ChessPieces/BKnight.png");
	private ImageIcon BRook = new ImageIcon("src/ChessPieces/BRook.png");
	private ImageIcon BQueen = new ImageIcon("src/ChessPieces/BQueen.png");
	private ImageIcon BKing = new ImageIcon("src/ChessPieces/BKing.png");
	private ImageIcon WPawn = new ImageIcon("src/ChessPieces/WPawn.png");
	private ImageIcon WBishop = new ImageIcon("src/ChessPieces/WBishop.png");
	private ImageIcon WKnight = new ImageIcon("src/ChessPieces/WKnight.png");
	private ImageIcon WRook = new ImageIcon("src/ChessPieces/WRook.png");
	private ImageIcon WQueen = new ImageIcon("src/ChessPieces/WQueen.png");
	private ImageIcon WKing = new ImageIcon("src/ChessPieces/WKing.png");

	public GridLayoutManager(){
		super("");
		
		contents = getContentPane();
		contents.setLayout(new GridLayout(8,8));
		
		//Create event handlers:
		ButtonHandler buttonHandler = new ButtonHandler();
		
		//Create and add board components:
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				squares[i][j] = new JButton();
				squares[i][j].setBorder( new LineBorder(Color.WHITE) );
				if((i + j) % 2 != 0){
					
					squares[i][j].setOpaque(true);
					squares[i][j].setBackground(colorBlack);
					squares[i][j].setBorder( new LineBorder(colorBlack) );
					
				}
				contents.add(squares[i][j]);
				squares[i][j].addActionListener(buttonHandler);
			}
		}
		
		//Size and display window:
		setSize(500, 500);
		setResizable(false);
		setLocationRelativeTo(null); //Centers window
		setVisible(true);
	}
	
	public void updateBoard(Board b){
		
		for(int i = 0; i <8 ; i++){
			for(int j = 0; j < 8; j++){
				
				Piece p = b.locations[i][j];
				
				//Flip the board
				int newJ = 7-j;
				int newI = i;
				
				if(!p.player){
					
					switch(p.symbol){
					case "-": squares[newJ][newI].setIcon(null);
							break;
					case "p": squares[newJ][newI].setIcon(BPawn);
							break;
					case "b": squares[newJ][newI].setIcon(BBishop);
							break;
					case "n": squares[newJ][newI].setIcon(BKnight);
							break;
					case "r": squares[newJ][newI].setIcon(BRook);
							break;
					case "q": squares[newJ][newI].setIcon(BQueen);
							break;
					case "k": squares[newJ][newI].setIcon(BKing);
							break;
					}
					
				}else{
					
					switch(p.symbol){
					case "-": squares[newJ][newI].setIcon(null);
							break;
					case "p": squares[newJ][newI].setIcon(WPawn);
							break;
					case "b": squares[newJ][newI].setIcon(WBishop);
							break;
					case "n": squares[newJ][newI].setIcon(WKnight);
							break;
					case "r": squares[newJ][newI].setIcon(WRook);
							break;
					case "q": squares[newJ][newI].setIcon(WQueen);
							break;
					case "k": squares[newJ][newI].setIcon(WKing);
							break;
					}		
				}	
			}
		}
	}
	
	private class ButtonHandler implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			
			//If the board is active...
			if(active){
				
				Object source = e.getSource();
				
				for(int i = 0; i < 8; i++){
					for(int j = 0; j <8; j++){
						if(source == squares[i][j]){
							
							if(Game.squareFrom == null){
								
								Game.squareFrom = new Point(j,7-i);
							
							}else{
								
								Game.squareTo = new Point(j,7-i);
						
							}
							
							return;
						}
					}
				}
			}
		}
	}
}


