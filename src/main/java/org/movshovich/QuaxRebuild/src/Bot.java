package org.movshovich.QuaxRebuild.src;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

/**
 * An BOT player that calculates moves using A* search.
 */
public class Bot extends Player {
    private static Queue<Tile> path  = new LinkedList<>(); // Current planned path
    private static Stack<Tile> placed = new Stack<>();    //Bot's history
    private static Stack<Tile> allPlaced = new Stack<>(); // Global history for path validation
    public static boolean endReached = false;

    /** Creates a Bot with the given player ID (1=BLACK, -1=WHITE). */
    public Bot(int playerId) {
        super(playerId);
    }

    public static Queue<Tile> getPath() {return path;}

    public static Stack<Tile> getPlaced() {return placed;}

    /** Resets the bot's planned path and placed tile history when Pi Rule is activated. */
    public static void piRule() {
        path.clear();
        placed.clear();
    }

    /**
     * Executes the Bot's turn.
     * Calculates a path to the goal and selects the next tile in that path.
     */
    @Override
    public void makeMove() {
        Tile next = null;
        if (!endReached) {
            if (placed.isEmpty()) next = createPath(findNewStart()).poll();
            else {
                path = createPath(placed.peek());
                // Pathfinding recovery if the current path is blocked
                if (path == null) {
                    while (next == null) {
                        placed.pop();
                        Queue<Tile> nextQ = createPath(placed.peek());
                        if (nextQ == null) next = createPath(findNewStart()).poll();
                        else {
                            nextQ.poll();
                            next = nextQ.poll();
                        }
                    }
                }
                else {
                    path.poll();
                    next = createPath(path.poll()).poll();
                }
            }
        } else next = findingGap(); // Defensive play if goal reached
        if (next == null) next = givenUp(); // Random fall-back move
        next.getTileButton().doClick(); //place tile
        placed.push(next); //add placed tile to stack
        allPlaced.push(next);
        if (!endReached) endReached = (boolean) Game.valueForID((next.getY() == 10), next.getX() == 20, this);
        System.out.println(endReached);
        Game.flipMovingFlag(); //go to next move
    }

    /** Picks a random unoccupied tile on the bot's starting edge to begin a new path. */
    private Tile findNewStart() {
        Tile newStart = null;
        Random tileFinder = new Random();

        do {
            if (getPlayerId() == 1) {
                    newStart = Board.getTile((tileFinder.nextInt() % 10) * 2, 0);
            }
            else {
                    newStart = Board.getTile(0, tileFinder.nextInt() % 10);
            }
        } while (newStart == null || newStart.getValue() != 0);

        return newStart;
    }

    /** Wrapper: runs A* from start toward the highest weight goal tile on the board. */
    public static Queue<Tile> createPath(Tile start) {
            return A_Star(start, Board.largestWeight(start));
    }

    /**
     * A* Search Algorithm implementation.
     * Finds the most efficient path from start to goal.
     */
    private static Queue<Tile> A_Star(Tile start, Tile goal) {
        // Initialize open and closed lists
        Queue<Tile> openList = new PriorityQueue<>((a, b) -> a.getF() - b.getF());// Nodes to be evaluated
        List<Tile> closedList = new ArrayList<>();            // Nodes already evaluated
        openList.add(start);

        // Initialize node properties
        start.setG(0);                // Cost from start to start is 0
        start.setH(heuristic(start, goal));  // Estimate to goal
        start.setF();                        // Total estimated cost
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
            for (Tile neighbor : Board.furthNeighbours(current)){
                if (closedList.contains(neighbor) || neighbor.getValue() != 0|| neighbor.isBlocked() || allPlaced.contains(neighbor)) {
                    continue;  // Skip already evaluated nodes
                }
                // Calculate tentative g score
                int tentativeG = current.getG() + 1;
                if (!openList.contains(neighbor)) openList.add(neighbor);
                else if (tentativeG >= neighbor.getG()) continue;  // This path is not better

                // This path is the best so far
                neighbor.setParent(current);
                neighbor.setG(tentativeG);
                neighbor.setH(heuristic(neighbor, goal));
                neighbor.setF();
            }
        }
    return null;  // No path exists
    }

    /** Traces parent pointers from goal back to start and returns the path in order. */
    private static Queue<Tile> reconstruct_path(Tile current) {
        Deque <Tile> path = new LinkedList<>();
        while (current != null) {
            path.addFirst(current);
            current = current.getParent();
        }
        return path;
    }

    /** Geometric distance estimation (Euclidean distance) for A* heuristic */
    private static int heuristic(Tile a, Tile b) {

        return (int) Math.floor(Math.sqrt(Math.pow((double) (b.getX() / 2 - a.getX()) /2,2) + Math.pow((b.getY() - a.getY()),2)));
    }

    /** Clears all strategy overlay components and repaints the board. */
    public static void hideStrategy() {
        DrawBoard.strategyPane.removeAll();
        DrawBoard.repaintAll();
    }

    /**
     * Defensive move: finds an empty tile adjacent to a gap in the bot's chain.
     * Used when the bot has reached its goal side but needs to fill gaps.
     */
    private static Tile findingGap() {
        if (placed.size() > 1) {
            Tile child = placed.pop();
            Tile parent = placed.peek();

            if (!Board.getNeighbours(child).contains(parent)) {
                for (Tile n : Board.getNeighbours(parent)) {
                    if (Board.getNeighbours(child).contains(n) && n.getValue() == 0 ) return n;
                }
            }
            return findingGap();
        } else return null;
    }

    /**
     * Fallback move: tries up to 500 random tiles to find an empty one.
     * Used when A* cannot find any valid path. Returns null if board is full.
     */
    private Tile givenUp() {
        Random find = new Random();
        int attempts = 0;
        while (attempts < 500) {
            Tile candidate = Board.getTile(find.nextInt(20), find.nextInt(10));
            if (candidate != null && candidate.getValue() == 0) return candidate;
            attempts++;
        }
        return null;
    }
}
