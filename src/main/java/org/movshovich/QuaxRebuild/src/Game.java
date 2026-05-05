package org.movshovich.QuaxRebuild.src;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

/**
 * Main Controller class for the Quax game.
 * Manages game state, turn logic, and UI interactions.
 */
public class Game {

    // Static Constants & State Variables
    public static Color playingBlack = new Color(45, 45, 45);
    private static int currentPlayer = 1; // 1 for Black, -1 for White
    private static boolean movingFlag;   // True when waiting for a move to complete
    private static boolean whiteFirst;  // Tracks if it's the start of the game
    private static List<Player> plrList = new ArrayList<>();
    private static JButton piRuleBut;
    private static JButton showStrategyBut;
    private static JButton hideStrategyBut;
    private static boolean ongoing;    // Is the game currently active
    private static boolean piRulePressed = false;

    /**
     * Initializes a new game instance, sets up players (Bot vs Human),
     * and resets the game board UI.
     */
    public Game() {
        System.out.println("Game start"); //for testing
        plrList.clear();
        plrList.add(new Bot(1));       // Typically Black
        plrList.add(new Player(-1));  // Typically White
        movingFlag = false;
        whiteFirst = true;
        ongoing = true;
        DrawBoard.initBoard();
        new Board();
        initPiRuleButton();
        initStrategyButtons();
    }

    // Getters and State Helpers

    /**
     * Returns the ID of the player whose turn it currently is (1=BLACK, -1=WHITE).
     */
    public static int getCurrentPlayer() {return currentPlayer;}

    /**
     * Toggles movingFlag called by Player and Bot after a move starts or ends.
     */
    public static void flipMovingFlag() {movingFlag = !movingFlag;}

    /**
     * Returns true while a move is in progress. Used in tests.
     */
    public boolean getMovingFlag() {return movingFlag;}

    /**
     * Returns true if we are still in the first move window where Pi Rule is available.
     */
    public boolean isWhiteFirst() {return whiteFirst;}

    /**
     * Returns the ordered player list (index 0 = Bot, index 1 = human Player).
     */
    public static List<Player> getPlrList() {return plrList;}

    /**
     * Logic to swap turns between players.
     * Updates the UI text and removes the Pi Rule button after the first move.
     */
    public static void nextTurn() {
        if (whiteFirst && currentPlayer == -1) {
            whiteFirst = false;
            DrawBoard.buttonPane.remove(piRuleBut);
        }
        currentPlayer *= -1; // Toggle between 1 and -1
        if (currentPlayer == 1) DrawBoard.nextMove.setText("BLACK to play");
        else DrawBoard.nextMove.setText("WHITE to play");
        DrawBoard.repaintAll();
    }

    /**
     * Finds a player object in the list based on their ID.
     */
    public static Player plrByID(int ID) {
        for (Player plr : plrList) {
            if (plr.getPlayerId() == ID) {
                return plr;
            }
        }
        throw new IllegalArgumentException("No Players of this ID");
    }

    /**
     * Helper to return a generic value based on which player is active.
     */
    public static <E> Object valueForID(E a, E b, Player plr) {
        return (plr.getPlayerId() == 1 ? a : b);
    }

    // Button Initialization & Action Listeners
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

    /**
     * Creates Show/Hide Strategy buttons and registers them on the board.
     */
    public static void initStrategyButtons() {
        showStrategyBut = buildShowStrategyButton();
        hideStrategyBut = buildHideStrategyButton();
        DrawBoard.placedPane.add(showStrategyBut);
    }

    /**
     * Builds the Show Strategy button clicking it reveals the A* overlay and swaps to Hide.
     */
    private static JButton buildShowStrategyButton() {
        JButton btn = new JButton("Show Strategy");
        btn.setName("Show Strategy");
        btn.setBounds(690, 830, 150, 50);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DrawBoard.showStrategy();
                DrawBoard.placedPane.remove(showStrategyBut);
                DrawBoard.placedPane.add(hideStrategyBut);
                DrawBoard.placedPane.setComponentZOrder(hideStrategyBut, 0);
                DrawBoard.repaintAll();
            }
        });
        return btn;
    }

    /**
     * Builds the Hide Strategy button clicking it clears the overlay and swaps back to Show.
     */
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

    /**
     * Implements the "Pie Rule" (Swap Rule).
     * The second player can choose to swap roles with the first player
     * to counteract any first move advantage.
     */
    public static void piRule() {
        for (Player plr : plrList) {
            plr.setPlayerId(plr.getPlayerId() * -1);
            plr.refreshPlayerColour();
            Bot.piRule();
        }

        currentPlayer *= -1;
        movingFlag = false;
        whiteFirst = false;
        piRulePressed = true;

        // Update UI colors to reflect the swap
        DrawBoard.bot.setForeground(playingBlack);
        DrawBoard.bot.setBackground(Color.WHITE);
        DrawBoard.player.setForeground(Color.WHITE);
        DrawBoard.player.setBackground(playingBlack);
        DrawBoard.buttonPane.remove(piRuleBut);
        Board.piRuleWeight();
    }

    /**
     * Creates and displays the end game result window.
     */
    private static void winLoseWind(Player winner, Player loser) {
        movingFlag = true;
        JInternalFrame resultFrame = buildResultFrame();
        resultFrame.add(buildPlayAgainButton(resultFrame));
        resultFrame.add(buildQuitButton());
        addScoreLabels(resultFrame, winner);
        DrawBoard.popupPane.add(resultFrame);
        DrawBoard.repaintAll();
    }

    /**
     * Creates the result dialog frame. Named "WL" so UI tests can locate it by name.
     */
    private static JInternalFrame buildResultFrame() {
        JInternalFrame frame = new JInternalFrame();
        frame.setVisible(true);
        frame.setBounds(250, 450, 300, 200);
        frame.setName("WL"); // used in tests
        return frame;
    }

    /**
     * Builds the Play Again button clicking it calls resetForNewGame.
     */
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

    /**
     * Resets game state, scores (if swapped), and board for a fresh round.
     */
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

    /**
     * Builds the Quit button clicking it exits the application. Named "Quit" for tests.
     */
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

    /**
     * Adds winner name, score, and Play Again prompt labels to the result dialog.
     */
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

    /**
     * Triggered when a win condition is met. Updates stats and shows popup.
     */
    public static void gameFinished() {
        Player winner = (-currentPlayer == 1 ? plrByID(1) : plrByID(-1));
        Player loser = plrByID(-winner.getPlayerId());
        if (winner.getPlayerColour() == Color.WHITE) {
            DrawBoard.nextMove.setText("WHITE WINS!!");
        } else {
            DrawBoard.nextMove.setText("BLACK WINS!!");
        }
        winner.incrementWins();
        loser.incrementLosses();
        winLoseWind(winner, loser);
        currentPlayer = winner.getPlayerId();
        DrawBoard.repaintAll();
        // Busy wait loop to hold the execution while the win screen is visible
        while (movingFlag) {
            System.out.print("");
        }
    }

    /**
     * Main game loop. Initialises the game then repeatedly:
     * runs turns until a win, shows the Pi Rule button on WHITE's first turn,
     * then calls gameFinished() to handle the result and wait for replay.
     */
    public static void main(String[] args) {
        new Game();
        while (true) {
            if (ongoing) {
                do {
                    // Show Pi Rule button if it's the second turn and not yet swapped
                    if ((currentPlayer == -1) && whiteFirst && piRuleBut.getParent() == null) {
                        DrawBoard.buttonPane.add(piRuleBut);
                        DrawBoard.buttonPane.setComponentZOrder(piRuleBut, 0);
                    }
                    plrByID(currentPlayer).makeMove();
                    // Wait for move selection
                    while (movingFlag) {
                        System.out.print("");
                    }
                    DrawBoard.overlayPanel.remove(DrawBoard.buttonPane);
                    nextTurn();
                } while (!Board.checkWin(plrByID(-currentPlayer))); // Check if the last player won
            }
            ongoing = false;
            gameFinished();
        }
    }
}