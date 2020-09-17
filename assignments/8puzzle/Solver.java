/* *****************************************************************************
 *  Name:              Brian Hambleton
 *  Last modified:     15 Sept 2020
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode goal;
    private int moves = -1;

    // constructor
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        // use two A* searches
        MinPQ<SearchNode> mPQ = new MinPQ<>();
        MinPQ<SearchNode> pPQ = new MinPQ<>();

        mPQ.insert(new SearchNode(initial, 0, null));
        pPQ.insert(new SearchNode(initial.twin(), 0, null));

        SearchNode mSearchNode, pSearchNode;

        while (!mPQ.min().board.isGoal() && !pPQ.min().board.isGoal()) {
            mSearchNode = mPQ.delMin();
            pSearchNode = pPQ.delMin();

            // Main priority queue for search
            for (Board b : mSearchNode.board.neighbors()) {
                // if first node, or
                // if prev search node == current node, skip adding to queue
                if (mSearchNode.prev == null || !b.equals(mSearchNode.prev.board))
                    mPQ.insert(new SearchNode(b, mSearchNode.moves + 1, mSearchNode));
            }

            // Secondary priority queue for search
            for (Board b : pSearchNode.board.neighbors()) {
                // if first node, or
                // if prev search node == current node, skip adding to queue
                if (pSearchNode.prev == null || !b.equals(pSearchNode.prev.board))
                    pPQ.insert(new SearchNode(b, pSearchNode.moves + 1, pSearchNode));
            }
        }

        // if main queue was solved, min() gives
        if (mPQ.min().board.isGoal()) {
            this.goal = mPQ.min();
            this.moves = mPQ.min().moves;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return this.moves >= 0;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return this.moves;
    }

    // sequence of boards in the shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        Stack<Board> solution = new Stack<Board>();

        // construct solution stack by iterating through goal Search Node's prev nodes
        SearchNode curr = this.goal;
        while (curr != null) {
            solution.push(curr.board);
            curr = curr.prev;
        }

        return solution;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final int priority;
        private final SearchNode prev;

        private SearchNode(Board b, int moves, SearchNode prev) {
            this.board = b;
            this.moves = moves;
            this.prev = prev;
            this.priority = moves + b.manhattan();
        }

        @Override
        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);
        }
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
