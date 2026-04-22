
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;
//import org.junit.jupiter.api;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import org.movshovich.QuaxRebuild.src.Board;
import org.movshovich.QuaxRebuild.src.Game;
import org.movshovich.QuaxRebuild.src.Player;
import org.movshovich.QuaxRebuild.src.Tile;

public class BGTest {
    @Test
    public void TileGettersSettersTest() {
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

        assertEquals(testTile.getX(), 10);
        assertEquals(testTile.getY(), 5);
        assertEquals(testTile.getValue(), 1);
        assertEquals(testTile.getWeight(), 3);
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
}
