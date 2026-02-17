import java.awt.*;
import javax.swing.*;

public class TileDraw extends JPanel{
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int yC = 0;
        int xC = 0;
        int numberOfPoints = 8;
        int[] y = {25 + yC*68, 25 + yC*68, 45 + yC*68, 73 + yC*68, 93 + yC*68, 93 + yC*68, 73 + yC*68, 45 + yC*68, 25 + yC*68};
        int[] x = {50 + xC*68, 78 + xC*68, 98 + xC*68, 98 + xC*68, 78 + xC*68, 50 + xC*68, 30 + xC*68, 30 + xC*68, 50 + xC*68};
        g2.setColor(Color.black);
        g2.fillPolygon(x, y, numberOfPoints);
        
        g2.setColor(Color.black);
        g2.drawLine(x[0], y[0], x[numberOfPoints - 1], y[numberOfPoints - 1]);
    }   
}
