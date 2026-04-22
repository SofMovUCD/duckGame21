package org.movshovich.QuaxRebuild.src;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Tile {
    private int x; //x cord on the board
    private int y; //y cord on the board
    private int value; //1 for black, -1 for white
    private int weight; //weight for the bot
    private JButton tileButton;
    private int g;
    private int h;
    private int f;

    public Tile getParent() {
        return parent;
    }

    public void setParent(Tile parent) {
        this.parent = parent;
    }

    private Tile parent;

    public Tile(int x, int y, int value, int weight) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.weight = weight;
        initButton();
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x;}

    public int getY() {return y;}
    public void setY(int y) {this.y = y;}

    public int getValue() {return value;}
    public void setValue(int value) {this.value = value;}

    public int getWeight() {return weight;}
    public void setWeight(int weight) {this.weight = weight;}

    public int getG() {return g;}
    public void setG(int g) {this.g = g;}

    public int getH() {return h;}
    public void setH(int h) {this.h = h;}

    public int getF() {return f;}
    public void setF() {this.f = g + h;}


    public JButton getTileButton() {return tileButton;}

    public void initButton() {
        tileButton = new JButton();
        if (x % 2 == 0) {octagonButton();}
                else {rhombusButton();}
        tileButton.setName(x +" " + y);
        tileButton.setContentAreaFilled(false);
        tileButton.setBorderPainted(false);

        tileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();
            value = Game.getCurrentPlayer();
            weight = -10;
            Color currentColor = Game.plrByID(Game.getCurrentPlayer()).getPlayerColour();
            TileDraw newTile = new TileDraw(currentColor, (x % 2 == 0? true: false));
            newTile.setBounds(drawnBounds(x));
            DrawBoard.placedPane.add(newTile);
            DrawBoard.buttonPane.remove(src);
            if (x % 2 == 0) {
                Tile endTileA = Board.getTile((int)Game.valueForID(x, 20, Game.plrByID(-Game.getCurrentPlayer())), 
                            (int) Game.valueForID(10, y, Game.plrByID(-Game.getCurrentPlayer())));
                Tile endTileB = Board.getTile((int)Game.valueForID(x, 20, Game.plrByID(Game.getCurrentPlayer())), 
                            (int) Game.valueForID(10, y, Game.plrByID(Game.getCurrentPlayer())));
                

                if (Game.plrByID(Game.getCurrentPlayer()).getClass() == Bot.class) {
                    endTileB.weight++;
                }
                else {
                    endTileA.weight--;
                }
            }
            Game.flipMovingFlag();
            //System.out.println(src.getName());
            }
        });
        DrawBoard.buttonPane.add(tileButton);
    }

    public void octagonButton() {
        tileButton.setBounds(30 + (x/2)*DrawBoard.OCTAGON_DISTANCE,
                             45 + (y)*DrawBoard.OCTAGON_DISTANCE,
                                    DrawBoard.OCTAGON_DISTANCE,
                             30);    
    }

    public void rhombusButton() {
        tileButton.setBounds(88 + (x/2)*DrawBoard.OCTAGON_DISTANCE,
                             82 + (y)*DrawBoard.OCTAGON_DISTANCE,
                                    20,
                             18);   
    }

    private Rectangle drawnBounds(int x) {
        if (x % 2 ==0) {
            return new Rectangle((x/2)*DrawBoard.OCTAGON_DISTANCE + 30,
                                   (y)*DrawBoard.OCTAGON_DISTANCE + 25,
                                       DrawBoard.OCTAGON_DISTANCE,
                                       DrawBoard.OCTAGON_DISTANCE);
        } else {
            return new Rectangle((x/2)*DrawBoard.OCTAGON_DISTANCE + 80,
                                 (y)*DrawBoard.OCTAGON_DISTANCE + 75,
                                 40,
                                 40);
        }
    }
    public String toString() {
        return "Tile X: " + x +", Y: " + y + ", value: " + value;
    }

    public boolean isBlocked() {
        for (Tile neighbour : Board.getNeighbours(this)) {
            if (neighbour.value == 0) return false;
        }
        return true;
    }

    public Tile largerWeight(Tile tile) {
        if (this.weight > tile.weight) {
            return this;
        } else return tile;
    }
}
