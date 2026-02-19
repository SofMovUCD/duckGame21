import java.awt.*;
import javax.swing.*;

public class BoardDraw extends JPanel {

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
    }

    public static void main(String[] args) {
        //Init board name and closing function
        JFrame boardFrame = new JFrame("Quax Game");
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //overlay panel to place above
        JPanel overlayPanel = new JPanel();
        overlayPanel.setLayout(new OverlayLayout(overlayPanel));
        overlayPanel.setPreferredSize(new Dimension(810, 830));

        //Create Panes for placing buttons
        JLayeredPane buttonPane = new JLayeredPane(); 
        JLayeredPane placedPane = new JLayeredPane();

        //create buttons
        buttArrMaker.initButtArr(buttonPane, placedPane);

        //Add Buttons, Tiles and Board to working Panel
        overlayPanel.add(buttonPane); // Top Layer (Invisible buttons)
        overlayPanel.add(placedPane); // Middle Layer (Tiles being placed)
        overlayPanel.add(new BoardDraw()); // Bottom Layer (The Brown Board)
        boardFrame.add(overlayPanel);

        //Set board properties
        boardFrame.setSize(810,830);
        boardFrame.setLocationRelativeTo(null);
        boardFrame.setVisible(true);
    }
}
