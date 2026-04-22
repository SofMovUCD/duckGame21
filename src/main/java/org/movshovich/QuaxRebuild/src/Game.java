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
    private static JButton showStrategyBut;
    private static JButton hideStrategyBut;
    private static boolean ongoing;
    private static boolean piRulePressed = false;
    

    public Game() {
        System.out.println("Game start");
        plrList.add(new Bot(1));
        plrList.add(new Player(-1));
        movingFlag = false;
        whiteFirst = true;
        ongoing = true;
        DrawBoard.initBoard();
        Board board = new Board();
        initPiRuleButton();
        initStrategyButtons();
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

    public static void initStrategyButtons() {
        // Show Strategy button
        showStrategyBut = new JButton("Show Strategy");
        showStrategyBut.setName("Show Strategy");
        showStrategyBut.setBounds(690, 830, 150, 50);
        showStrategyBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Bot.showStrategy();
                // swap Show -> Hide
                DrawBoard.placedPane.remove(showStrategyBut);
                DrawBoard.placedPane.add(hideStrategyBut);
                DrawBoard.placedPane.setComponentZOrder(hideStrategyBut, 0);
                DrawBoard.repaintAll();
            }
        });

        // Hide Strategy button (starts hidden)
        hideStrategyBut = new JButton("Hide Strategy");
        hideStrategyBut.setName("Hide Strategy");
        hideStrategyBut.setBounds(690, 830, 150, 50);
        hideStrategyBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Bot.hideStrategy();
                DrawBoard.placedPane.remove(hideStrategyBut);
                DrawBoard.placedPane.add(showStrategyBut);
                //DrawBoard.placedPane.setComponentZOrder(showStrategyBut, 0);
                DrawBoard.repaintAll();
            }
        });

        DrawBoard.placedPane.add(showStrategyBut);
        //DrawBoard.placedPane.setComponentZOrder(showStrategyBut, 0);
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
        WL.setBounds(250, 450, 300, 200);
        JButton AB = new JButton("Play Again");
        AB.setBounds(20, 100, 100, 50);
        AB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DrawBoard.resetBoard();
                Board board = new Board();
                initStrategyButtons();
                Bot.piRule();

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

                DrawBoard.placedPane.remove(WL);
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

        JLabel results = new JLabel("Winner: " + (winner.getPlayerId() == 1? "Black" : "White"));
        results.setBounds(10, -30, 90, 90);
        JLabel score = new JLabel("Score:");
        score.setBounds(10, -10, 90, 90);
        JLabel P1Score = new JLabel("Bot: " + plrList.get(0).getWins() + " - " + plrList.get(0).getlosses());
        P1Score.setBounds(10, 10, 90, 90);
        JLabel P2Score = new JLabel("Player: " + plrList.get(1).getWins() + " - " + plrList.get(1).getlosses());
        P2Score.setBounds(10, 30, 90, 90);
        JLabel PA = new JLabel("Play Again?");
        PA.setBounds(40, 30, 90, 90);

        WL.add(results);
        WL.add(score);
        WL.add(P1Score);
        WL.add(P2Score);
        WL.add(PA);
            

        DrawBoard.placedPane.add(WL);
        DrawBoard.repaintAll();
    }

    public static void main(String[] args) {
        new Game();

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
            if(winner.getPlayerColour() == Color.WHITE) {
                DrawBoard.nextMove.setText("WHITE WINS!!");
            }
            else{
                DrawBoard.nextMove.setText("BLACK WINS!!");
            }
            
            winner.incrementWins();
            loser.incrementLosses();
            winLoseWind(winner, loser);
            while (movingFlag) {
                System.out.print("");
            }

        }
    }
}
