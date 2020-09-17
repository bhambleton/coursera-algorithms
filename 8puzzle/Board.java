/* *****************************************************************************
 *  Name:              Brian Hambleton
 *  Last Modified: 15 Sept 2020
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {

    private final int width;
    private final int[][] tiles;
    private int manhattanNum = Integer.MIN_VALUE;
    private int hammingNum = Integer.MIN_VALUE;
    private int blankRow;
    private int blankCol;

    //  create a board from an n-by-n array of tiles,
    //      where tiles[row][col] = tile at (row, col)
    public Board(int[][] t) {

        // check if tiles is null or not a square 2D array
        if (t == null || t[0].length != t.length)
            throw new IllegalArgumentException();

        this.width = t.length;
        this.tiles = new int[this.width][this.width];

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.width; j++) {
                this.tiles[i][j] = t[i][j];
                if (t[i][j] == 0) {
                    this.blankRow = i;
                    this.blankCol = j;
                }
            }
        }
    }

    //  string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append(this.width);
        s.append(System.lineSeparator());

        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles.length; j++) {
                s.append(String.format("%2d ", this.tiles[i][j]));
            }
            s.append(System.lineSeparator());
        }

        return s.toString();
    }

    // return board width
    public int dimension() {
        return this.width;
    }

    // sum of the Manhattan distances between tiles and goal
    public int manhattan() {
        if (this.manhattanNum != Integer.MIN_VALUE)
            return this.manhattanNum; // return precomputed manhattan distance

        int manhattan = 0;

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.width; j++) {

                int goal = i * this.width + j + 1;
                if (this.tiles[i][j] == 0 || this.tiles[i][j] == goal)
                    continue; // ignore blank square or if in correct location

                int rPos = (this.tiles[i][j] - 1) / this.width;
                int cPos = this.tiles[i][j] - rPos * this.width - 1;

                // add distance to goal for this tile
                manhattan += Math.abs(i - rPos) + Math.abs(j - cPos);
            }
        }

        this.manhattanNum = manhattan;
        return this.manhattanNum;
    }

    // number of tiles out of place
    public int hamming() {
        if (this.hammingNum != Integer.MIN_VALUE)
            return this.hammingNum; // return precomputed hamming val

        int hamming = 0;

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.width; j++) {
                int goal = i * this.dimension() + j + 1;
                if (this.tiles[i][j] > 0 && this.tiles[i][j] != goal)
                    hamming++;
            }
        }

        this.hammingNum = hamming;
        return this.hammingNum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        // if no tile is out of place (hamming == 0), return true
        return (this.hamming() == 0);
    }

    // does this board equal o
    public boolean equals(Object other) {
        if (other == null) return false;
        if (this == other) return true;
        if (this.getClass() != other.getClass()) return false;

        Board that = (Board) other;
        if (this.dimension() != that.dimension()) return false;
        return Arrays.deepEquals(this.tiles, ((Board) other).tiles);
    }

    // all neighboring boards (aka potential moves)
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<>();

        // Add neighboring boards to neighbors Stack
        // neighbor above
        if (this.blankRow > 0) {
            Board board = new Board(this.tiles);
            board.swap(this.blankRow, this.blankCol, this.blankRow - 1, this.blankCol);
            neighbors.push(board);
        }
        // neighbor below
        if (this.blankRow < this.width - 1) {
            Board board = new Board(this.tiles);
            board.swap(this.blankRow, this.blankCol, this.blankRow + 1, this.blankCol);
            neighbors.push(board);
        }
        // neighbor left
        if (this.blankCol > 0) {
            Board board = new Board(this.tiles);
            board.swap(this.blankRow, this.blankCol, this.blankRow, this.blankCol - 1);
            neighbors.push(board);
        }
        // neighbor right
        if (this.blankCol < this.width - 1) {
            Board board = new Board(this.tiles);
            board.swap(this.blankRow, this.blankCol, this.blankRow, this.blankCol + 1);
            neighbors.push(board);
        }

        return neighbors;
    }

    private void swap(int row1, int col1, int row2, int col2) {
        assert isValidCoordinate(row1, col1);
        assert isValidCoordinate(row2, col2);

        // update row/col index of empty tile (blankRow, blankCol)
        if (this.tiles[row1][col1] == 0) {
            this.blankRow = row2;
            this.blankCol = col2;
        }
        else if (this.tiles[row2][col2] == 0) {
            this.blankRow = row1;
            this.blankCol = col1;
        }

        int temp = this.tiles[row1][col1];
        this.tiles[row1][col1] = this.tiles[row2][col2];
        this.tiles[row2][col2] = temp;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board board = new Board(this.tiles);

        for (int i = 0; i < board.width; i++) {
            for (int j = 0; j < board.width; j++) {
                if (board.tiles[i][j] != 0) {
                    if (j + 1 < board.width && board.tiles[i][j + 1] != 0)
                        board.swap(i, j, i, j + 1); // swap with tile to right
                    else if (i + 1 < board.width && board.tiles[i + 1][j] != 0)
                        board.swap(i, j, i + 1, j); // swap with tile below
                    else if (i + 1 < board.width && j + 1 < board.width &&
                            board.tiles[i + 1][j + 1] != 0)
                        board.swap(i, j, i + 1, j + 1);

                    return board;
                }
            }

        }

        // if we reach here, error in board makeup
        throw new IllegalArgumentException();
    }

    // private void printSummary() {
    //     StdOut.println("Manhattan: " + this.manhattan());
    //     StdOut.println("Hamming: " + this.hamming());
    //
    //     StdOut.println("Initial board:");
    //     StdOut.println(this.toString());
    //     StdOut.println();
    //
    //     StdOut.println("Potential moves:");
    //     for (Board b : this.neighbors()) {
    //         StdOut.println(b.toString());
    //     }
    //     StdOut.println();
    //
    //     StdOut.println("Board Twin:");
    //     StdOut.println(this.twin().toString());
    //     StdOut.println();
    // }

    private boolean isValidCoordinate(int row, int col) {
        return (row >= 0 && col >= 0 && row < this.width && col < this.width);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();

        int[][] tiles = new int[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

        Board initial = new Board(tiles);
        StdOut.println(initial.toString());
    }
}
