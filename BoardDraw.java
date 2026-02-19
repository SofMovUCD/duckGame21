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
        

        // Connect the first and last vertex
        
    }

    public Dimension getPreferredSize() {
        return new Dimension(810,800);
    }


    public static void main(String[] args) {
        //System.setProperty("sun.java2d.uiScale", "1.5");
        JFrame boardFrame = new JFrame("Quax Game");
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(new Color(45, 45, 45)); // Dar

        JPanel overlayPanel = new JPanel();
        overlayPanel.setLayout(new OverlayLayout(overlayPanel));
        //cahnges to fix my screen
        overlayPanel.setPreferredSize(new Dimension(810, 800));
        JLayeredPane buttonPane = new JLayeredPane(); 
        JLayeredPane placedPane = new JLayeredPane();
        //boardFrame.setLayout(new BorderLayout()); // Explicitly set BorderLayout
        
        // Add BoardDraw to CENTER (takes most space)
        //boardFrame.add(new BoardDraw(), BorderLayout.CENTER);

        /* // aidan's code i will uncommet later.
        buttArrMaker.initButtArr(buttonPane, placedPane);
        overlayPanel.add(buttonPane); //add the buttons
        overlayPanel.add(placedPane);
        overlayPanel.add(new BoardDraw()); //below add the actual board
        //buttonPane.setOpaque(false);
        //buttonPane.setBackground(new Color(0, 0, 0, 0)); */

        //test code

        buttonPane.setLayout(null);
        buttonPane.setOpaque(false);
        placedPane.setLayout(null);
        placedPane.setOpaque(false);

        // test code
        buttArrMaker.initButtArr(buttonPane, placedPane);

        // i added this codes for testing
        // this ones aswell
        // Correct order for the overlay
        overlayPanel.add(buttonPane); // Top Layer (Invisible buttons)
        overlayPanel.add(placedPane); // Middle Layer (Tiles being placed)
        overlayPanel.add(new BoardDraw()); // Bottom Layer (The Brown Board)
// fix my screen
        centerWrapper.add(overlayPanel);
        boardFrame.add(centerWrapper);

// Ensure these are absolutely transparent
       /* buttonPane.setOpaque(false);
        placedPane.setOpaque(false); */

        // ending of my testing
        
        // Add buttonPane to SOUTH or another region
        //boardFrame.add(overlayPanel, BorderLayout.CENTER);

        boardFrame.setSize(1200,1200);
        //boardFrame.pack(); //sets the size of the stuff in the frame
       // mine code
        boardFrame.setLocationRelativeTo(null);
       // aidans code i will change for now trying things out
        //boardFrame.setLocation(100,100); //chhange position of where displayed on screen
        boardFrame.setVisible(true);
    }
}
/*
    public static void main(String[] args) {
        JFrame boardFrame = new JFrame("Quax Game");
        JLayeredPane buttonPane = new JLayeredPane();
        boardFrame.setBounds(0,0,810,800);
        buttonPane.setBounds(0,0,810,800);
        boardFrame.add(new BoardDraw());
        boardFrame.add(buttonPane);
        buttArrMaker.initButtArr(buttonPane);
        boardFrame.setVisible(true);

    }
}
*/

