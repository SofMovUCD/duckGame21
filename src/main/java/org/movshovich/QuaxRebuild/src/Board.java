package org.movshovich.QuaxRebuild.src;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the grid logic, neighbor discovery, and win condition checking.
 * Handles the dual graph coordinate system (Octagons vs Rhombuses).
 */
public class Board {
    public static final int BOARD_X = 21;
    public static final int BOARD_Y = 11;

    static Tile[][] board = new Tile[BOARD_Y][BOARD_X];;

    /** Constructs a new Board and populates all tiles with initial weights. */
    public Board() {
        initBoard();
    }

    /** Returns the raw 2D tile grid. Used in tests. */
    public Tile[][] getBoard() {
        return board;
    }

    /** Returns a specific Tile based on its X and Y logic coordinates */
    public static Tile getTile(int x, int y) {
        for (int c = 0; c < BOARD_Y; c++) {
            for (int r = 0; r < BOARD_X; r++) {
                if (board[c][r] != null){
                    if ((board[c][r].getX() == x) && (board[c][r].getY() == y)) return board[c][r];
                }
            }
        }
        return null;
    }

    /** Initializes the grid, skipping specific cells to create the Quax board shape */
    private void initBoard() {
        for (int c = 0; c < BOARD_Y; c++) {
            for (int r = 0; r < BOARD_X; r++) {
                if (c == 10 && r % 2 == 1) board[c][r] = null;
                else board[c][r] = new Tile(r, c);
            }
        }
        initialWeights();
    }

    /**
     * Returns a list of immediate neighbors for a tile.
     * Logic differs based on whether the tile is an Octagon (even X) or Rhombus (odd X).
     */
    public static List<Tile> getNeighbours(Tile inputTile) {
        List<Tile> nList = new ArrayList<>();
        if (inputTile.getX() % 2 == 0) { // Octagon neighbors
            int[] dx = {-2, -1,  0,  1, 2, 1, 0, -1};
            int[] dy = { 0, -1, -1, -1, 0, 0, 1,  0};

            for (int i = 0; i < 8; ++i) {
                int neighbourX = inputTile.getX() + dx[i];
                int neighbourY = inputTile.getY() + dy[i];
                if (inBounds(neighbourX, neighbourY)) {
                    nList.add(board[neighbourY][neighbourX]);
                }
            }
        } else { // Rhombus neighbors
            int[] dx = {-1, 1, 1, -1};
            int[] dy = {0, 0, 1, 1};

            for (int i = 0; i < 4; ++i) {
                int neighbourX = inputTile.getX() + dx[i];
                int neighbourY = inputTile.getY() + dy[i];
                if (inBounds(neighbourX, neighbourY)) {
                    nList.add(board[neighbourY][neighbourX]);
                }
            }
        }
        return nList;
    }

    /** Returns a wider range of neighbors used for the Bot's A* pathfinding */
    public static List<Tile> furthNeighbours(Tile inputTile) {
        List<Tile> nList = new ArrayList<>();
        if (inputTile.getX() % 2 == 0) {
            // Displacement arrays for Black (dxb) vs White (dxw)
            int[] dxb = {0, 2, -2, 1, -1, -2, 2, -2, -1,  1,  2,  0};
            int[] dyb = {1, 1,  1, 0,  0,  0, 0, -1, -1, -1, -1, -1};
            int[] dxw = {2, 2,  2, 1,  1,  0, 0, -2, -2, -1, -1, -2};
            int[] dyw = {0, 1, -1, 0, -1, -1, 1,  1, -1,  0, -1,  0};

            for (int i = 0; i < 12; ++i) {
                int neighbourX = inputTile.getX() + (int) Game.valueForID(dxb[i], dxw[i], Game.plrByID(Game.getCurrentPlayer()));
                int neighbourY = inputTile.getY() + (int) Game.valueForID(dyb[i], dyw[i], Game.plrByID(Game.getCurrentPlayer()));
                if (inBounds(neighbourX, neighbourY)) {
                    nList.add(board[neighbourY][neighbourX]);
                }
            }

            return nList;
        } else {return getNeighbours(inputTile);}
    }

    /** Returns true if (x,y) is within the board and not a null cell. */
    private static boolean inBounds(int x, int y) {
        return (x > -1 && x < 21) && (y > -1 && y < 11) && board[y][x] != null;
    }

    /**
     * Entry point for checking if the current player has won.
     * Uses Depth First Search (DFS) recursion to find a path to the opposite side.
     */
    public static boolean checkWin(Player plr) {
        List<Tile> visited = new ArrayList<>();
        boolean output = false;
        for (int i = 0; i < BOARD_X; i+= 2) {
            if (plr.getPlayerId() == ((Tile)Game.valueForID(getTile(i, 10), getTile(20, i/2), plr)).getValue()) {
            output = checkWinRecur(plr, (Tile)Game.valueForID(getTile(i, 10), getTile(20, i/2), plr), visited);
            if (output) break;
            }
        }
        return output;
    }

    /** Recursive helper for win checking DFS */
    private static boolean checkWinRecur(Player plr, Tile curr, List<Tile> visited) {
        visited.add(curr);
        boolean output = false;
        for (Tile neighbour : getNeighbours(curr)) { //check each neighbour
            if (plr.getPlayerId() == neighbour.getValue() && !visited.contains(neighbour)) { //if the nieghbour is the same value as the player and not visited
                // If we reached the target coordinate (0 on the relevant axis), win found
                if (((int)Game.valueForID(neighbour.getY(), neighbour.getX(), plr)) == 0) output = true;
                else {output = checkWinRecur(plr, neighbour, visited);}
                if (output) break;
            }
        }
        return output;
    }

    /** Finds the tile with the highest importance (weight) reachable from start */
    public static Tile largestWeight(Tile start) {
        List<Tile> visited = new ArrayList<>();
        return largestWeightRecur(start, visited);
    }

    /** DFS helper that propagates upward, returning the tile with the highest weight. */
    private static Tile largestWeightRecur(Tile tile, List<Tile> visited) {
        visited.add(tile);

        for (Tile neighbour : getNeighbours(tile)) {
            if (!visited.contains(neighbour) && neighbour != null && neighbour.getValue() == 0) {
                tile = tile.largerWeight(largestWeightRecur(neighbour, visited));
            }
        }
        return tile;
    }

    /** Sets weight=1 on all bottom row tiles (BLACK's goal tiles) at game start. */
    private static void initialWeights() {
        for (int i = 0; i < 21; i += 2) {
            board[10][i].setWeight(1);
        }
    }

/** Updates goal weights if the Pi Rule is active (swapping sides) */
    public static void piRuleWeight() {
        for (int i = 0; i < 21; i += 2) {
            board[10][i].setWeight(0);
            board[i/2][20].setWeight(1);
        }
    }
}
