package org.movshovich.QuaxRebuild.src;
import java.awt.*;
import java.util.Arrays;

import javax.swing.*;

public class DrawBoard extends JPanel{
    public static final int OCTAGON_LENGTH = 11;
    public static final int RHOMBUS_LENGTH = 10;
    public static final int OCTAGON_DISTANCE = 68;

    public static JLabel nextMove, bot, player;
    public static JLayeredPane placedPane;
    public static JPanel overlayPanel;
    public static JLayeredPane buttonPane;

    public DrawBoard() {

    }

    public void paint(Graphics g) {

        int yC;
        int xC;
        int numberOfPoints = 8;

        final Color octagonColor = new Color(173, 111, 49);
        final Color rhombusColor = new Color(99, 59, 19);

        int[] octagonX = {50, 78, 98, 98, 78, 50, 30, 30, 50};
        int[] octagonY = {25, 25, 45, 73, 93, 93, 73, 45, 25};

        Graphics2D g2 = (Graphics2D) g;

        g2.fillRect(0, 0, 810, 800);

        g2.setColor(Color.white);
        g2.fillRect(0, 45, 810, 710);

        g2.setColor(rhombusColor);
        g2.fillRect(50, 50, 700, 700);

        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(3));

        for (yC = 0; yC < OCTAGON_LENGTH; ++yC) {
            final int currYC = yC;
            int[] y = Arrays.stream(octagonY).map(a -> a + currYC*OCTAGON_DISTANCE).toArray();
                        
            for (xC = 0; xC < OCTAGON_LENGTH; ++xC) {
                final int currXC = xC;
                int[] x = Arrays.stream(octagonX).map(a -> a + currXC*OCTAGON_DISTANCE).toArray();

                g2.setColor(octagonColor);
                g2.fillPolygon(x, y, numberOfPoints);
                for (int i = 0; i < numberOfPoints - 1; i++) {
                    g2.setColor(Color.black);
                    g2.drawLine(x[i], y[i], x[i + 1], y[i + 1]);
                    
                }
                g2.drawLine(x[0], y[0], x[numberOfPoints - 1], y[numberOfPoints - 1]);
            }
        }
        //update __ to move
        g2.setColor(Game.plrByID(Game.getCurrentPlayer()).getPlayerColour());
        int[] newX = Arrays.stream(octagonX).map(a -> a + 200).toArray();
        int[] newY = Arrays.stream(octagonY).map(a -> a + 800).toArray();
        g2.fillPolygon(newX, newY, 8);
        g2.setColor(Color.BLACK);
        g2.drawPolygon(newX, newY, 8);

        int[] newNewX = {18+330, 38+330, 18+330, -2+330};
        int[] newNewY = {25 + 815, 45 + 815, 65 + 815,  45 + 815};

        g2.setColor(Game.plrByID(Game.getCurrentPlayer()).getPlayerColour());
        g2.fillPolygon(newNewX, newNewY, 4);
        g2.setColor(Color.BLACK);
        g2.drawPolygon(newNewX, newNewY, 4);
    }

    public static void initBoard() {
        //Init board name and closing function
        JFrame boardFrame = new JFrame("Quax Player vs Bot");
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //overlay panel to place above
        overlayPanel = new JPanel();
        overlayPanel.setLayout(new OverlayLayout(overlayPanel));
        overlayPanel.setPreferredSize(new Dimension(810, 1000));

        //Create Panes for placing buttons
        buttonPane = new JLayeredPane(); 
        placedPane = new JLayeredPane();
        placedPane.setSize(810, 1000);
        //create label
        nextMove = new JLabel("BLACK to play");
        nextMove.setName("nextMoveLabelName");
        nextMove.setBounds(400, 830, 150, 50);
        //display text to display which player is which colour
        bot = new JLabel("    Bot");
        bot.setForeground(Color.WHITE);
        bot.setBackground(new Color(45,45,45));
        bot.setBounds(580, 830, 50, 50);
        bot.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
        player = new JLabel("  Player");
        player.setForeground(new Color(45,45,45));
        player.setBackground(Color.WHITE);
        player.setBounds(660, 830, 55, 50);
        player.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
        //do the numbers and letters at edge of board
        for(int i = 0; i < OCTAGON_LENGTH; i++){
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
        //add labels
        nextMove.setOpaque(true);
        bot.setOpaque(true);
        player.setOpaque(true);
        placedPane.add(nextMove);
        placedPane.add(player);
        placedPane.add(bot);

        overlayPanel.add(buttonPane);
        overlayPanel.add(placedPane); // Middle Layer (Tiles being placed)
        overlayPanel.add(new DrawBoard()); // Bottom Layer (The Brown Board)
        boardFrame.add(overlayPanel);

        //Set board properties
        boardFrame.setSize(810,1000);
        boardFrame.setLocationRelativeTo(null);
        boardFrame.setVisible(true);
    }

    public static void repaintAll() {
        overlayPanel.repaint();
        placedPane.repaint();
        buttonPane.repaint();
    } 
}
