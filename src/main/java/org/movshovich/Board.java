package org.movshovich;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class Board {
	
	public static int[][][] board = new int[11][21][3];
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
        /*
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
        return isWin;*/
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
        //octagon checks
        if(y > 0 && board[y-1][x][0] == turn){ //check above ^
            result = checkPlayerWin(y-1, x, turn);
        }
        if(x < 20 && board[y][x+2][0] == turn){ //check to the right →
            result = result || checkPlayerWin(y, x+2, turn);
        }
        if(y < 10 && board[y+1][x][0] == turn){//check below ↓
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
        //currentPlayer *= -1;
        BoardDraw.overlayPanel.remove(piRuleBut);
        BoardDraw.overlayPanel.repaint();
    }

//	public static void main(String[] args) {
//		BoardDraw.initBoard();
//		//plrList.add(new Player(1));
//		//plrList.add(new Player(-1));
//
//        initPiRuleButton();
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
//        /*
//
//		while (!checkWin()) {
//            if ((currentPlayer == -1) && whitefirst) {
//                //System.out.println("PI Rule: Enforced");
//                BoardDraw.overlayPanel.add(piRuleBut);
//                BoardDraw.overlayPanel.setComponentZOrder(piRuleBut, 0);
//                BoardDraw.overlayPanel.repaint();
//            }
//
//			plrByID(currentPlayer).makeMove();
//            try { Thread.sleep(10); } catch (Exception e) {}
//            //System.out.println(checkWin()? "Black won" : "Black not won");
//
//            if ((currentPlayer == 1) && whitefirst) {
//                whitefirst = false;
//                BoardDraw.overlayPanel.remove(piRuleBut);
//                //System.out.println("whitefirst: " + whitefirst);
//                BoardDraw.overlayPanel.repaint();
//            }
//		}*/
//	}

    public static void main(String[] args) {
        // 1. Initialize the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            BoardDraw.initBoard();
        });
        // 2. Setup Players
        plrList.add(new Bot(1));   // Bot is Black
        plrList.add(new Player(-1)); // Human is White

        initPiRuleButton();

        // 3. Start the Game Logic in a background thread
        new Thread(() -> {
            // Wait a second for the GUI to actually pop up
            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            while (true) {
                // Handle Pie Rule Visibility
                if ((currentPlayer == -1) && whitefirst && piRuleBut.getParent() == null) {
                    SwingUtilities.invokeLater(() -> {
                        BoardDraw.overlayPanel.add(piRuleBut);
                        BoardDraw.overlayPanel.setComponentZOrder(piRuleBut, 0);
                        BoardDraw.overlayPanel.repaint();
                    });
                }

                // Execute the move for the current player
                int playerWhoMoved = currentPlayer;
                plrByID(playerWhoMoved).makeMove();

                // Check for win
                if (checkWin(playerWhoMoved)) {
                    System.out.println("Game Over!");
                    break;
                }

                // ie Rule Cleanup if turn 1 is over
                if ((currentPlayer == 1) && whitefirst) {
                    whitefirst = false;
                    SwingUtilities.invokeLater(() -> {
                        BoardDraw.overlayPanel.remove(piRuleBut);
                        BoardDraw.overlayPanel.repaint();
                    });
                }

                // E. Small delay to prevent the CPU from redlining
                try { Thread.sleep(100); } catch (InterruptedException e) {}
            }
        }).start(); // Starts the background thread
    }

    //resetting the board since the board doesn't reset in one run so the value stays the same for different tests.
    public static void resetBoard() {
        // Reset the array to all zeros
        board = new int[11][21][3];
        // Reset player list
        plrList.clear();
        // Reset game state variables
        currentPlayer = 1;
        whitefirst = true;
        movingFlag = 0;
    }
}
