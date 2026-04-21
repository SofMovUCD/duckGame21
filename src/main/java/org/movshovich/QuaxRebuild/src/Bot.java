package org.movshovich.QuaxRebuild.src;
import java.awt.*;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import javax.swing.JPanel;
import java.awt.Font;

public class Bot extends Player {
    private static Queue<Tile> path  = new LinkedList<>();
    private static Stack<Tile> placed = new Stack<>();

    public Bot(int playerId) {
        super(playerId);
    }

    public static void piRule() {
        path.clear();
        placed.clear();
    }

    @Override
    public void makeMove() {
        Tile next;

        if (placed.isEmpty()) {
            next = createPath(findNewStart()).poll();
        }

        else {
            path = createPath(placed.peek());

            if (path == null) {
                placed.pop();
                next = createPath(placed.peek()).poll();
            }
            else {
                path.poll();
                next = createPath(path.poll()).poll();
            }
        }

        System.out.println(next);

        next.getTileButton().doClick(); //place tile
        Game.flipMovingFlag(); //go to next move
        placed.push(next); //add placed tile to stack

    }

    private Tile findNewStart() {
        Tile newStart;

        for (int i = 0; i < Board.BOARD_X; i += 2) {
            newStart = (Tile) Game.valueForID(Board.getTile(i, 0), Board.getTile(0, i/2), this);
            if (!newStart.isBlocked() && !placed.contains(newStart) && newStart.getValue() == 0) {
                return newStart;
            }
        }
        return null;
    }

    private Queue<Tile> createPath(Tile start) {
            return A_Star(start, Board.largestWeight());
    }

    private static Queue<Tile> A_Star(Tile start, Tile goal) {
    // Initialize open and closed lists
        Queue<Tile> openList = new PriorityQueue<>((a, b) -> a.getF() - b.getF());          // Nodes to be evaluated
        List<Tile> closedList = new ArrayList<>();            // Nodes already evaluated
        openList.add(start);

        // Initialize node properties
        start.setG(0);                // Cost from start to start is 0
        start.setH(heuristic(start, goal));  // Estimate to goal
        start.setF();       // Total estimated cost
        start.setParent(null);              // For path reconstruction
        while (!openList.isEmpty()) {
            // Get node with lowest f value - implement using a priority queue
           // for faster retrieval of the best node
            Tile current = openList.peek();

            // Check if we've reached the goal
            if (current == goal) {
                return reconstruct_path(current);
            }

            // Move current node from open to closed list
            openList.remove(current);
            closedList.add(current);

            // Check all neighboring nodes
            for (Tile neighbor : Board.getNeighbours(current)){
                if (closedList.contains(neighbor) || neighbor.getValue() != 0|| neighbor.isBlocked()) {
                    continue;  // Skip already evaluated nodes
                }
                // Calculate tentative g score
                int tentative_g = current.getG() + 1;
                if (!openList.contains(neighbor)) openList.add(neighbor);
                else if (tentative_g >= neighbor.getG()) continue;  // This path is not better
            
                // This path is the best so far
                neighbor.setParent(current);
                neighbor.setG(tentative_g);
                neighbor.setH(heuristic(neighbor, goal));
                neighbor.setF();
            
            }
        }
    return null;  // No path exists
    }

    private static Queue<Tile> reconstruct_path(Tile current) {
        Deque <Tile> path = new LinkedList<>();
        while (current != null) {
            path.addFirst(current);
            current = current.getParent();
        }
        return path;
    }

    private static int heuristic(Tile a, Tile b) {
        
        return (int) Math.sqrt(Math.pow((b.getX() - a.getX()),2) + Math.pow((b.getY() - a.getY()),2));
    }

    private Rectangle drawnBounds(Tile tile) {
        if (tile.getX() % 2 ==0) {
            return new Rectangle((tile.getX()/2)*DrawBoard.OCTAGON_DISTANCE + 30, 
                                   (tile.getY())*DrawBoard.OCTAGON_DISTANCE + 25, 
                                       DrawBoard.OCTAGON_DISTANCE, 
                                       DrawBoard.OCTAGON_DISTANCE);
        } else {
            return new Rectangle((tile.getX()/2)*DrawBoard.OCTAGON_DISTANCE + 80, 
                                 (tile.getY())*DrawBoard.OCTAGON_DISTANCE + 75, 
                                 40, 
                                 40);
        }
    }
    /* Returns the pixel center of a tile for drawing arrows */
    private static Point tileCenter(Tile tile) {
        if (tile.getX() % 2 == 0) {
            int px = (tile.getX()/2) * DrawBoard.OCTAGON_DISTANCE + 30 + DrawBoard.OCTAGON_DISTANCE / 2;
            int py = tile.getY() * DrawBoard.OCTAGON_DISTANCE + 25 + DrawBoard.OCTAGON_DISTANCE / 2;
            return new Point(px, py);
        } else {
            int px = (tile.getX()/2) * DrawBoard.OCTAGON_DISTANCE + 80 + 20;
            int py = tile.getY() * DrawBoard.OCTAGON_DISTANCE + 75 + 20;
            return new Point(px, py);
        }
    }

    /*Show the bot strategy: draw arrows from each placed tile along its A* path,
     *  with the weight of the goal tile shown. Uses placed stack. */
    public static void showStrategy() {
        DrawBoard.strategyPane.removeAll();

        // Build a full size transparent overlay to draw on
        JPanel overlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // For each tile in placed, compute the A* path and draw arrows
                List<Tile> placedList = new ArrayList<>(placed); // snapshot, no new data
                Tile goal = Board.largestWeight();

                for (Tile start : placedList) {
                    Queue<Tile> pathQ = computePath(start);
                    if (pathQ == null) continue;

                    List<Tile> pathTiles = new ArrayList<>(pathQ);
                    if (pathTiles.size() < 2) continue;

                    // Draw arrows along path segments
                    g2.setColor(new Color(220, 50, 50, 200)); // semi transparent red
                    g2.setStroke(new BasicStroke(2.5f));

                    for (int i = 0; i < pathTiles.size() - 1; i++) {
                        Point from = tileCenter(pathTiles.get(i));
                        Point to   = tileCenter(pathTiles.get(i + 1));
                        drawArrow(g2, from, to);
                    }

                    // Draw weight label at goal tile
                    if (!pathTiles.isEmpty()) {
                        Tile last = pathTiles.get(pathTiles.size() - 1);
                        Point center = tileCenter(last);
                        g2.setColor(new Color(255, 50, 50));
                        g2.setFont(new Font("Arial", Font.BOLD, 13));
                        g2.drawString("W:" + last.getWeight(), center.x - 10, center.y - 5);
                    }
                }

                // Highlight the goal tile (highest weight) in green if not yet placed
                if (goal != null && goal.getValue() == 0) {
                    Point c = tileCenter(goal);
                    g2.setColor(new Color(0, 180, 0, 160));
                    g2.fillOval(c.x - 18, c.y - 18, 36, 36);
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawOval(c.x - 18, c.y - 18, 36, 36);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.BOLD, 11));
                    g2.drawString("" + goal.getWeight(), c.x - 5, c.y + 4);
                }
            }

            /* Draw a line with an arrowhead at to */
            private void drawArrow(Graphics2D g2, Point from, Point to) {
                g2.drawLine(from.x, from.y, to.x, to.y);
                double angle = Math.atan2(to.y - from.y, to.x - from.x);
                int arrowLen = 10;
                double arrowAngle = Math.toRadians(30);
                int ax1 = (int)(to.x - arrowLen * Math.cos(angle - arrowAngle));
                int ay1 = (int)(to.y - arrowLen * Math.sin(angle - arrowAngle));
                int ax2 = (int)(to.x - arrowLen * Math.cos(angle + arrowAngle));
                int ay2 = (int)(to.y - arrowLen * Math.sin(angle + arrowAngle));
                g2.drawLine(to.x, to.y, ax1, ay1);
                g2.drawLine(to.x, to.y, ax2, ay2);
            }
        };

        overlay.setOpaque(false);
        overlay.setBounds(0, 0, 810, 1000);
        DrawBoard.strategyPane.add(overlay);
        DrawBoard.strategyPane.setComponentZOrder(overlay, 0);
        DrawBoard.repaintAll();
    }

    public static void hideStrategy() {
        DrawBoard.strategyPane.removeAll();
        DrawBoard.repaintAll();
    }

    private static Queue<Tile> computePath(Tile start) {
        return A_Star(start, Board.largestWeight());
    }
}
