import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class buttArrMaker {

    // tryout color elbetel.
    private static boolean isBlackTurn = true;

    public static JButton[][] initButtArr(JLayeredPane p, JLayeredPane t) {
        JButton[][] buttArr = new JButton[21][21];
        //p.setLayout(new GridLayout(11, 11, 5, 5)); //make a grid layout for the octogons
        p.setLayout(null); //set absolute positioning
        //p.setOpaque(false);
        //p.setBackground(new Color(0, 0, 0, 0));

            for(int i = 0; i < 10; i++){ //draw the rhombus buttons
                for(int j = 0; j <  10; j++){
                    // aidans again buttArr[i][2*j+1] = new JButton();
                    //testing
                    int row = i * 2 + 1;
                    int col = j * 2 + 1;
                    buttArr[row][col] = new JButton();

                    buttArr[row][col].setBounds(82 + j * 68, 77 + i * 68, 35, 35);

                    buttArr[row][col].setContentAreaFilled(false);
                    buttArr[row][col].setBorderPainted(false);

                    buttArr[row][col].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JButton src = (JButton) e.getSource();
                            Color currentColor = isBlackTurn ? Color.BLACK : Color.WHITE;
                            TileDraw newTile = new TileDraw(currentColor, false);
                            newTile.setBounds(src.getBounds());
                            t.add(newTile);
                            isBlackTurn = !isBlackTurn;
                            p.remove(src);
                            p.repaint();
                            t.repaint();
                        }
                    });
                    p.add(buttArr[row][col]);
                  /*
                buttArr[i][2*j+1].setBounds(82 + j*68, 77 + i*68, 35, 35);
                //added this 2 lines

                    buttArr[i][2*j+1].setContentAreaFilled(false);
                    buttArr[i][2*j+1].setBorderPainted(false);

               // tryout the color issue
                buttArr[i][2*j+1].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JButton src = (JButton) e.getSource();

                        // tryout color
                        Color currentColor = isBlackTurn ? Color.BLACK : Color.WHITE;

                        TileDraw newTile = new TileDraw(currentColor, false);
                        newTile.setBounds(src.getBounds());
                        t.add(newTile);

                        isBlackTurn = !isBlackTurn; // Switch turn

                        p.remove(src);
                        p.repaint();
                        t.repaint();

                        /*
                        // BoardDraw.paintComponent()
                        // Board[i][j][0] = getCurrentPlayer();
                        t.add(new TileDraw());
                        p.repaint();
                        t.repaint();
                        p.remove(src);
                    }
                });
                buttArr[i][2*j+1].setVisible(true);
                p.add(buttArr[i][2*j+1]);
                */
                }
            }

        for (int i = 0; i < 11; ++i) { //draw the octagon buttons
            for (int j = 0; j < 11; ++j) {
                // aidans one uncommented for now buttArr[i][j+ j%2] = new JButton();

                // testing code
                int row = i *2;
                int col = j *2;
                buttArr[row][col] = new JButton();
                buttArr[row][col].setBounds(30 + j * 68, 25 + i * 68, 68, 68);

                buttArr[row][col].setContentAreaFilled(false);
                buttArr[row][col].setBorderPainted(false);

                buttArr[row][col].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JButton src = (JButton) e.getSource();
                        Color currentColor = isBlackTurn ? Color.BLACK : Color.WHITE;
                        TileDraw newTile = new TileDraw(currentColor, true);
                        newTile.setBounds(src.getBounds());
                        t.add(newTile);
                        isBlackTurn = !isBlackTurn;
                        p.remove(src);
                        p.repaint();
                        t.repaint();
                    }
                });
                p.add(buttArr[row][col]);
                /*
                buttArr[i][j+j%2].setBounds(30 + j*68, 25 + i*68, 68, 68);
                // i added new lines to test out 2 lines
                buttArr[i][j+j%2].setContentAreaFilled(false);
                buttArr[i][j+j%2].setBorderPainted(false);

                buttArr[i][j+j%2].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JButton src = (JButton) e.getSource();

                        // tryout color
                        Color currentColor = isBlackTurn ? Color.BLACK : Color.WHITE;

                        // Pass the color to the TileDraw constructor
                        TileDraw newTile = new TileDraw(currentColor, true);
                        newTile.setBounds(src.getBounds());
                        t.add(newTile);

                        isBlackTurn = !isBlackTurn; // Switch turn

                        p.remove(src);
                        p.repaint();
                        t.repaint();
                        /*
                        t.add(new TileDraw());
                        p.repaint();
                        t.repaint();
                        // Board[i][j][0] = getCurrentPlayer();
                        p.remove(src);
                    }
                });
                buttArr[i][j+j%2].setVisible(true);
                p.add(buttArr[row][col]); */
                //aidans code p.add(buttArr[i][j+j%2]);
                System.out.println("Button " + i + ", " + j + " created!");
            }
        }
        //p.setPreferredSize(new Dimension(11 * 68, 11 * 68));
        return buttArr;
    }
}