/* *****************************************************************************
 *  Name:              Branko Milosevic
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Iterator;

public class Solver {

    private final int moves;
    private final boolean isSolvable;
    private Stack<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("No Board to process !!!");

        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null));

        Board twin = initial.twin();
        MinPQ<SearchNode> tpq = new MinPQ<>();
        tpq.insert(new SearchNode(twin, 0, null));

        SolutionObj sol = solve(pq, tpq);
        this.moves = sol.moves;
        this.isSolvable = sol.isSolvable;
        this.solution = buildSolution(sol.solution);
    }

    private Stack<Board> buildSolution(SearchNode finalSol) {
        Stack<Board> sol = new Stack<>();

        while (finalSol.previous != null) {
            sol.push(finalSol.board);
            finalSol = finalSol.previous;
        }
        sol.push(finalSol.board);

        return sol;
    }

    private class SolutionObj {

        public final SearchNode solution;
        public final int moves;
        public final boolean isSolvable;

        private SolutionObj(SearchNode solution, int moves, boolean isSolvable) {
            this.moves = moves;
            this.isSolvable = isSolvable;
            this.solution = solution;
        }
    }

    private SolutionObj solve(MinPQ<SearchNode> pq, MinPQ<SearchNode> tpq) {
        SearchNode node = pq.min();
        SearchNode tnode = pq.min();

        while (!node.board.isGoal() || !tnode.board.isGoal()) {
            node = pq.delMin();
            tnode = tpq.delMin();

            for (Board neighbor : node.board.neighbors()) {
                if (neighbor.isGoal()) {
                    node = new SearchNode(neighbor, node.moves + 1, node);
                    return new SolutionObj(node, node.moves, true);
                }
                else if (node.previous == null || !node.previous.board.equals(neighbor))
                    pq.insert(new SearchNode(neighbor, node.moves + 1, node));
            }
            for (Board tneighbor : tnode.board.neighbors()) {
                if (tneighbor.isGoal()) {
                    return new SolutionObj(null, -1, false);
                }
                else if (tnode.previous == null || !tnode.previous.board.equals(tneighbor))
                    tpq.insert(new SearchNode(tneighbor, tnode.moves + 1, tnode));
            }
        }

        if (node.board.isGoal()) {
            return new SolutionObj(node, node.moves, true);
        }
        else {
            return new SolutionObj(null, -1, false);
        }
    }

    private class SearchNode implements Comparable<SearchNode> {

        private Board board;
        private int moves;
        private SearchNode previous;
        public int priority;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            priority = moves + board.manhattan();
        }

        public int compareTo(SearchNode that) {
            return (Integer.compare(this.priority, that.priority));
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.isSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return this.moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (moves != -1) return new Solution();
        else return null;
    }

    private class Solution implements Iterable<Board> {

        public Iterator<Board> iterator() {
            return new SolutionBoardsIterator();
        }

        private class SolutionBoardsIterator implements Iterator<Board> {

            public boolean hasNext() {
                return !Solver.this.solution.isEmpty();
            }

            public Board next() {
                return Solver.this.solution.pop();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }

    // test client (see below)
    public static void main(String[] args) {
    }
}
