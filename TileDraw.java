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
        
        if (isOctagon) {
            // Full 68x68 coverage
            int[] x = {20, 48, 68, 68, 48, 20, 0, 0};
            int[] y = {0, 0, 20, 48, 68, 68, 48, 20};
            g2.setColor(tileColor);
            g2.fillPolygon(x, y, 8);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(3));
            g2.drawPolygon(x, y, 8);
        } else {
            // Full 35x35 coverage for the Rhombus
            int[] x = {18, 38, 18, -2};
            int[] y = {-2, 18, 38, 18};
            g2.setColor(tileColor);
            g2.fillPolygon(x, y, 4);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawPolygon(x, y, 4);
        }
        //update label on button press
        if(buttArrMaker.isBlackTurn == 1){//whites turn, black to move
            BoardDraw.nextMove.setText("Black to move");
        }
        else{
            BoardDraw.nextMove.setText("White to move");
        }

    }
}
