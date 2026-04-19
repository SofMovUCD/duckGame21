import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
        System.out.println(next);
        System.out.println(path);

        next.getTileButton().doClick();
        Game.flipMovingFlag();
        placed.push(next);
    }

    private Tile findNewStart() {
        Tile newStart;

        for (int i = 0; i < Board.BOARD_X; i += 2) {
            newStart = (Tile) Game.valueForID(Board.getTile(i, 0), Board.getTile(0, i/2), this);
            if (!newStart.isBlocked() && !placed.contains(newStart)) {
                return newStart;
            }
        }
        return null;
    }

    private void createPath(Tile start) {
        System.out.println(start + " " + Board.largestWeight());
        path = fullPathMaker(start, Board.largestWeight());
    }

    private Queue<Tile> fullPathMaker(Tile start, Tile end) {
        Queue<Tile> output = new LinkedList<Tile>();
        List<Tile> visited = new ArrayList<>();
        Queue<Tile> working = new LinkedList<>();
        output = findShortestPath(start, end, working, visited);
        System.out.println(output);
        return output;
    }

    private Queue<Tile> findShortestPath(Tile curr, Tile end, Queue<Tile> output, List<Tile> visited) {
        visited.add(curr);

        for (Tile neighbour : Board.getNeighbours(curr)) {
            if (!neighbour.isBlocked() && neighbour.getValue() == 0 && !visited.contains(neighbour)) {
                if (neighbour.equals(end)) {
                    output.add(neighbour);
                    return output;
                }
                else findShortestPath(neighbour, end, output, visited);
            }
        }
        return null;
    }
    
}
