package org.movshovich;

import java.awt.*;
import javax.swing.JButton;

public class Bot extends Player{
    public Bot(int playerId) {
        super(playerId);
    }

    @Override
    public void makeMove() {
        //A* algorithm?
        JButton target = null;
        int col = 10; // Our preferred vertical lane

        // 1. Find our "lowest" currently placed Octagon in this column
        int lowestRow = -1;
        for (int r = 10; r >= 0; r--) {
            if (Board.board[r][col][0] == this.getPlayerId()) {
                lowestRow = r;
                break;
            }
        }

        // 2. If we haven't started yet, take the top-center Octagon
        if (lowestRow == -1) {
            target = Board.buttonGrid[0][col];
        }
        else if (lowestRow < 10) {
            // Try to go directly down (Octagon)
            if (Board.board[lowestRow + 1][col][0] == 0) {
                target = Board.buttonGrid[lowestRow + 1][col];
            }
            // blocked Try Rhombus to maneuver around
            else {
                // Try Rhombus Right (leads to col + 2)
                if (col + 1 < 21 && Board.board[lowestRow][col + 1][0] == 0) {
                    target = Board.buttonGrid[lowestRow][col + 1];
                }
                // Try Rhombus Left (leads to col - 2)
                else if (col - 1 >= 0 && Board.board[lowestRow][col - 1][0] == 0) {
                    target = Board.buttonGrid[lowestRow][col - 1];
                }
            }
        }

        // 5. fallback: If strategy fails, find any empty Octagon
        if (target == null || target.getParent() == null) {
            target = findAnyAvailableOctagon();
        }

        // Execute
        if (target != null) {
            try { Thread.sleep(400); } catch (Exception e) {}
            target.doClick();
        }else {
            System.out.println("Bot has no moves left!");
            // Force a turn swap if trapped to prevent infinite loop
            Board.currentPlayer *= -1;
        }
    }

    private JButton findAnyAvailableOctagon() {
        for (int r = 0; r < BoardDraw.BOARD_Y; r++) {
            for (int c = 0; c < BoardDraw.BOARD_X; c ++) {
                if (Board.board[r][c][0] == 0 && Board.buttonGrid[r][c].getParent() != null) {
                    return Board.buttonGrid[r][c];
                }
            }
        }
        return null;
    }

}
