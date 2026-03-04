package org.movshovich;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class buttArrMaker {

    public static JButton[][] initButtArr(JLayeredPane p, JLayeredPane t) {
        JButton[][] buttArr = new JButton[21][21];
        

            for(int i = 0; i < 10; i++){ //draw the rhombus buttons
                for(int j = 0; j <  10; j++){
                    int row = i;
                    int col = j * 2 + 1;
                    buttArr[row][col] = new JButton();
                    buttArr[row][col].setBounds(88 + j * 68, 82 + i * 68, 20, 18);
                    buttArr[row][col].setName(i+ " "+ col); //for testing purposes

                    buttArr[row][col].setContentAreaFilled(false);
                    buttArr[row][col].setBorderPainted(false);

                    buttArr[row][col].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JButton src = (JButton) e.getSource();
                            Color currentColor =  Board.plrByID(Board.currentPlayer).getPlayerColour();
                            TileDraw newTile = new TileDraw(currentColor, false);
                            newTile.setBounds(src.getX()-8 , src.getY()-7, 40, 40);
                            t.add(newTile);
                            p.remove(src);
                            Board.board[row][col][0] = Board.plrByID(Board.currentPlayer).getPlayerId();
                            /*
                            //line fix maybe ??? elbetel???/
                            Board.board[row][col][0] = Board.plrByID(Board.currentPlayer).getPlayerId();
                            Board.currentPlayer *= -1;
                            Board.movingFlag = 0;
                            t.repaint();
                            if(Board.currentPlayer == 1){
                                BoardDraw.nextMove.setText("BLACK to play");
                            } else {
                                BoardDraw.nextMove.setText("WHITE to play");
                            }
                            //Board.movingFlag = 0;
                            //itBoard.board[(src.getY()-82) / 68][((src.getX()-88) / 68)*2+1][0] = Board.plrByID(Board.currentPlayer).getPlayerId();
                        */
                            if (Board.checkWin()) {
                                Board.movingFlag = 0;
                                t.repaint();
                                BoardDraw.overlayPanel.repaint();
                            } else {
                                Board.currentPlayer *= -1;
                                Board.movingFlag = 0;
                                if(Board.currentPlayer == 1){
                                    BoardDraw.nextMove.setText("BLACK to play");
                                } else {
                                    BoardDraw.nextMove.setText("WHITE to play");
                                }
                                t.repaint();
                            }
                        }

                    });
                    p.add(buttArr[row][col]);
                }
            }

        for (int i = 0; i < 11; i++) { //draw the octagon buttons
            for (int j = 0; j < 11; j++) {
                int row = i ;
                int col = j*2;
                buttArr[row][col] = new JButton();
                buttArr[row][col].setBounds(30 + j * 68, 45 + i * 68, 68, 30);
                buttArr[row][col].setName(i+ " "+ col); //for testing purposes

                buttArr[row][col].setContentAreaFilled(false);
                buttArr[row][col].setBorderPainted(false);

                buttArr[row][col].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JButton src = (JButton) e.getSource();
                        Color currentColor = Board.plrByID(Board.currentPlayer).getPlayerColour();
                        TileDraw newTile = new TileDraw(currentColor, true);
                        newTile.setBounds(src.getX(), src.getY()-20, 68, 68);
                        t.add(newTile);
                        p.remove(src);
                        Board.board[row][col][0] = Board.plrByID(Board.currentPlayer).getPlayerId();
                        /*
                        //line fix maybe ??? elbetel???/
                        Board.board[row][col][0] = Board.plrByID(Board.currentPlayer).getPlayerId();
                        Board.currentPlayer *= -1;
                        Board.movingFlag = 0;
                        t.repaint();
                        if(Board.currentPlayer == 1){
                            BoardDraw.nextMove.setText("BLACK to play");
                        } else {
                            BoardDraw.nextMove.setText("WHITE to play");
                        }
                        //Board.movingFlag = 0;
                        //Board.board[(src.getY()-45)/ 68][((src.getX()-30) / 68)*2][0] = Board.plrByID(Board.currentPlayer).getPlayerId();
*/
                        if (Board.checkWin()) {
                            Board.movingFlag = 0;
                            t.repaint();
                            BoardDraw.overlayPanel.repaint();
                        } else {
                            Board.currentPlayer *= -1;
                            Board.movingFlag = 0;
                            if(Board.currentPlayer == 1){
                                BoardDraw.nextMove.setText("BLACK to play");
                            } else {
                                BoardDraw.nextMove.setText("WHITE to play");
                            }
                            t.repaint();
                        }
                    }
                });
                p.add(buttArr[row][col]);
            }
        }

        return buttArr;
    }
}