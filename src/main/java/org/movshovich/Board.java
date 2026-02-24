package org.movshovich;

import java.util.ArrayList;

public class Board {
	
	public static int[][][] board = new int[11][21][2];
	public static ArrayList<Player> plrList = new ArrayList<>();
	public static int currentPlayer = 1;
	public static int movingFlag;

	public Player checkWin(Board[][][] b){
		//check if win condition satisfied
		//initially check if black has won.
		//check if we can add all edges and then be able to 
		//traverse them to get from the bottom to the top.
		//construct a spanning tree
		//find and add all edges keeping track of how tall the tree gets
		return null;
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
