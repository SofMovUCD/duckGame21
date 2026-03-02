package org.movshovich;

import java.util.ArrayList;

public class Board {
	
	public static int[][][] board = new int[11][21][2];
	public static ArrayList<Player> plrList = new ArrayList<>();
	public static int currentPlayer = 1;
	public static int movingFlag;

	public static boolean checkWin(Board[][][] b){
		//go through all the entries on the top/left row/column
        //1 -> check white (check path containing -1)
        //-1 -> check black (check path containing 1)

        if(currentPlayer == 1){ //black check
            for(int i = 0; i < 11; i++){ //check top row for existence of black tiles
                if(board[0][i*2][0] == 1){ //top row has found a black octagon, should check if there is path to bottom
                    checkBlackWin(0, i*2);
                }
            }
        } else if (currentPlayer == -1) { //white check

        }
        return true;
	}

    //recursive function
    private static boolean checkBlackWin(int y, int x){
        if(y == 10){
            return true;
        }
        if(y > 11 && board[y+1][x][0] == 1){ //check above
            checkBlackWin(y+1, x);
        }
        if(x < 11 && board[y][x+2][0] == 1){ //check to the right ->
            checkBlackWin(y, x+2);
        }
        if(y < 11 && board[y-1][x][0] == 1){//check below
            checkBlackWin(y-1, x);
        }
        if(x > 0 && board[y][x+2][0] == 1){ //check to the left ->
            checkBlackWin(y, x+2);
        }
        //rhombus checks
        if(x > 0 && y < 11 && board[y][x-1][0] == 1 && board[y+1][x-2][0] == 1){ //bottom left
            checkBlackWin(y+1, x-2);
        }
        if(x < 11 && y < 11 && board[y][x+1][0] == 1 && board[y+1][x+2][0] == 1){ //bottom right
            checkBlackWin(y+1, x+2);
        }
        if(x  > 0 && y > 0 && board[y-1][x-1][0] == 1 && board[y+1][x-2][0] == 1){ //upper left
            checkBlackWin(y-1, x-2);
        }
        if(x < 11 && y > 0 && board[y-1][x+1][0] == 1 && board[y-1][x+2][0] == 1){ //upper right
            checkBlackWin(y-1, x+2);
        }
        return false;
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
	
	public static void main(String[] args) {
		BoardDraw.initBoard();
		plrList.add(new Player(1));
		plrList.add(new Player(-1));

		while (true) {
			//System.out.println("Game Start!");
			plrByID(currentPlayer).makeMove();
			//System.out.println("move Made");
		}
	}
}
