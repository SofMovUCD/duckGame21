package org.movshovich.QuaxRebuild.src;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

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
        System.out.println("Game start"); //for testing
        plrList.clear();
        plrList.add(new Bot(1));
        plrList.add(new Player(-1));
        movingFlag = false;
        whiteFirst = true;
        ongoing = true;
        DrawBoard.initBoard();
        new Board();
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
        showStrategyBut = buildShowStrategyButton();
        hideStrategyBut = buildHideStrategyButton();
        DrawBoard.placedPane.add(showStrategyBut);
    }

    private static JButton buildShowStrategyButton() {
        JButton btn = new JButton("Show Strategy");
        btn.setName("Show Strategy");
        btn.setBounds(690, 830, 150, 50);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Bot.showStrategy();
                DrawBoard.placedPane.remove(showStrategyBut);
                DrawBoard.placedPane.add(hideStrategyBut);
                DrawBoard.placedPane.setComponentZOrder(hideStrategyBut, 0);
                DrawBoard.repaintAll();
            }
        });
        return btn;
    }

    private static JButton buildHideStrategyButton() {
        JButton btn = new JButton("Hide Strategy");
        btn.setName("Hide Strategy");
        btn.setBounds(690, 830, 150, 50);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Bot.hideStrategy();
                DrawBoard.placedPane.remove(hideStrategyBut);
                DrawBoard.placedPane.add(showStrategyBut);
                DrawBoard.repaintAll();
            }
        });
        return btn;
    }

    public static void piRule() {
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
        JInternalFrame resultFrame = buildResultFrame();
        resultFrame.add(buildPlayAgainButton(resultFrame));
        resultFrame.add(buildQuitButton());
        addScoreLabels(resultFrame, winner);
        DrawBoard.popupPane.add(resultFrame);
        DrawBoard.repaintAll();
    }

    private static JInternalFrame buildResultFrame() {
        JInternalFrame frame = new JInternalFrame();
        frame.setVisible(true);
        frame.setBounds(250, 450, 300, 200);
        frame.setName("WL"); // used in tests
        return frame;
    }

    private static JButton buildPlayAgainButton(JInternalFrame resultFrame) {
        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setName("Play Again");
        playAgainButton.setBounds(20, 100, 100, 50);
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForNewGame(resultFrame);
            }
        });
        return playAgainButton;
    }

    private static void resetForNewGame(JInternalFrame resultFrame) {
        DrawBoard.resetBoard();
        new Board();
        initStrategyButtons();
        Bot.piRule();
        if (piRulePressed) {
            for (Player plr : plrList) {
                plr.setPlayerId(-plr.getPlayerId());
                plr.refreshPlayerColour();
            }
            piRulePressed = false;
        }
        whiteFirst = true;
        currentPlayer = 1;
        Bot.endReached = false;
        DrawBoard.popupPane.remove(resultFrame);
        ongoing = true;
        movingFlag = false;
    }

    private static JButton buildQuitButton() {
        JButton quitButton = new JButton("Quit");
        quitButton.setName("Quit"); // used in tests
        quitButton.setBounds(145, 100, 100, 50);
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        return quitButton;
    }

    private static void addScoreLabels(JInternalFrame frame, Player winner) {
        JLabel winnerLabel = new JLabel("Winner: " + (winner.getPlayerId() == 1 ? "Black" : "White"));
        winnerLabel.setBounds(10, -30, 90, 90);
        JLabel scoreLabel = new JLabel("Score:");
        scoreLabel.setBounds(10, -10, 90, 90);
        JLabel botScoreLabel = new JLabel("Bot: " + plrList.get(0).getWins() + " - " + plrList.get(0).getlosses());
        botScoreLabel.setBounds(10, 10, 90, 90);
        JLabel playerScoreLabel = new JLabel("Player: " + plrList.get(1).getWins() + " - " + plrList.get(1).getlosses());
        playerScoreLabel.setBounds(10, 30, 90, 90);
        JLabel playAgainPrompt = new JLabel("Play Again?");
        playAgainPrompt.setBounds(40, 30, 90, 90);
        frame.add(winnerLabel);
        frame.add(scoreLabel);
        frame.add(botScoreLabel);
        frame.add(playerScoreLabel);
        frame.add(playAgainPrompt);
    }

    public static void gameFinished() {
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
            currentPlayer = winner.getPlayerId();
            DrawBoard.repaintAll();
            while (movingFlag) {
                System.out.print("");
            }
    }

    public static void main(String[] args) {
        new Game();

        while(true) {
            if (ongoing) {
                do {
                    if ((currentPlayer == -1) && whiteFirst && piRuleBut.getParent() == null) {
                        DrawBoard.buttonPane.add(piRuleBut);
                        DrawBoard.buttonPane.setComponentZOrder(piRuleBut, 0);
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
            gameFinished();
        }
    }
}
