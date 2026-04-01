package org.movshovich;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class Board {

	public static int[][][] board = new int[BoardDraw.BOARD_Y][BoardDraw.BOARD_X][3];
	public static ArrayList<Player> plrList = new ArrayList<>();
    public static JButton[][] buttonGrid = new JButton[11][21];
	public static int currentPlayer = 1;
	public static int movingFlag;
    public static boolean whitefirst = true;
    public static JButton piRuleBut;

    /**
     * Check if a player has won
     * @return boolean value whether a player has won
     * */
	public static boolean checkWin(int player) {
        for (int r = 0; r < BoardDraw.BOARD_Y; r++) {
            for (int c = 0; c < BoardDraw.BOARD_X; c++) {
                board[r][c][2] = 0;
            }
        }
        if (player == 1) { // Check Black
            for (int i = 0; i < 11; i++) {
                if (board[0][i * 2][0] == 1) {
                    if (checkPlayerWin(0, i * 2, 1)) return true;
                }
            }
        } else { // Check White
            for (int i = 0; i < 11; i++) {
                if (board[i][0][0] == -1) {
                    if (checkPlayerWin(i, 0, -1)) return true;
                }
            }
        }
        return false;
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
            BoardDraw.nextMove.setText("BLACK WINS!!");
            System.out.println("Black wins!");
            return true;
        }
        if(turn == -1 && x == 20) { //check win for white
            BoardDraw.nextMove.setText("WHITE WINS!!");
            System.out.println("White wins!");
            return true;
        }
		if (board[y][x][2] == 1) { //already visited tile
			return false;
		}

		board[y][x][2] = 1;

        boolean result = false;
//        //octagon checks
//        if(y > 0 && board[y-1][x][0] == turn){ //check above ^
//            result = checkPlayerWin(y-1, x, turn);
//        }
//        if(x < (BoardDraw.BOARD_X - 1) && board[y][x+2][0] == turn){ //check to the right →
//            result = result || checkPlayerWin(y, x+2, turn);
//        }
//        if(y < (BoardDraw.BOARD_Y - 1) && board[y+1][x][0] == turn){//check below ↓
//            result = result || checkPlayerWin(y+1, x, turn);
//        }
//        if(x > 0 && board[y][x-2][0] == turn){ //check to the left ←
//            result = result || checkPlayerWin(y, x-2, turn);
//        }
//        //rhombus checks
//        if(x > 0 && y < (BoardDraw.BOARD_Y - 1) && board[y][x-1][0] == turn && board[y+1][x-2][0] == turn){ //bottom left
//            result = result || checkPlayerWin(y+1, x-2, turn);
//        }
//        if(x < BoardDraw.BOARD_X && y < BoardDraw.BOARD_Y && board[y][x+1][0] == turn && board[y+1][x+2][0] == turn){ //bottom right
//            result = result || checkPlayerWin(y+1, x+2, turn);
//        }
//        if(x > 0 && y > 0 && board[y-1][x-1][0] == turn && board[y-1][x-2][0] == turn){ //upper left
//            result = result || checkPlayerWin(y-1, x-2, turn);
//        }
//        if(x < BoardDraw.BOARD_X && y > 0 && board[y-1][x+1][0] == turn && board[y-1][x+2][0] == turn){ //upper right
//            result = result || checkPlayerWin(y-1, x+2, turn);
//        }
//        //board[y][x][2] = 0;
        // -octagon check
        // Up
        if (y > 0 && board[y - 1][x][0] == turn) {
            result = result || checkPlayerWin(y - 1, x, turn);
        }
        // Down
        if (y < BoardDraw.BOARD_Y - 1 && board[y + 1][x][0] == turn) {
            result = result || checkPlayerWin(y + 1, x, turn);
        }
        // Right (Octagons are at even X indices, jump by 2)
        if (x + 2 < BoardDraw.BOARD_X && board[y][x + 2][0] == turn) {
            result = result || checkPlayerWin(y, x + 2, turn);
        }
        // Left
        if (x - 2 >= 0 && board[y][x - 2][0] == turn) {
            result = result || checkPlayerWin(y, x - 2, turn);
        }

        //  rhombus check
        // Bottom Left
        if (x - 2 >= 0 && y + 1 < BoardDraw.BOARD_Y) {
            if (board[y][x - 1][0] == turn && board[y + 1][x - 2][0] == turn) {
                result = result || checkPlayerWin(y + 1, x - 2, turn);
            }
        }
        // Bottom Right
        if (x + 2 < BoardDraw.BOARD_X && y + 1 < BoardDraw.BOARD_Y) {
            if (board[y][x + 1][0] == turn && board[y + 1][x + 2][0] == turn) {
                result = result || checkPlayerWin(y + 1, x + 2, turn);
            }
        }
        // Upper Left
        if (x - 2 >= 0 && y - 1 >= 0) {
            if (board[y - 1][x - 1][0] == turn && board[y - 1][x - 2][0] == turn) {
                result = result || checkPlayerWin(y - 1, x - 2, turn);
            }
        }
        // Upper Right
        if (x + 2 < BoardDraw.BOARD_X && y - 1 >= 0) {
            if (board[y - 1][x + 1][0] == turn && board[y - 1][x + 2][0] == turn) {
                result = result || checkPlayerWin(y - 1, x + 2, turn);
            }
        }
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

    public static void initPiRuleButton() {
        piRuleBut = new JButton("Activate Pi Rule");
        piRuleBut.setName("Activate Pi Rule");
        piRuleBut.setBounds(50, 830, 150, 50);
        piRuleBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                piRule();
            }
        });
    }

    public static void piRule() {
        System.out.println("I am a PI Rule");
        for (Player plr: plrList) {
            plr.setPlayerId(plr.getPlayerId() * -1);
            plr.refreshPlayerColour();
        }

        movingFlag = 0;
        whitefirst = false;
        //change colours
        BoardDraw.bot.setForeground(new Color(45,45,45));
        BoardDraw.bot.setBackground(Color.WHITE);
        BoardDraw.player.setForeground(Color.WHITE);
        BoardDraw.player.setBackground(new Color(45,45,45));
        BoardDraw.overlayPanel.remove(piRuleBut);
        BoardDraw.overlayPanel.repaint();
    }

	public static void main(String[] args) {
		BoardDraw.initBoard();

		plrList.add(new Bot(1));
		plrList.add(new Player(-1));
        initPiRuleButton();
//        while (true) {
//            // 1. Show Pie Rule if needed
//            if ((currentPlayer == -1) && whitefirst && piRuleBut.getParent() == null) {
//                BoardDraw.overlayPanel.add(piRuleBut);
//                BoardDraw.overlayPanel.setComponentZOrder(piRuleBut, 0);
//                BoardDraw.overlayPanel.repaint();
//            }
//
//            // 2. get who is moving now
//            int playerWhoMoved = currentPlayer;
//            plrByID(playerWhoMoved).makeMove();
//
//            // 3. Check for win immediately using that player's ID
//            if (checkWin(playerWhoMoved)) {
//                // The checkPlayerWin method already sets the text to "BLACK WINS" etc.
//                BoardDraw.overlayPanel.repaint();
//                break; // Exit the game loop
//            }
//
//            try { Thread.sleep(10); } catch (Exception e) {}
//
//            // 4. Handle Pie Rule cleanup and turn swap
//            if ((currentPlayer == 1) && whitefirst) {
//                whitefirst = false;
//                BoardDraw.overlayPanel.remove(piRuleBut);
//                BoardDraw.overlayPanel.repaint();
//            }
//        }
        // 3. start thread
        new Thread(() -> {
            // UI to load
            try { Thread.sleep(1000); } catch (Exception e) {}

            while (true) {
                // Pie Rule Logic
                if ((currentPlayer == -1) && whitefirst && piRuleBut.getParent() == null) {
                    BoardDraw.overlayPanel.add(piRuleBut);
                    BoardDraw.overlayPanel.setComponentZOrder(piRuleBut, 0);
                    BoardDraw.overlayPanel.repaint();
                }

                // Execute Move
                int playerWhoMoved = currentPlayer;
                plrByID(playerWhoMoved).makeMove();

                // Win Check
                if (checkWin(playerWhoMoved)) {
                    BoardDraw.overlayPanel.repaint();
                    break;
                }

                try { Thread.sleep(50); } catch (Exception e) {}

                // Pie Rule Cleanup
                if ((currentPlayer == 1) && whitefirst) {
                    whitefirst = false;
                    BoardDraw.overlayPanel.remove(piRuleBut);
                    BoardDraw.overlayPanel.repaint();
                }
            }
        }).start(); //  starts the logic in the background

	}

    //resetting the board since the board doesn't reset in one run so the value stays the same for different tests.
    public static void resetBoard() {
        // Reset the array to all zeros
        board = new int[BoardDraw.BOARD_Y][BoardDraw.BOARD_X][3];
        // Reset player list
        plrList.clear();
        // Reset game state variables
        currentPlayer = 1;
        whitefirst = true;
        movingFlag = 0;
    }
}
