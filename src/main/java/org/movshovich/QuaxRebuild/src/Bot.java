package org.movshovich.QuaxRebuild.src;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class Bot extends Player {
    private Queue<Tile> path  = new LinkedList<>();
    private Stack<Tile> placed = new Stack<>();

    public Bot(int playerId) {
        super(playerId);
    }


    @Override
    public void makeMove() {

        Tile last;
        Tile next = path.poll();
        Tile fallback;

        if (next == null) {
            
            if (placed.isEmpty()) {
                fallback = findNewStart();
                createPath(fallback);
            }

            else {
                last = placed.peek();

                if (last.isBlocked()) {
                    while (last.isBlocked() && !placed.isEmpty()) {
                        placed.pop();
                        last = placed.pop();
                    }

                    if (placed.isEmpty()) {
                    fallback = findNewStart();
                    createPath(fallback);
                    }

                    createPath(last);
                }
                else {
                    createPath(last);
                }
            }

            next = path.poll();
        }

        if (next.isBlocked()) {

        }
        

        if (next.getValue() != 0) {
            path.clear();
            createPath(Board.getTile(next.getX() + 2, next.getY() - 1));
            next = path.poll();
        }

        

        next.getTileButton().doClick();
        Game.flipMovingFlag();
        placed.push(next);
        
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

    private void createPath(Tile start) {
            path.addAll(A_Star(start, Board.largestWeight()));
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
                if (closedList.contains(neighbor) || neighbor.getValue() != 0) {
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
    
}
