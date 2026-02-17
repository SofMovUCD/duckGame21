import java.awt.*;
import javax.swing.*;

public class BoardDraw extends JPanel {

    public void paint(Graphics g) {
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

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
    }   

    public Dimension getPreferredSize() {
        return new Dimension(810,800);
    }


    public static void main(String[] args) {
        JFrame boardFrame = new JFrame(); 
        
        JPanel overlayPanel = new JPanel();
        overlayPanel.setLayout(new OverlayLayout(overlayPanel));

        JLayeredPane buttonPane = new JLayeredPane(); 
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boardFrame.setLayout(new BorderLayout()); // Explicitly set BorderLayout
        
        // Add BoardDraw to CENTER (takes most space)
        //boardFrame.add(new BoardDraw(), BorderLayout.CENTER);
        
        buttArrMaker.initButtArr(buttonPane);
        overlayPanel.add(buttonPane); //add the buttons
        overlayPanel.add(new BoardDraw()); //below add the actual board
        //buttonPane.setOpaque(false);
        //buttonPane.setBackground(new Color(0, 0, 0, 0));
        
        // Add buttonPane to SOUTH or another region
        boardFrame.add(overlayPanel);
        boardFrame.pack(); //sets the size of the stuff in the frame
        boardFrame.setLocation(100,100); //chhange position of where displayed on screen
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

