import java.awt.*;
import javax.swing.*;

public class TileDraw extends JPanel {
    private Color tileColor;
    private boolean isOctagon;
    private int nextMove;

    public TileDraw(Color color, boolean isOctagon) {
        this.tileColor = color;
        this.isOctagon = isOctagon;
        this.setOpaque(false);
        nextMove = -1;
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

        //update __ to move
        if(nextMove == 1){//whites turn, black to move
            g2.setColor(new Color(45,45,45));
        }
        else{
            g2.setColor(Color.WHITE);
        }

        int[] newX = {50 + 200, 78 + 200, 98 + 200, 98 + 200, 78 + 200, 50 + 200, 30 + 200, 30 + 200, 50 + 200};
        int[] newY = {25 + 800, 25 + 800, 45 + 800, 73 + 800, 93 + 800, 93 + 800, 73 + 800, 45 + 800, 25 + 800};
        g2.fillPolygon(newX, newY, 8);
        g2.setColor(Color.BLACK);
        g2.drawPolygon(newX, newY, 8);
        int[] newNewX = {18+330, 38+330, 18+330, -2+330};
        int[] newNewY = {25 + 815, 45 + 815, 65 + 815,  45 + 815};

        if(nextMove == 1){//whites turn, black to move
            g2.setColor(new Color(45,45,45));
        }
        else{
            g2.setColor(Color.WHITE);
        }

        g2.fillPolygon(newNewX, newNewY, 4);
        g2.setColor(Color.BLACK);
        g2.drawPolygon(newNewX, newNewY, 4);

        if(nextMove == 1){//whites turn, black to move
            BoardDraw.nextMove.setText("Black to move");
        }
        else{
            BoardDraw.nextMove.setText("White to move");
        }
        nextMove*= -1;

    }
}
