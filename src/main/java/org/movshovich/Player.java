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

    public Color getPlayerColour(){return tileColour;}

    public int getWins(){return wins;}
    public void incrementWins(){wins++;}

    public int getlosses(){return losses;}
    public void incrementLosses(){losses++;}


    public void makeMove(){
        //should add button pane
                //System.out.println(Board.currentPlayer + "'s turn");
        BoardDraw.overlayPanel.add(BoardDraw.buttonPane);
                //System.out.println("Buttons added");
        Board.movingFlag = 1;
        while (Board.movingFlag == 1) {
                    System.out.print("");
        }
                //System.out.println("Move Made");
        BoardDraw.overlayPanel.remove(BoardDraw.buttonPane);
                //System.out.println("buttons removed");
        Board.currentPlayer *= -1;
                //System.out.println("Next turn...");
        //should remove button pane
    }

}
