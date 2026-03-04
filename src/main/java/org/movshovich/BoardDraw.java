package org.movshovich;

import java.awt.*;
import javax.swing.*;

public class BoardDraw extends JPanel {
    public static JLabel nextMove;
    public static JPanel overlayPanel;
    public static JLayeredPane buttonPane;

    public void paint(Graphics g) {

        int myboard[][][] = Board.board;
        int yC = 0;
        int xC = 0;
        int numberOfPoints = 8;

        Graphics2D g2 = (Graphics2D) g;

        g2.fillRect(0, 0, 810, 800);

        g2.setColor(Color.white);
        g2.fillRect(0, 45, 810, 710);

        g2.setColor(new Color(99, 59, 19));
        g2.fillRect(50, 50, 700, 700);

        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(3));
        for (yC = 0; yC < 11; ++yC) {
            int[] y = {25 + yC*68, 25 + yC*68, 45 + yC*68, 73 + yC*68, 93 + yC*68, 93 + yC*68, 73 + yC*68, 45 + yC*68, 25 + yC*68};
            for (xC = 0; xC < 11; ++xC) {
                int[] x = {50 + xC*68, 78 + xC*68, 98 + xC*68, 98 + xC*68, 78 + xC*68, 50 + xC*68, 30 + xC*68, 30 + xC*68, 50 + xC*68};
                g2.setColor(new Color(173, 111, 49));
                g2.fillPolygon(x, y, numberOfPoints);
                for (int i = 0; i < numberOfPoints - 1; i++) {
                    g2.setColor(Color.black);
                    g2.drawLine(x[i], y[i], x[i + 1], y[i + 1]);
                    
                }
                g2.drawLine(x[0], y[0], x[numberOfPoints - 1], y[numberOfPoints - 1]);
            }
        }
        //update __ to move
        g2.setColor(Board.plrByID(Board.currentPlayer).getPlayerColour());
        int[] newX = {50 + 200, 78 + 200, 98 + 200, 98 + 200, 78 + 200, 50 + 200, 30 + 200, 30 + 200, 50 + 200};
        int[] newY = {25 + 800, 25 + 800, 45 + 800, 73 + 800, 93 + 800, 93 + 800, 73 + 800, 45 + 800, 25 + 800};
        g2.fillPolygon(newX, newY, 8);
        g2.setColor(Color.BLACK);
        g2.drawPolygon(newX, newY, 8);

        int[] newNewX = {18+330, 38+330, 18+330, -2+330};
        int[] newNewY = {25 + 815, 45 + 815, 65 + 815,  45 + 815};

        g2.setColor(Board.plrByID(Board.currentPlayer).getPlayerColour());
        g2.fillPolygon(newNewX, newNewY, 4);
        g2.setColor(Color.BLACK);
        g2.drawPolygon(newNewX, newNewY, 4);
    }

    public static void initBoard() {
        //Init board name and closing function
        JFrame boardFrame = new JFrame("Quax Player vs Player");
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //overlay panel to place above
        overlayPanel = new JPanel();
        overlayPanel.setLayout(new OverlayLayout(overlayPanel));
        overlayPanel.setPreferredSize(new Dimension(810, 1000));

        //Create Panes for placing buttons
        buttonPane = new JLayeredPane(); 
        JLayeredPane placedPane = new JLayeredPane();
        placedPane.setSize(810, 1000);
        //create label
        nextMove = new JLabel("BLACK to play");
        nextMove.setBounds(400, 830, 150, 50);
        //do the numbers and letters at edge of board
        for(int i = 0; i < 11; i++){
            JLabel num = new JLabel(Integer.toString(i+1));
            JLabel chars = new JLabel(Character.toString(i+65));
            num.setBounds(10, 50 + i*68, 15,15);
            chars.setBounds(60 + i*68, 10, 15,15 );
            chars.setForeground(Color.WHITE);
            num.setName(Integer.toString(i+1)); //for testing
            chars.setName(Character.toString((char)(i+65))); //for testing
            placedPane.add(num);
            placedPane.add(chars);
        }
        //l.size
        nextMove.setOpaque(true);
        placedPane.add(nextMove);

        //create buttons
        buttArrMaker.initButtArr(buttonPane, placedPane);
        //Add Buttons, Tiles and Board to working Panel
         // Top Layer (Invisible buttons)
        //line fix maybe??? elbetel wed
        overlayPanel.add(buttonPane);
        overlayPanel.add(placedPane); // Middle Layer (Tiles being placed)
        overlayPanel.add(new BoardDraw()); // Bottom Layer (The Brown Board)
        boardFrame.add(overlayPanel);

        //Set board properties
        boardFrame.setSize(810,1000);
        boardFrame.setLocationRelativeTo(null);
        boardFrame.setVisible(true);
    }
}
