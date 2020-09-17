/* *****************************************************************************
 *  name:              Brian Hambleton
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */


import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n;
    private boolean[] grid;
    private final WeightedQuickUnionUF quf;
    private int openSites;
    // private Stopwatch sw;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(
                    "n: (" + n + ") it not greater than 0.");
        }
        this.n = n;
        this.openSites = 0;

        // use array to maintain list of open/closed sites
        this.grid = new boolean[n * n];

        // use weighted quick union type to maintain connections
        //      + 2 for virtual top (quf[n*n+1] and virtual bottom (quf[n*n])
        this.quf = new WeightedQuickUnionUF((n * n) + 2);

        for (int i = 0; i < n * n; i++) {
            this.grid[i] = false;
        }
    }

    public void open(int row, int col) {
        if (!isValidSite(row, col)) {
            throw new IllegalArgumentException("Illegal indices [" + row + "][" + col + "]");
        }

        if (isOpen(row, col)) {
            // if already open, don't open
            return;
        }
        else {
            int site = this.getSite(row, col);

            // open space
            this.grid[site] = true;
            this.openSites++;

            // check if connected to virtual top or virtual bottom
            if (row == 1) {
                // connect to virtual top
                this.quf.union((this.n * this.n) + 1, site);
            }

            if (row == this.n) {
                // connect to virtual bottom
                this.quf.union((this.n * this.n), site);
            }

            // check for open neighbors to connect with

            // if site below is open, connect
            if (isValidSite(row + 1, col) && isOpen(row + 1, col)) {
                this.quf.union(this.getSite(row + 1, col), site);
            }

            // if site above is open, connect
            if (isValidSite(row - 1, col) && isOpen(row - 1, col)) {
                this.quf.union(this.getSite(row - 1, col), site);
            }

            // if site to the right is open, connect
            if (isValidSite(row, col + 1) && isOpen(row, col + 1)) {
                this.quf.union(this.getSite(row, col + 1), site);
            }

            // if site to the left is open, connect
            if (isValidSite(row, col - 1) && isOpen(row, col - 1)) {
                this.quf.union(this.getSite(row, col - 1), site);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        if (!isValidSite(row, col)) {
            throw new IllegalArgumentException("Illegal indices [" + row + "][" + col + "]");
        }
        // check if site is open
        return this.grid[this.getSite(row, col)];
    }

    public boolean isFull(int row, int col) {
        // check if connected to virtual top
        if (!isValidSite(row, col)) {
            throw new IllegalArgumentException("Illegal indices [" + row + "][" + col + "]");
        }
        return this.quf.find((this.n * this.n) + 1) == this.quf.find(this.getSite(row, col));
    }

    public int numberOfOpenSites() {
        return this.openSites;
    }

    public boolean percolates() {
        return this.quf.find(this.n * this.n) == this.quf.find((this.n * this.n) + 1);
    }

    private int getSite(int row, int col) {
        return (this.n * (row - 1)) + (col - 1);
    }

    private boolean isValidSite(int row, int col) {
        int gridSize = this.n;
        return row - 1 >= 0 && col - 1 >= 0 && row - 1 < gridSize && col - 1 < gridSize;
    }

    public static void main(String[] args) {
        // unused main
    }
}
