import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class buttArrMaker {

    // tryout color elbetel.
    public static int isBlackTurn = 1;

    public static JButton[][] initButtArr(JLayeredPane p, JLayeredPane t) {
        JButton[][] buttArr = new JButton[21][21];
        Color usingBlack = new Color(45, 45, 45);

            for(int i = 0; i < 10; i++){ //draw the rhombus buttons
                for(int j = 0; j <  10; j++){
                    // aidans again buttArr[i][2*j+1] = new JButton();
                    //testing
                    int row = i;
                    int col = j * 2 + 1;
                    buttArr[row][col] = new JButton();

                    buttArr[row][col].setBounds(88 + j * 68, 82 + i * 68, 20, 18);

                    buttArr[row][col].setContentAreaFilled(false);
                    buttArr[row][col].setBorderPainted(false);

                    buttArr[row][col].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JButton src = (JButton) e.getSource();
                            Color currentColor = isBlackTurn == 1 ? usingBlack : Color.WHITE;
                            TileDraw newTile = new TileDraw(currentColor, false);
                            newTile.setBounds(src.getX()-8 , src.getY()-7, 40, 40);
                            t.add(newTile);
                            isBlackTurn *= -1;
                            p.remove(src);
                            t.repaint();
                            p.repaint();
                            System.out.println(((src.getY()-82) / 68) + " " + (((src.getX()-88) / 68)*2+1));
                            Board.board[(src.getY()-82) / 68][((src.getX()-88) / 68)*2+1][0] = isBlackTurn;
                        }
                    });
                    p.add(buttArr[row][col]);
                }
            }

        for (int i = 0; i < 11; i++) { //draw the octagon buttons
            for (int j = 0; j < 11; j++) {
                // aidans one uncommented for now buttArr[i][j+ j%2] = new JButton();

                // testing code
                int row = i ;
                int col = j+ j%2;
                buttArr[row][col] = new JButton();
                buttArr[row][col].setBounds(30 + j * 68, 45 + i * 68, 68, 30);

                buttArr[row][col].setContentAreaFilled(false);
                buttArr[row][col].setBorderPainted(false);

                buttArr[row][col].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JButton src = (JButton) e.getSource();
                        Color currentColor = isBlackTurn == 1 ? usingBlack : Color.WHITE;
                        TileDraw newTile = new TileDraw(currentColor, true);
                        newTile.setBounds(src.getX(), src.getY()-20, 68, 68);
                        t.add(newTile);
                        isBlackTurn *= -1;
                        p.remove(src);
                        t.repaint();
                        p.repaint();
                        System.out.println(((src.getY()-45)/ 68) + " " + (((src.getX()-30) / 68)*2));
                        Board.board[(src.getY()-45)/ 68][((src.getX()-30) / 68)*2][0] = isBlackTurn;

                    }
                });
                p.add(buttArr[row][col]);
            }
        }

        return buttArr;
    }
}