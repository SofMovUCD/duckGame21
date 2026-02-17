import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class buttArrMaker {
    public static JButton[][] initButtArr(JLayeredPane p, JLayeredPane t) {
        JButton[][] buttArr = new JButton[21][21];
        //p.setLayout(new GridLayout(11, 11, 5, 5)); //make a grid layout for the octogons
        p.setLayout(null); //set absolute positioning
        //p.setOpaque(false);
        //p.setBackground(new Color(0, 0, 0, 0));
        for (int i = 0; i < 11; ++i) { //draw the octagon buttons
            for (int j = 0; j < 11; ++j) {
                buttArr[i][j+ j%2] = new JButton();
                buttArr[i][j+j%2].setBounds(30 + j*68, 45 + i*68, 68, 29);
                buttArr[i][j+j%2].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JButton src = (JButton) e.getSource();
                        t.add(new TileDraw());
                        p.repaint();
                        t.repaint();
                        // Board[i][j][0] = getCurrentPlayer();
                        p.remove(src);
                    }
                });
                buttArr[i][j+j%2].setVisible(true);
                p.add(buttArr[i][j+j%2]);
                System.out.println("Button " + i + ", " + j + " created!");
                }
            }
            for(int i = 0; i < 10; i++){ //graw the rhombus buttons
                for(int j = 0; j <  10; j++){
                    buttArr[i][2*j+1] = new JButton();
                buttArr[i][2*j+1].setBounds(88 + j*68, 83 + i*68, 20, 20);
                buttArr[i][2*j+1].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JButton src = (JButton) e.getSource();
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
                }
            }
        p.setPreferredSize(new Dimension(11 * 68, 11 * 68));
        return buttArr;
    }
}