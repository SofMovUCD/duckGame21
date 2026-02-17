public class Board {
	
	public static int[][][] board = new int[21][21][2];
	private Player player1;
	private Player player2;

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
	
	public Player getPlayer(int num){
		if(num != 1 && num != 2){
			throw new IllegalArgumentException("Wrong player number entered");
		}
		return num == 1? player1: player2;
	}	
	
}
