package org.movshovich;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

public class Board {
	
	public static int[][][] board = new int[11][21][3];
	public static ArrayList<Player> plrList = new ArrayList<>();
	public static int currentPlayer = 1;
	public static int movingFlag;
    public static boolean whitefirst = true;
    public static JButton piRuleBut = new JButton("pi Rule");

    /**
     * Check if a player has won
     * @return boolean value whether a player has won
     * */
	public static boolean checkWin(){
        boolean isWin = false;
        if(currentPlayer == -1){ //black check
            for(int i = 0; i < 11; i++){ //check top row for existence of black tiles
                if(board[0][i*2][0] == 1){ //top row has found a black octagon, should check if there is path to the bottom
                    System.out.println("found  black octagon at position: "+ i);
                    if(checkPlayerWin(0, i * 2, 1)){
                        return true;
                    }
                }
            }
        }
        else { //white check
            for(int i = 0; i < 11; i++){ //check left column for existence of white tiles
                if(board[i][0][0] == -1){ //left column has found a white octagon, should check if there is path to the right
                    System.out.println("found  white octagon at position: "+ i);
                    if(checkPlayerWin(i, 0, -1)){
                        return true;
                    }
                }
            }
        }
        return isWin;
	}

    /**
     * Return if selected player has won from given starting position
     * @return boolean value whether player has won
     * @param y a value where the white/black tile was found (along the y-axis)
     * @param x a value where the white/black tile was found (along the x-axis)
     * @param turn a value signifying whose turn it is (1 for black, -1 for white)
     * */
    private static boolean checkPlayerWin(int y, int x, int turn){
        System.out.println("("+y+", "+x+")");
        if(turn == 1 && y == 10){ //check win for black
            System.out.println("Black wins!");
            return true;
        }
        if(turn == -1 && x == 20) { //check win for white
            System.out.println("White wins!");
            return true;
        }
		if (board[y][x][2] == 1) { //already visited tile
			return false;
		}

		board[y][x][2] = 1;

        boolean result = false;
        //octagon checks
        if(y > 0 && board[y-1][x][0] == turn){ //check above ^
            result = checkPlayerWin(y-1, x, turn);
        }
        if(x < 21 && board[y][x+2][0] == turn){ //check to the right →
            result = result || checkPlayerWin(y, x+2, turn);
        }
        if(y < 11 && board[y+1][x][0] == turn){//check below ↓
            result = result || checkPlayerWin(y+1, x, turn);
        }
        if(x > 0 && board[y][x-2][0] == turn){ //check to the left ←
            result = result || checkPlayerWin(y, x-2, turn);
        }
        //rhombus checks
        if(x > 0 && y < 11 && board[y][x-1][0] == turn && board[y+1][x-2][0] == turn){ //bottom left
            result = result || checkPlayerWin(y+1, x-2, turn);
        }
        if(x < 21 && y < 11 && board[y][x+1][0] == turn && board[y+1][x+2][0] == turn){ //bottom right
            result = result || checkPlayerWin(y+1, x+2, turn);
        }
        if(x > 0 && y > 0 && board[y-1][x-1][0] == turn && board[y-1][x-2][0] == turn){ //upper left
            result = result || checkPlayerWin(y-1, x-2, turn);
        }
        if(x < 21 && y > 0 && board[y-1][x+1][0] == turn && board[y-1][x+2][0] == turn){ //upper right
            result = result || checkPlayerWin(y-1, x+2, turn);
        }
        board[y][x][2] = 0;
        return result;
    }

	public static Player plrByID(int ID) {
		for (Player plr : plrList) {
			if (plr.getPlayerId() == ID) {
				return plr;
			}
		}
		throw new IllegalArgumentException("No Players of this ID");
	}

    public static void piRule() {
        final int PIE_RULE_BUTTON_LENGTH = 500;
        final int PIE_RULE_BUTTON_HEIGHT = 50;
        piRuleBut.setBounds(0, 900, PIE_RULE_BUTTON_LENGTH, PIE_RULE_BUTTON_HEIGHT);
        piRuleBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Player plr: plrList) { //switch the player colours
                    plr.setPlayerId(plr.getPlayerId() * -1);
                }
                BoardDraw.overlayPanel.remove(piRuleBut); //remove the button
                whitefirst = false;
                movingFlag = 0;
            }
        });
        BoardDraw.overlayPanel.add(piRuleBut);
        BoardDraw.overlayPanel.repaint();
    }

	public static void main(String[] args) {
		BoardDraw.initBoard();
		plrList.add(new Player(1));
		plrList.add(new Player(-1));

		while (!checkWin()) {
            if ((currentPlayer == -1) && whitefirst) {
                piRule();
            }
			//System.out.println("Game Start!");
			plrByID(currentPlayer).makeMove();
			//System.out.println("move Made");
            if (whitefirst) {
                BoardDraw.overlayPanel.remove(piRuleBut);
                //whitefirst = false;
            }
		}
	}
}
