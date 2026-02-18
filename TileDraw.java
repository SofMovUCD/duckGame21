import java.awt.*;
import javax.swing.*;

public class TileDraw extends JPanel{
    private int x;
    private int y;
    public TileDraw(int x, int y){
        setPreferredSize(new Dimension(11 * 68, 11 * 68));
    }
    public void paint(Graphics g) {
        //super.paintComponent(g); //draws squares on top
        super.paint(g); //also draws squares on top
        Graphics2D g2 = (Graphics2D) g;
        int yC = y;
        int xC = x;
        int numberOfPoints = 8;
        int[] y = {yC, 25 + yC*68, 45 + yC*68, 73 + yC*68, 93 + yC*68, 93 + yC*68, 73 + yC*68, 45 + yC*68, 25 + yC*68};
        int[] x = {50 + xC*68, 78 + xC*68, 98 + xC*68, 98 + xC*68, 78 + xC*68, 50 + xC*68, 30 + xC*68, 30 + xC*68, 50 + xC*68};
        g2.setColor(Color.black);
        g2.fillPolygon(x, y, numberOfPoints);
//        g2.setColor(Color.black);
//        g2.drawLine(x[0], y[0], x[numberOfPoints - 1], y[numberOfPoints - 1]);
//        g2.dispose();

        System.out.println("Drawn octagon!"); //check that the function is accessed
    }   
}
