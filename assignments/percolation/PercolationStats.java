import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_CONSTANT = 1.96;
    private final int trialCount;
    private final double[] trialResults;


    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException(
                    "n: (" + n + ") and trials: (" + trials + ") are not greater than 0.");
        }

        this.trialCount = trials;
        this.trialResults = new double[this.trialCount];

        for (int t = 0; t < trials; t++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                // while grid does not percolate, open random sites
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                perc.open(row, col);
            }
            double result = (double) perc.numberOfOpenSites() / (n * n);
            this.trialResults[t] = result;
        }
    }

    public double mean() {
        return StdStats.mean(this.trialResults);
    }

    public double stddev() {
        return StdStats.stddev(this.trialResults);
    }

    public double confidenceLo() {
        return mean() - ((CONFIDENCE_CONSTANT * this.stddev()) / Math.sqrt(this.trialCount));
    }

    public double confidenceHi() {
        return mean() + ((CONFIDENCE_CONSTANT * this.stddev()) / Math.sqrt(this.trialCount));
    }


    public static void main(String[] args) {
        int n = 0;
        int t = 0;

        if (args.length >= 2) {
            n = Integer.parseInt(args[0]);
            t = Integer.parseInt(args[1]);
        }

        PercolationStats percStats = new PercolationStats(n, t);

        String confInterv = "[" + percStats.confidenceLo() + ", " + percStats.confidenceHi() + "]";
        StdOut.println("mean\t\t\t\t= " + percStats.mean());
        StdOut.println("stddev\t\t\t\t= " + percStats.stddev());
        StdOut.println("95% confidence interval = " + confInterv);
    }
}
