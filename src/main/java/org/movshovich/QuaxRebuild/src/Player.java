package org.movshovich.QuaxRebuild.src;
import java.awt.*;

public class Player {
    private int playerId;
    private Color tileColour;
    private int wins;
    private int losses;

    public Player(int playerId){
        this.playerId = playerId;
        this.tileColour = playerId == 1? Game.playingBlack: Color.white;
        this.wins = 0;
        this.losses = 0;
    }
    public int getPlayerId(){return playerId;}
    public void setPlayerId(int newId){playerId = newId;}
    public Color getPlayerColour(){return tileColour;}
    
    public void refreshPlayerColour() {
        tileColour = playerId == 1? Game.playingBlack: Color.white;
        }

    public int getWins(){return wins;}
    public void incrementWins(){wins++;}

    public int getlosses(){return losses;}
    public void incrementLosses(){losses++;}

    public void makeMove() {
        System.out.print("");
        DrawBoard.overlayPanel.add(DrawBoard.buttonPane);
        Game.flipMovingFlag();
    }
}