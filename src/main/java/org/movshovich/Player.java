package org.movshovich;

import java.awt.*;

public class Player {
    private int playerId;
    private Color tileColour;
    private int wins;
    private int losses;

    public Player(int playerId){
        this.playerId = playerId;
        this.tileColour = playerId == 1? new Color(45, 45, 45): Color.white;
        this.wins = 0;
        this.losses = 0;
    }
    public int getPlayerId(){return playerId;}
    public void setPlayerId(int newId){playerId = newId;}
    public Color getPlayerColour(){return tileColour;}
    
    public void refreshPlayerColour() {
        tileColour = playerId == 1? new Color(45, 45, 45): Color.white;
        }

    public int getWins(){return wins;}
    public void incrementWins(){wins++;}

    public int getlosses(){return losses;}
    public void incrementLosses(){losses++;}


    public void makeMove(){
        BoardDraw.overlayPanel.add(BoardDraw.buttonPane);
                
        Board.movingFlag = 1;
        while (Board.movingFlag == 1) {
                    System.out.print("");
        }

        BoardDraw.overlayPanel.remove(BoardDraw.buttonPane);
     
    }

}
