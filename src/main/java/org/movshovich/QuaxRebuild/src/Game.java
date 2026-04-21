package org.movshovich.QuaxRebuild.src;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class Game {

    public static Color playingBlack = new Color(45,45,45);

    private static int currentPlayer = 1;
    private static boolean movingFlag;
    private static boolean whiteFirst;
    private static List<Player> plrList = new ArrayList<>();
    private static JButton piRuleBut;
    private static boolean ongoing;
    private static boolean piRulePressed = false;
    

    public Game() {

        plrList.add(new Bot(1));
        plrList.add(new Player(-1));
        movingFlag = false;
        whiteFirst = true;
        ongoing = true;
        DrawBoard.initBoard();
        Board board = new Board();
        initPiRuleButton();
    }

    public static int getCurrentPlayer() {return currentPlayer;}
    public static void flipMovingFlag() {movingFlag = !movingFlag;}

    public boolean getMovingFlag() {return movingFlag;}
    public boolean isWhiteFirst() {return whiteFirst;}
    public static List<Player> getPlrList() {return plrList;}

    public static void nextTurn() { 

        if (whiteFirst && currentPlayer == -1) {
            whiteFirst = false;
            DrawBoard.buttonPane.remove(piRuleBut);
        }

        currentPlayer *= -1;

        if(currentPlayer == 1){
            DrawBoard.nextMove.setText("BLACK to play");
        } else {
            DrawBoard.nextMove.setText("WHITE to play");
        }
        //System.out.println(currentPlayer);
        DrawBoard.repaintAll();
    }

    public static Player plrByID(int ID) {
		for (Player plr : plrList) {
			if (plr.getPlayerId() == ID) {
				return plr;
			}
		}
		throw new IllegalArgumentException("No Players of this ID");
	}

    public static <E> Object valueForID(E a, E b, Player plr) {
        return (plr.getPlayerId()  == 1 ? a : b);
    }

    public static void initPiRuleButton() {
        piRuleBut = new JButton("Activate Pi Rule");
        piRuleBut.setName("Activate Pi Rule");
        piRuleBut.setBounds(50, 830, 150, 50);
        piRuleBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                piRule();
            }
        });
    }

    public static void piRule() {
        //System.out.println("I am a PI Rule");
        for (Player plr: plrList) {
            plr.setPlayerId(plr.getPlayerId() * -1);
            plr.refreshPlayerColour();
            Bot.piRule();
        }

        currentPlayer *= -1;
        movingFlag = false;
        whiteFirst = false;
        piRulePressed = true;
        //change colours
        DrawBoard.bot.setForeground(playingBlack);
        DrawBoard.bot.setBackground(Color.WHITE);
        DrawBoard.player.setForeground(Color.WHITE);
        DrawBoard.player.setBackground(playingBlack);
        DrawBoard.buttonPane.remove(piRuleBut);
        Board.piRuleWeight();
        //DrawBoard.buttonPane.repaint();
    }

    private static void winLoseWind(Player winner, Player loser) {
        movingFlag = true;
        JInternalFrame WL = new JInternalFrame();
        WL.setVisible(true);
        WL.setBounds(250, 450, 400, 200);
        JButton AB = new JButton("Play Again");
        AB.setBounds(20, 100, 100, 50);
        AB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movingFlag = false;
                whiteFirst = true;
                ongoing = true;
                currentPlayer = 1;

                if (piRulePressed) {
                    for (Player plr: plrList) {
                        plr.setPlayerId(-plr.getPlayerId());
                    }
                    piRulePressed = false;
                }

                DrawBoard.resetBoard();
                Board board = new Board();
                initPiRuleButton();
                DrawBoard.placedPane.remove(WL);
                Bot.piRule();
            }
        });
        WL.add(AB);

        JButton QB = new JButton("Quit");
        QB.setBounds(145, 100, 100, 50);
        QB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        WL.add(QB);

        JLabel results = new JLabel("Winner: " + (winner.getPlayerId() == 1? "Black" : "White") + "\n" + "Score:\n" + "\n" + "\n" + "Play Again?");
        results.setBounds(360, 460, 90, 90);

        WL.add(results);

        DrawBoard.placedPane.add(WL);
        DrawBoard.repaintAll();
    }

    public static void main(String[] args) {
        Game quax = new Game();

        while(true) {
            if (ongoing) {
                do {
                    if ((currentPlayer == -1) && whiteFirst && piRuleBut.getParent() == null) {
                        DrawBoard.buttonPane.add(piRuleBut);
                        DrawBoard.buttonPane.setComponentZOrder(piRuleBut, 0);
                        //zDrawBoard.buttonPane.repaint();
                    }

                    plrByID(currentPlayer).makeMove();
                    while (movingFlag) {
                        System.out.print("");
                    }
                    DrawBoard.overlayPanel.remove(DrawBoard.buttonPane);
                    nextTurn();
                } while (!Board.checkWin(plrByID(-currentPlayer)));
            }
            ongoing = false;

            Player winner = (-currentPlayer == 1? plrByID(1) : plrByID(-1));
            Player loser = plrByID(-winner.getPlayerId());
            
            winner.incrementWins();
            loser.incrementLosses();
            winLoseWind(winner, loser);
            while (movingFlag) {
                System.out.print("");
            }

        }
    }
}
