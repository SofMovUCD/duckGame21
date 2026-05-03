package org.movshovich.QuaxRebuild.src;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * Represents a logic cell on the game board.
 * Contains pathfinding data (G, H, F) and the physical UI button.
 */
public class Tile {
    private int x; // X coordinate in the grid
    private int y; // Y coordinate in the grid
    private int value = 0; // 0: Empty, 1: Black, -1: White
    private int weight = 0; // Importance of this tile for the Bot's decision making
    private JButton tileButton;

    // A* Pathfinding Variables
    private int g;  // Distance from start node
    private int h; // Heuristic (estimated distance to end)
    private int f; // Total cost (G + H)
    private Tile parent; // Link to previous tile in a path

    /** Constructs a Tile at grid position (x,y) and initialises its clickable button. */
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        initButton();
    }

    // Getters and Setters
    public Tile getParent() {return parent;}
    public void setParent(Tile parent) {this.parent = parent;}
    public int getX() { return x;}
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

    /**
     * Initializes the button for this tile.
     * Determines if the tile is an Octagon or a Rhombus based on coordinates.
     */
    public void initButton() {
        tileButton = new JButton();

        // Even X coordinates are Octagons, Odd are Rhombuses
        if (x % 2 == 0) {octagonButton();}
        else {rhombusButton();}

        tileButton.setName(x +" " + y);
        tileButton.setContentAreaFilled(false);
        tileButton.setBorderPainted(false);

        // Click Logic
        tileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();

            // Set tile ownership to current player
            value = Game.getCurrentPlayer();
            weight = -10;// Mark as occupied/less desirable for future moves

            Color currentColor = Game.plrByID(Game.getCurrentPlayer()).getPlayerColour();

            // Create the visual graphic for the placed piece
            TileDraw newTile = new TileDraw(currentColor, (x % 2 == 0));
            newTile.setBounds(drawnBounds(x));

            DrawBoard.placedPane.add(newTile);
            DrawBoard.buttonPane.remove(src);// Remove the clickable button once placed

            // Logic for updating weights near the edges (end game tiles)
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
            Game.flipMovingFlag();// Signal the Game loop that the move is done
            }
        });
        DrawBoard.buttonPane.add(tileButton);
    }

    /** Sets the clickable area for Octagon tiles */
    public void octagonButton() {
        tileButton.setBounds(30 + (x/2)*DrawBoard.OCTAGON_DISTANCE,
                             45 + (y)*DrawBoard.OCTAGON_DISTANCE,
                                    DrawBoard.OCTAGON_DISTANCE,30);
    }

    /** Sets the clickable area for Rhombus tiles */
    public void rhombusButton() {
        tileButton.setBounds(88 + (x/2)*DrawBoard.OCTAGON_DISTANCE,
                             82 + (y)*DrawBoard.OCTAGON_DISTANCE, 20, 18);
    }

    /**
     * Calculates the Rectangle area where the TileDraw polygon will be rendered.
     */
    private Rectangle drawnBounds(int x) {
        if (x % 2 ==0) {
            return new Rectangle((x/2)*DrawBoard.OCTAGON_DISTANCE + 30,
                                   (y)*DrawBoard.OCTAGON_DISTANCE + 25,
                                       DrawBoard.OCTAGON_DISTANCE,
                                       DrawBoard.OCTAGON_DISTANCE);
        } else {
            return new Rectangle((x/2)*DrawBoard.OCTAGON_DISTANCE + 80,
                                 (y)*DrawBoard.OCTAGON_DISTANCE + 75,
                                 40,40);
        }
    }

    /** Check if all neighbors are occupied */
    public boolean isBlocked() {
        for (Tile neighbour : Board.furthNeighbours(this)) {
            if (neighbour.value == 0) return false;
        }
        return true;
    }

    /** Returns the tile with the higher weight for bot decision making */
    public Tile largerWeight(Tile tile) {
        if (this.weight > tile.weight) {
            return this;
        } else return tile;
    }

    public String toString() {
        return "Tile X: " + x +", Y: " + y + ", value: " + value;
    }
}
