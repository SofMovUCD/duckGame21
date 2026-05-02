package org.movshovich.QuaxRebuild.src;
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
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (isOctagon) {
            // Full 68x68 coverage
            int[] x = {20, 48, 68, 68, 48, 20, 0, 0};
            int[] y = {0, 0, 20, 48, 68, 68, 48, 20};
            graphics.setColor(tileColor);
            graphics.fillPolygon(x, y, 8);
            graphics.setColor(Color.BLACK);
            graphics.setStroke(new BasicStroke(3));
            graphics.drawPolygon(x, y, 8);
        } else {
            // Full 35x35 coverage for the Rhombus
            int[] x = {18, 38, 18, -2};
            int[] y = {-2, 18, 38, 18};
            graphics.setColor(tileColor);
            graphics.fillPolygon(x, y, 4);
            graphics.setColor(Color.BLACK);
            graphics.setStroke(new BasicStroke(2));
            graphics.drawPolygon(x, y, 4);
        }
    }
}
