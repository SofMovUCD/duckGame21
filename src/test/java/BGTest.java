import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import org.assertj.swing.edt.GuiActionRunner;
import org.junit.*;
//import org.junit.jupiter.api;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.movshovich.QuaxRebuild.src.Board;
import org.movshovich.QuaxRebuild.src.Bot;
import org.movshovich.QuaxRebuild.src.DrawBoard;
import org.movshovich.QuaxRebuild.src.Game;
import org.movshovich.QuaxRebuild.src.Player;
import org.movshovich.QuaxRebuild.src.Tile;

import static org.junit.jupiter.api.Assertions.*;

public class BGTest {

    @BeforeAll
    public static void setUp() {
        GuiActionRunner.execute(Game::new);
    }

    @Test
    public void TileGettersSettersTest() {
        //DrawBoard.initBoard();
        Tile testTile = new Tile(10, 5, 1, 3);
        Tile parentTile = new Tile(0, 0, -1, 3);

        testTile.setX(5);
        testTile.setY(10);
        testTile.setValue(-1);
        testTile.setWeight(4);
        testTile.setG(1);
        testTile.setH(3);
        testTile.setF();
        testTile.setParent(parentTile);

        assertEquals(testTile.getX(), 5);
        assertEquals(testTile.getY(), 10);
        assertEquals(testTile.getValue(), -1);
        assertEquals(testTile.getWeight(), 4);
        assertEquals(testTile.getG(),1);
        assertEquals(testTile.getH(), 3);
        assertEquals(testTile.getF(), 4);
        assertEquals(testTile.getParent(), parentTile);
    }

    @Test
    public void BoardGettersTest() {
        Board testBoard = new Board();
        assertEquals(Board.getTile(4, 7), testBoard.getBoard()[7][4]);
    }

    @Test
    public void NeighbourTests() {
        Board testBoard = new Board();
        Game.getPlrList().add(new Player(1));
        List<Tile> octNeighbourList = new ArrayList<>();
        Tile testOctTile = Board.getTile(20, 5);
        octNeighbourList.add(Board.getTile(18, 5));
        octNeighbourList.add(Board.getTile(19, 4));
        octNeighbourList.add(Board.getTile(20, 4));
        octNeighbourList.add(Board.getTile(20, 6));
        octNeighbourList.add(Board.getTile(19, 5));
        assertEquals(Board.getNeighbours(testOctTile), octNeighbourList);

        Tile testRhombTile = Board.getTile(5, 4);
        List<Tile> rhombNeighbourList = new ArrayList<>();
        rhombNeighbourList.add(Board.getTile(4, 4));
        rhombNeighbourList.add(Board.getTile(6, 4));
        rhombNeighbourList.add(Board.getTile(6, 5));
        rhombNeighbourList.add(Board.getTile(4, 5));
        assertEquals(Board.getNeighbours(testRhombTile), rhombNeighbourList);

        List<Tile> furthOctNeighbourList = new ArrayList<>();
        Tile testfurthOctTile = Board.getTile(10, 5);
        furthOctNeighbourList.add(Board.getTile(10, 6));
        furthOctNeighbourList.add(Board.getTile(12, 6));
        furthOctNeighbourList.add(Board.getTile(8, 6));
        furthOctNeighbourList.add(Board.getTile(11, 5));
        furthOctNeighbourList.add(Board.getTile(9, 5));
        furthOctNeighbourList.add(Board.getTile(8, 5));
        furthOctNeighbourList.add(Board.getTile(12, 5));
        furthOctNeighbourList.add(Board.getTile(8, 4));
        furthOctNeighbourList.add(Board.getTile(9, 4));
        furthOctNeighbourList.add(Board.getTile(11, 4));
        furthOctNeighbourList.add(Board.getTile(12, 4));
        furthOctNeighbourList.add(Board.getTile(10, 4));
        assertEquals(Board.furthNeighbours(testfurthOctTile), furthOctNeighbourList);        
    }

    @Test
    public void playerTests() {
        Player testPlayer = new Player(0);
        testPlayer.setPlayerId(1);
        assertEquals(testPlayer.getPlayerId(), 1);

        testPlayer.refreshPlayerColour();
        assertEquals(testPlayer.getPlayerColour(), Game.playingBlack);
        
        testPlayer.incrementWins(); 
        testPlayer.incrementLosses();
        assertEquals(testPlayer.getWins(), 1);
        assertEquals(testPlayer.getlosses(), 1);
    }

    @Test
    public void checkWinTest() {
        Board testBoard = new Board();
        for (int i = 0; i < 11; i++) {
            Board.getTile(6, i).setValue(1);
        }
        assertEquals(Board.checkWin(new Player(1)), true);

    }

    @Test
    public void startWeights() {
        Board testBoard = new Board();
        for (int i = 0; i < 20; i += 2) {
            assertEquals(Board.getTile(i, 10).getWeight(), 1);
        }

        Board.piRuleWeight();
        for (int i = 0; i < 10; ++i) {
            assertEquals(Board.getTile(20, i).getWeight(), 1);
        }
    }

    @Test
    public void largestWeight() {
        Board testBoard = new Board();
        Tile target = Board.getTile(10, 10);
        target.setWeight(50);
        assertEquals(Board.largestWeight(Board.getTile(0, 0)), target);
    }

    @Test
    public void gameTests() {
        Game testGame = new Game();
        Game.flipMovingFlag();
        assertEquals(testGame.getMovingFlag(), true);
        assertEquals(testGame.isWhiteFirst(), true);

        
        assertEquals(Game.getPlrList().get(0).getPlayerId(), 1);
        assertEquals(Game.getPlrList().get(1).getPlayerId(), -1);

        Game.piRule();

        assertEquals(Game.getPlrList().get(0).getPlayerId(), -1);
        assertEquals(Game.getPlrList().get(1).getPlayerId(), 1);
    }

    @Test
    public void botPiRuleClearsStacks() throws Exception {
        Field placedField = Bot.class.getDeclaredField("placed");
        placedField.setAccessible(true);
        Stack<Tile> placed = (Stack<Tile>) placedField.get(null);
        placed.push(Board.getTile(0, 0));
        assertFalse(placed.isEmpty(), "placed should have a tile before piRule");

        Bot.piRule();

        assertTrue(placed.isEmpty(), "placed stack should be empty after piRule()");

        Field pathField = Bot.class.getDeclaredField("path");
        pathField.setAccessible(true);
        Queue<Tile> path = (Queue<Tile>) pathField.get(null);
        assertTrue(path.isEmpty(), "path queue should be empty after piRule()");
    }

    @Test
    public void botMakeMoveplacesATile() throws Exception {
        Field endField = Bot.class.getDeclaredField("endReached");
        endField.setAccessible(true);
        endField.set(null, false);

        Field placedField = Bot.class.getDeclaredField("placed");
        placedField.setAccessible(true);
        Stack<Tile> placed = (Stack<Tile>) placedField.get(null);
        assertTrue(placed.isEmpty(), "placed should be empty before makeMove");

        Method findNewStart = Bot.class.getDeclaredMethod("findNewStart");
        findNewStart.setAccessible(true);
        Bot bot = new Bot(1);
        Tile start = (Tile) findNewStart.invoke(bot);

        assertNotNull(start, "findNewStart should return a non-null tile on empty board");
        assertEquals(0, start.getValue(), "findNewStart tile should be unoccupied");
    }

    @Test
    public void botFindNewStartReturnsValidTile() throws Exception {
        Bot bot = new Bot(1);
        Method findNewStart = Bot.class.getDeclaredMethod("findNewStart");
        findNewStart.setAccessible(true);

        Tile result = (Tile) findNewStart.invoke(bot);

        assertNotNull(result, "findNewStart must not return null on a fresh board");
        assertEquals(0, result.getValue(), "Start tile must be unoccupied");
        assertEquals(0, result.getX() % 2, "BLACK start tile should be an octagon (even x)");
    }
}
