package org.movshovich;

import javax.swing.JButton;

public class Bot extends Player{

    public Bot(int playerId) {
        super(playerId);
    }

    private int col = 10; // Our preferred vertical lane
    private int row = 5; //used if colour changed to white (pi rule)
    private int step = 1; //distance between octagons in array (1 for black, 2 for white)

    @Override
    public void makeMove() {
        //A* algorithm?
        JButton target = null;

        if(getPlayerId() == 1) { //BLACK
            // 1. Find our "lowest" currently placed Octagon in this column
            int lowestRow = -1;
            for (int r = 10; r >= 0; r--) {
                if (Board.board[r][col][0] == this.getPlayerId()) {
                    lowestRow = r;
                    break;
                }
            }

            // 2. If we or pi rule haven't started yet, take the top-center Octagon
            if (lowestRow == -1) {
                if (Board.board[0][col][0] == 0) {
                    target = Board.buttonGrid[0][col];
                } else {
                    target = findAnyAvailableOctagonBlack();
                }
            } else if (lowestRow < 10) {
                // Try to go directly down (Octagon)
                if (Board.board[lowestRow + 1][col][0] == 0) {
                    target = Board.buttonGrid[lowestRow + 1][col];
                }
                //Try below right rhombus (if exists)
                else if (col < 19 && Board.board[lowestRow][col + 1][0] == getPlayerId() && Board.board[lowestRow + 1][col + 2][0] == 0) {
                    //try to the right
                    System.out.println("checking right rhombus");
                    target = Board.buttonGrid[lowestRow + 1][col + 2];
                    col += 2;

                }
                //Try below left rhombus (if exists)
                else if (col > 1 && Board.board[lowestRow][col - 1][0] == getPlayerId() && Board.board[lowestRow + 1][col - 2][0] == 0) {
                    //try to the right
                    target = Board.buttonGrid[lowestRow + 1][col - 2];
                    col -= 2;

                }
                //fully blocked Try Rhombus to maneuver around
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
        }
        else{ //WHITE
            // 1. Find our "rightmost" currently placed Octagon in this row
            int RightmostColumn = -1;
            for (int c = 20; c >= 0; c-=2) {
                if (Board.board[row][c][0] == this.getPlayerId()) {
                    RightmostColumn = c;
                    break;
                }
            }

            // 2. If we or pi rule haven't started yet, take the left-center Octagon
            if (RightmostColumn == -1) {
                if (Board.board[row][0][0] == 0) {
                    System.out.println("placing white octagon in center");
                    target = Board.buttonGrid[row][0];
                } else {
                    target = findAnyAvailableOctagonWhite();
                }
            } else if (RightmostColumn < 21) {
                // Try to go directly right (Octagon)
                if (Board.board[row][RightmostColumn + 2][0] == 0) {
                    target = Board.buttonGrid[row][RightmostColumn + 2];
                }
                //Try below lower rhombus (if exists)
                else if (row < 10 && Board.board[row][RightmostColumn + 1][0] == getPlayerId() && Board.board[row+1][RightmostColumn + 2][0] == 0) {
                    //try to the right
                    System.out.println("checking right rhombus");
                    target = Board.buttonGrid[row+1][RightmostColumn + 2];
                    row += 1;

                }
                //Try upper rhombus (if exists)
                else if (row > 1 && Board.board[row - 1][RightmostColumn + 1][0] == getPlayerId() && Board.board[row - 1][RightmostColumn + 2][0] == 0) {
                    //try to the right
                    target = Board.buttonGrid[row - 1][RightmostColumn + 2];
                    row -= 1;

                }
                //fully blocked Try Rhombus to maneuver around
                else {
                    // Try Rhombus Lower (leads to row)
                    if (row != 10 && Board.board[row][RightmostColumn + 1][0] == 0) {
                        target = Board.buttonGrid[row][RightmostColumn + 1];
                    }
                    // Try Rhombus Above (leads to row + 1)
                    else if (row != 0 && Board.board[row - 1][RightmostColumn + 1][0] == 0) {
                        target = Board.buttonGrid[row - 1][RightmostColumn + 1];
                    }
                }
            }
        }

        // 5. fallback: If strategy fails, find any empty Octagon
        if (target == null /*|| target.getParent() == null*/) {
            target = getPlayerId() == 1? findAnyAvailableOctagonBlack() : findAnyAvailableOctagonWhite() ;
        }

        // Execute
        if (target != null) {
            try { Thread.sleep(400); } catch (Exception e) {} //sleep for no reason
            target.doClick();
        }/*else { //will never happen
            System.out.println("Bot has no moves left!");
            // Force a turn swap if trapped to prevent infinite loop
            Board.currentPlayer *= -1;
        }*/
    }

    private JButton findAnyAvailableOctagonBlack() {
        for (int r = 0; r < BoardDraw.BOARD_Y; r++) {
            for (int c = 0; c < BoardDraw.BOARD_X; c++) {
                if (Board.board[r][c][0] == 0 && Board.buttonGrid[r][c].getParent() != null) {
                    col = c;
                    return Board.buttonGrid[r][c];
                }
            }
        }
        return null;
    }

    private JButton findAnyAvailableOctagonWhite() {
        for (int c = 0; c < BoardDraw.BOARD_X; c++) {
            for (int r = 0; r < BoardDraw.BOARD_Y; r++) {
                if (Board.board[r][c][0] == 0 && Board.buttonGrid[r][c].getParent() != null) {
                    row = c;
                    return Board.buttonGrid[r][c];
                }
            }
        }
        return null;
    }

}
