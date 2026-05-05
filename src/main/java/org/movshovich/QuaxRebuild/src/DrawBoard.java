package org.movshovich.QuaxRebuild.src;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

import javax.swing.*;

/**
 * Visual renderer for the game board.
 * Uses JLayeredPane to manage overlapping graphics components.
 */
public class DrawBoard extends JPanel{
    public static final int OCTAGON_LENGTH = 11;
    public static final int RHOMBUS_LENGTH = 10;
    public static final int OCTAGON_DISTANCE = 68;

    public static JLabel nextMove, bot, player;
    public static JFrame boardFrame; //just for testing
    public static JLayeredPane placedPane;   // Layer for placed tiles
    public static JPanel overlayPanel;       // Main wrapper
    public static JLayeredPane buttonPane;   // Layer for clickable buttons
    public static JLayeredPane strategyPane; // Layer for bot arrows
    public static JLayeredPane popupPane;     // Layer for win/lose windows

    int tileRow;
    int tileCol;
    int numberOfPoints = 8;
    static final Color octagonColor = new Color(173, 111, 49);
    static final Color rhombusColor = new Color(99, 59, 19);

    /** Coordinates for drawing the background octagon shape */
    static int[] octagonX = {50, 78, 98, 98, 78, 50, 30, 30, 50};
    static int[] octagonY = {25, 25, 45, 73, 93, 93, 73, 45, 25};

    /** Default constructor  DrawBoard is instantiated as the bottom rendering layer. */
    public DrawBoard() {}

    /**
     * Primary rendering method for the board background.
     * Draws the brown octagons and rhombuses.
     */
    public void paint(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        buildBoardBackground(graphics);
        graphics.setStroke(new BasicStroke(3));
        for (tileRow = 0; tileRow < OCTAGON_LENGTH; ++tileRow) {
            final int currTileRow = tileRow;
            int[] y = Arrays.stream(octagonY).map(a -> a + currTileRow * OCTAGON_DISTANCE).toArray();
            for (tileCol = 0; tileCol < OCTAGON_LENGTH; ++tileCol) {
                final int currtileCol = tileCol;
                int[] x = Arrays.stream(octagonX).map(a -> a + currtileCol * OCTAGON_DISTANCE).toArray();
                graphics.setColor(octagonColor);
                graphics.fillPolygon(x, y, numberOfPoints);
                for (int i = 0; i < numberOfPoints - 1; i++) {
                    graphics.setColor(Color.black);
                    graphics.drawLine(x[i], y[i], x[i + 1], y[i + 1]);
                }
                graphics.drawLine(x[0], y[0], x[numberOfPoints - 1], y[numberOfPoints - 1]);
            }
        }
        // Draw the current player colour indicator shapes below the board
        buildLowerIcons(graphics);
    }

    private static void buildBoardBackground(Graphics2D graphics) {
        graphics.fillRect(0, 0, 810, 800); // Header area
        graphics.setColor(Color.white);
        graphics.fillRect(0, 45, 810, 710); // Background for board
        graphics.setColor(rhombusColor);
        graphics.fillRect(50, 50, 700, 700); // Main rhombus area
        graphics.setColor(Color.black);
    }

    private static void buildLowerIcons(Graphics2D graphics) {
        graphics.setColor(Game.plrByID(Game.getCurrentPlayer()).getPlayerColour());
        int[] newX = Arrays.stream(octagonX).map(a -> a + 200).toArray();
        int[] newY = Arrays.stream(octagonY).map(a -> a + 800).toArray();
        graphics.fillPolygon(newX, newY, 8);
        graphics.setColor(Color.BLACK);
        graphics.drawPolygon(newX, newY, 8);
        int[] newNewX = {18 + 330, 38 + 330, 18 + 330, -2 + 330};
        int[] newNewY = {25 + 815, 45 + 815, 65 + 815, 45 + 815};
        graphics.setColor(Game.plrByID(Game.getCurrentPlayer()).getPlayerColour());
        graphics.fillPolygon(newNewX, newNewY, 4);
        graphics.setColor(Color.BLACK);
        graphics.drawPolygon(newNewX, newNewY, 4);
    }

    /** Creates the Bot and Player colour coded label icons shown in the dashboard. */
    private static void createIcons() {
        bot = new JLabel("    Bot");
        bot.setForeground(Color.WHITE);
        bot.setBackground(new Color(45, 45, 45));
        bot.setBounds(560, 830, 50, 50);
        bot.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
        player = new JLabel("  Player");
        player.setForeground(new Color(45, 45, 45));
        player.setBackground(Color.WHITE);
        player.setBounds(620, 830, 55, 50);
        player.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
    }

    /** Sets up the UI layering order (Bottom to Top) */
    private static void addAll() {
        overlayPanel.add(popupPane);
        overlayPanel.add(strategyPane); // Top Layer (Strategy overlay)
        overlayPanel.add(buttonPane);
        overlayPanel.add(placedPane); // Middle Layer (Tiles being placed)
        overlayPanel.add(popupPane, null, 0);
        overlayPanel.add(new DrawBoard()); // Bottom Layer (The Brown Board)
        boardFrame.add(overlayPanel);
    }

    /** Initialises the dashboard: turn label, player icons, and board edge numbers/letters. */
    public static void initDash() {
        nextMove = new JLabel("BLACK to play");
        nextMove.setName("nextMoveLabelName");
        nextMove.setBounds(390, 830, 150, 50);
        //display text to display which player is which colour
        createIcons();
        //do the numbers and letters at edge of board

        for (int i = 0; i < OCTAGON_LENGTH; i++) {
            JLabel num = new JLabel(Integer.toString(i + 1));
            JLabel chars = new JLabel(Character.toString(i + 65));
            num.setBounds(10, 50 + i * 68, 15, 15);
            chars.setBounds(60 + i * 68, 10, 15, 15);
            chars.setForeground(Color.WHITE);
            num.setName(Integer.toString(i + 1)); //for testing
            chars.setName(Character.toString((char) (i + 65))); //for testing
            placedPane.add(num);
            placedPane.add(chars);
        }
        //add labels
        nextMove.setOpaque(true);
        bot.setOpaque(true);
        player.setOpaque(true);
        placedPane.add(nextMove);
        placedPane.add(player);
        placedPane.add(bot);
    }

    /** Initializes the main JFrame and all layered panels */
    public static void initBoard() {
        //Init board name and closing function
        boardFrame = new JFrame("Quax Player vs Bot");
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //overlay panel to place above
        overlayPanel = new JPanel();
        overlayPanel.setLayout(new OverlayLayout(overlayPanel));
        overlayPanel.setPreferredSize(new Dimension(810, 1000));
        //Create Panes for placing buttons
        buttonPane = new JLayeredPane();
        placedPane = new JLayeredPane();
        placedPane.setSize(810, 1000);
        //strategy overlay panel
        strategyPane = new JLayeredPane(); // top layer -> strategy arrows drawn here
        strategyPane.setSize(810, 1000);
        strategyPane.setOpaque(false);
        popupPane = new JLayeredPane();
        popupPane.setSize(810, 1000);
        //create label
        initDash();
        addAll();
        //Set board properties
        boardFrame.setSize(810, 1000);
        boardFrame.setLocationRelativeTo(null);
        boardFrame.setVisible(true);
    }

    /** Triggers a repaint on all UI layers to reflect the latest game state. */
    public static void repaintAll() {
        overlayPanel.repaint();
        placedPane.repaint();
        buttonPane.repaint();
        strategyPane.repaint();
    }

    /** Clears all placed tiles, buttons, and strategy overlays, then reinitialises the dashboard. */
    public static void resetBoard() {
        placedPane.removeAll();
        buttonPane.removeAll();
        strategyPane.removeAll();
        initDash();
        repaintAll();
    }

    /**
     * Shows the bot strategy overlay: red arrows from each placed tile along its
     * A* path to the highest weight goal, the goal highlighted in green, and a
     * textual description below the board.
     */
    public static void showStrategy() {
        if (Bot.getPlaced().isEmpty()) return;
        DrawBoard.strategyPane.removeAll();
        // Build a full size transparent overlay to draw on
        JPanel overlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D canvas = (Graphics2D) g;
                buildAllPaths(canvas);
            }
        };
        overlay.setOpaque(false);
        overlay.setBounds(0, 0, 810, 1000);
        DrawBoard.strategyPane.add(overlay);
        DrawBoard.strategyPane.setComponentZOrder(overlay, 0);
        createDesc(overlay);
    }

    /** Returns the pixel center of a tile for drawing arrows */
    private static Point tileCenter(Tile tile) {
        int px;
        int py;
        if (tile.getX() % 2 == 0) {
            px = (tile.getX() / 2) * DrawBoard.OCTAGON_DISTANCE + 30 + DrawBoard.OCTAGON_DISTANCE / 2;
            py = tile.getY() * DrawBoard.OCTAGON_DISTANCE + 25 + DrawBoard.OCTAGON_DISTANCE / 2;
        } else {
            px = (tile.getX() / 2) * DrawBoard.OCTAGON_DISTANCE + 80 + 20;
            py = tile.getY() * DrawBoard.OCTAGON_DISTANCE + 75 + 20;
        }
        return new Point(px, py);
    }

    /* Draw a line with an arrowhead at to */
    private static void drawArrow(Graphics2D canvas, Point from, Point to) {
        canvas.drawLine(from.x, from.y, to.x, to.y);
        double angle = Math.atan2(to.y - from.y, to.x - from.x);
        int arrowLen = 10;
        double arrowAngle = Math.toRadians(30);
        int ax1 = (int) (to.x - arrowLen * Math.cos(angle - arrowAngle));
        int ay1 = (int) (to.y - arrowLen * Math.sin(angle - arrowAngle));
        int ax2 = (int) (to.x - arrowLen * Math.cos(angle + arrowAngle));
        int ay2 = (int) (to.y - arrowLen * Math.sin(angle + arrowAngle));
        canvas.drawLine(to.x, to.y, ax1, ay1);
        canvas.drawLine(to.x, to.y, ax2, ay2);
    }

    private static void createDesc(JPanel overlay) {
        // Textual description shown to the bottom of the board
        JLabel strategyDescription = new JLabel(
                "<html><b>Bot Strategy:</b><br>The bot uses A* search to find the "
                        + "highest-weight path across the board.<br>It evaluates all reachable "
                        + "paths, selects the goal tile with the maximum weight,<br>and places "
                        + "tiles along the optimal route each turn.<br><br>"
                        + "<b>Legend:</b> Red arrows = computed paths &nbsp; "
                        + "Green circle = chosen goal tile</html>"
        );
        strategyDescription.setBounds(0, 780, 810, 90);
        strategyDescription.setFont(new Font("Arial", Font.PLAIN, 11));
        strategyDescription.setForeground(new Color(30, 30, 30));
        strategyDescription.setOpaque(true);
        strategyDescription.setBackground(new Color(255, 255, 220, 220));
        DrawBoard.strategyPane.add(strategyDescription);

        DrawBoard.repaintAll();
    }

    private static void createGoalOval(Graphics2D canvas, Tile goal) {
        Point c = tileCenter(goal);
        canvas.setColor(new Color(0, 180, 0, 160));
        canvas.fillOval(c.x - 18, c.y - 18, 36, 36);
        canvas.setColor(Color.BLACK);
        canvas.setStroke(new BasicStroke(2));
        canvas.drawOval(c.x - 18, c.y - 18, 36, 36);
        canvas.setColor(Color.WHITE);
        canvas.setFont(new Font("Arial", Font.BOLD, 11));
        canvas.drawString("" + goal.getWeight(), c.x - 5, c.y + 4);
    }

    public static void buildAllPaths(Graphics2D canvas) {
        canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // For each tile in placed, compute the A* path and draw arrows
        ArrayList<Tile> placedList = new ArrayList<>(Bot.getPlaced()); // snapshot, no new data
        Tile goal = Board.largestWeight(Bot.getPlaced().peek());
        for (Tile start : placedList) {
            Queue<Tile> pathQ = new LinkedList<>(Bot.createPath(start));
            List<Tile> pathTiles = new ArrayList<>(pathQ);
            if (pathTiles.size() < 2) continue;
            // Draw arrows along path segments
            canvas.setColor(new Color(220, 50, 50, 200)); // semi transparent red
            canvas.setStroke(new BasicStroke(2.5f));
            for (int i = 0; i < pathTiles.size() - 1; i++) {
                Point from = tileCenter(pathTiles.get(i));
                Point to = tileCenter(pathTiles.get(i + 1));
                drawArrow(canvas, from, to);
            }
            // Draw weight label at goal tile
            Tile last = pathTiles.get(pathTiles.size() - 1);
            Point center = tileCenter(last);
            canvas.setColor(new Color(255, 50, 50));
            canvas.setFont(new Font("Arial", Font.BOLD, 13));
            canvas.drawString("W:" + last.getWeight(), center.x - 10, center.y - 5);
        }
        // Highlight the goal tile (highest weight) in green if not yet placed
        if (goal != null && goal.getValue() == 0) {
            createGoalOval(canvas, goal);
        }
    }
}
