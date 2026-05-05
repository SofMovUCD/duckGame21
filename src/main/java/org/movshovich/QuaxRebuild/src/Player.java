package org.movshovich.QuaxRebuild.src;
import java.awt.*;

/**
 * Represents a player (either Human or Bot) in the game.
 * Stores ID, color, and performance statistics.
 */
public class Player {
    private int playerId; // Usually 1 or -1
    private Color tileColour;
    private int wins;
    private int losses;

    /** Constructs a Player with the given ID, sets colour (BLACK for 1, WHITE for -1) and zeroes stats. */
    public Player(int playerId) {
        this.playerId = playerId;
        // Default: 1 is Black (playingBlack), -1 is White
        this.tileColour = playerId == 1 ? Game.playingBlack : Color.white;
        this.wins = 0;
        this.losses = 0;
    }

    /** Returns this player's ID (1=BLACK, -1=WHITE). */
    public int getPlayerId(){return playerId;}
    /** Updates this player's ID, used when Pi Rule swaps player roles. */
    public void setPlayerId(int newId){playerId = newId;}
    /** Returns the tile colour associated with this player. */
    public Color getPlayerColour(){return tileColour;}

    /**
     * Syncs the tile color with the current PlayerID (used after Pi Rule swap).
     */
    public void refreshPlayerColour() {
        tileColour = playerId == 1? Game.playingBlack: Color.white;
        }

    /** Returns total wins this session. */
    public int getWins(){return wins;}
    /** Increments the win counter by one. */
    public void incrementWins(){wins++;}

    /** Returns total losses this session. */
    public int getlosses(){return losses;}
    /** Increments the loss counter by one. */
    public void incrementLosses(){losses++;}

    /**
     * Triggers the UI to allow a move and sets the moving flag.
     * This flag is monitored by the main game loop in Game.java.
     */
    public void makeMove() {
        DrawBoard.overlayPanel.add(DrawBoard.buttonPane);
        Game.flipMovingFlag();
    }
}