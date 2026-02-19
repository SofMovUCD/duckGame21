import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class buttArrMaker {

    // tryout color elbetel.
    private static boolean isBlackTurn = true;

    public static JButton[][] initButtArr(JLayeredPane p, JLayeredPane t) {
        JButton[][] buttArr = new JButton[21][21];

            for(int i = 0; i < 10; i++){ //draw the rhombus buttons
                for(int j = 0; j <  10; j++){
                    // aidans again buttArr[i][2*j+1] = new JButton();
                    //testing
                    int row = i * 2 + 1;
                    int col = j * 2 + 1;
                    buttArr[row][col] = new JButton();

                    buttArr[row][col].setBounds(88 + j * 68, 82 + i * 68, 20, 18);

                    buttArr[row][col].setContentAreaFilled(false);
                    buttArr[row][col].setBorderPainted(false);

                    buttArr[row][col].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JButton src = (JButton) e.getSource();
                            Color currentColor = isBlackTurn ? Color.BLACK : Color.WHITE;
                            TileDraw newTile = new TileDraw(currentColor, false);
                            newTile.setBounds(src.getX()-7 , src.getY()-7, 35, 35);
                            t.add(newTile);
                            isBlackTurn = !isBlackTurn;
                            p.remove(src);
                            t.repaint();
                        }
                    });
                    p.add(buttArr[row][col]);
                }
            }

        for (int i = 0; i < 11; ++i) { //draw the octagon buttons
            for (int j = 0; j < 11; ++j) {
                // aidans one uncommented for now buttArr[i][j+ j%2] = new JButton();

                // testing code
                int row = i *2;
                int col = j *2;
                buttArr[row][col] = new JButton();
                buttArr[row][col].setBounds(30 + j * 68, 45 + i * 68, 68, 30);

                buttArr[row][col].setContentAreaFilled(false);
                buttArr[row][col].setBorderPainted(false);

                buttArr[row][col].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JButton src = (JButton) e.getSource();
                        Color currentColor = isBlackTurn ? Color.BLACK : Color.WHITE;
                        TileDraw newTile = new TileDraw(currentColor, true);
                        newTile.setBounds(src.getX(), src.getY()-20, 68, 68);
                        t.add(newTile);
                        isBlackTurn = !isBlackTurn;
                        p.remove(src);
                        t.repaint();
                    }
                });
                p.add(buttArr[row][col]);
            }
        }
        return buttArr;
    }
}