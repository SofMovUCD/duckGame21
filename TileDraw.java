import java.awt.*;
import javax.swing.*;

public class TileDraw extends JPanel {
    private Color tileColor;
    private boolean isOctagon;

    public TileDraw(Color color, boolean isOctagon) {
        this.tileColor = color;
        this.isOctagon = isOctagon;
        this.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Determine the border color based on the tile color
        Color FtileColor = (tileColor == Color.BLACK) ? Color.BLACK : Color.WHITE;

        Color borderColor;
        if (tileColor == Color.BLACK) {
            borderColor = new Color(100,100,100);
        }else{
            borderColor = Color.BLACK;
        }
        if (isOctagon) {
            // Full 68x68 coverage
            int[] x = {20, 48, 68, 68, 48, 20, 0, 0};
            int[] y = {0, 0, 20, 48, 68, 68, 48, 20};
            g2.setColor(FtileColor);
            g2.fillPolygon(x, y, 8);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(2));
            g2.drawPolygon(x, y, 8);
        } else {
            // Full 35x35 coverage for the Rhombus
            int[] x = {17, 36, 17, -2};
            int[] y = {-2, 17, 36, 17};
            g2.setColor(FtileColor);
            g2.fillPolygon(x, y, 4);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(2));
            g2.drawPolygon(x, y, 4);
        }
    }
}
/*

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
        g2.setPaint(Color.WHITE);
        g2.drawLine(x[0], y[0], x[numberOfPoints - 1], y[numberOfPoints - 1]);
    }
}
*/
