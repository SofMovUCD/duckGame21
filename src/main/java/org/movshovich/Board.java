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

	public static boolean checkWin(){
		//go through all the entries on the top/left row/column
        //1 -> check white (check path containing -1)
        //-1 -> check black (check path containing 1)
        boolean isWin = false;
        if(currentPlayer == -1){ //black check
            for(int i = 0; i < 11; i++){ //check top row for existence of black tiles
                if(board[0][i*2][0] == 1){ //top row has found a black octagon, should check if there is path to bottom
                    System.out.println("found  black octagon at position: "+ i);
                    if(checkBlackWin(0, i * 2)){
                        return true;
                    }
                }
            }
        }
        else { //white check
            return false;
        }
        return isWin;
	}

    //recursive function
    private static boolean checkBlackWin(int y, int x){
        System.out.println("("+y+", "+x +")");
        if(y == 10){
            return true;
        }
		if (board[y][x][2] == 1) {
			return false;
		}
		
		board[y][x][2] = 1;
		
        
        
        boolean result = false;
        if(y > 0 && board[y-1][x][0] == 1 ){ //check above ^
            result = result || checkBlackWin(y-1, x);
        }
        if(x < 21 && board[y][x+2][0] == 1){ //check to the right →
            result = result || checkBlackWin(y, x+2);
        }
        if(y < 11 && board[y+1][x][0] == 1){//check below ↓
            result = result || checkBlackWin(y+1, x);
        }
        if(x > 0 && board[y][x-2][0] == 1){ //check to the left ←
            result = result || checkBlackWin(y, x-2);
        }
        //rhombus checks
        if(x > 0 && y < 11 && board[y][x-1][0] == 1 && board[y+1][x-2][0] == 1){ //bottom left
            result = result || checkBlackWin(y+1, x-2);
        }
        if(x < 21 && y < 11 && board[y][x+1][0] == 1 && board[y+1][x+2][0] == 1){ //bottom right
            result = result || checkBlackWin(y+1, x+2);
        }
        if(x > 0 && y > 0 && board[y-1][x-1][0] == 1 && board[y-1][x-2][0] == 1){ //upper left
            result = result || checkBlackWin(y-1, x-2);
        }
        if(x < 21 && y > 0 && board[y-1][x+1][0] == 1 && board[y-1][x+2][0] == 1){ //upper right
            result = result || checkBlackWin(y-1, x+2);
        }
        board[y][x][2] = 0;
        return result;
    }

	public int[][][] getBoard(){
		return board;
	}

	public void setTile(int x, int y, int z, int val) {
		board[x][y][z] = val;
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
        piRuleBut.setBounds(0, 900, 500, 50);
        piRuleBut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Player plr: plrList) {
                    plr.setPlayerId(plr.getPlayerId() * -1);
                }
                BoardDraw.overlayPanel.remove(piRuleBut);
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
            System.out.println(checkWin()? "Black won" : "Black not won");
            if (whitefirst) {
                BoardDraw.overlayPanel.remove(piRuleBut);
                //whitefirst = false;
            }
		}
	}
}
