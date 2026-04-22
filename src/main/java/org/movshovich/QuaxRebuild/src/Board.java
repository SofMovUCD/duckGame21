package org.movshovich.QuaxRebuild.src;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int BOARD_X = 21;
    public static final int BOARD_Y = 11;

    static Tile[][] board = new Tile[BOARD_Y][BOARD_X];;

    public Board() {
        initBoard();
    }

    public Tile[][] getBoard() {
        return board;
    }

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

    public static String visualBoard() {
        StringBuilder sb = new StringBuilder();
        for (int c = 0; c < BOARD_Y; c++) {
            for (int r = 0; r < BOARD_X; r += 2) {
                if (board[c][r] != null) {
                    sb.append(getTile(r, c).getValue() + "   "); 
                } 
            }
            sb.append("\n");
            for (int r = 1; r < BOARD_X; r += 2) {
                if (board[c][r] != null) {
                    sb.append("  " + getTile(r, c).getValue() + " ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private void initBoard() {
        for (int c = 0; c < BOARD_Y; c++) {
            for (int r = 0; r < BOARD_X; r++) {
                if (c == 10 && r % 2 == 1) board[c][r] = null;
                else board[c][r] = new Tile(r, c, 0, 0); 
            }
        }
        initialWeights();
    }

    public static List<Tile> getNeighbours(Tile inputTile) {
        List<Tile> nList = new ArrayList<>();
        if (inputTile.getX() % 2 == 0) {
            int[] dx = {-2, -1, 0, 1, 2, 1, 0, -1};
            int[] dy = {0, -1, -1, -1, 0, -0, 1, 0};

            for (int i = 0; i < 8; ++i) {
                int neighbourX = inputTile.getX() + dx[i];
                int neighbourY = inputTile.getY() + dy[i];
                if (inBounds(neighbourX, neighbourY)) {
                    nList.add(board[neighbourY][neighbourX]);
                }
            }
        } else {
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
        //System.out.println(nList);
        return nList;
    }

    private static boolean inBounds(int x, int y) {
        return (x > -1 && x < 21) && (y > -1 && y < 11) && board[y][x] != null;
    }

    public static boolean checkWin(Player plr) {
        //System.out.println(plr.getPlayerId());
        List<Tile> visited = new ArrayList<>();
        boolean output = false;
        for (int i = 0; i < BOARD_X; i+= 2) {
            if (plr.getPlayerId() == ((Tile)Game.valueForID(getTile(i, 0), getTile(0, i/2), plr)).getValue()) {
            output = checkWinRecur(plr, (Tile)Game.valueForID(getTile(i, 0), getTile(0, i/2), plr), visited);
            if (output) break;
            }
        }
        //System.out.println(visualBoard());
        return output;
    }

    private static boolean checkWinRecur(Player plr, Tile curr, List<Tile> visited) {
        visited.add(curr);
        //System.out.println(curr.toString());
        for (Tile neighbour : getNeighbours(curr)) {
            if (plr.getPlayerId() == neighbour.getValue() && !visited.contains(neighbour)) {
                if ((Game.valueForID(neighbour.getY(), neighbour.getX(), plr))==(Game.valueForID(10, 20, plr))) return true;
                else {return checkWinRecur(plr, neighbour, visited);}
            }
        }
        return false;
    }

    public static Tile largestWeight(Tile start) {
        List<Tile> visited = new ArrayList<>();
        return largestWeightRecur(start, visited);
    }

    private static Tile largestWeightRecur(Tile tile, List<Tile> visited) {
        visited.add(tile);

        Tile max = tile;
        for (Tile neighbour : getNeighbours(tile)) {
            if (!visited.contains(neighbour) && neighbour != null) {
                max = max.largerWeight(largestWeightRecur(neighbour, visited));
            }
        }
        return max;
    }

    private static void initialWeights() {
        for (int i = 0; i < 21; i += 2) {
            board[10][i].setWeight(1);
        }
    }

    public static void piRuleWeight() {
        for (int i = 0; i < 21; i += 2) {
            board[10][i].setWeight(0);
            board[i/2][20].setWeight(1);
        }
    }
}
