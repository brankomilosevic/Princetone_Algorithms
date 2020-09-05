/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFCONST = 1.96;
    private double[] avgP;
    private final int repetitionsT;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1) throw new IllegalArgumentException("Size of the grid not valid!");
        if (trials < 1) throw new IllegalArgumentException("Number of trials not valid!");

        avgP = new double[trials];
        repetitionsT = trials;

        for (int k = 0; k < trials; k++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row;
                int col;
                row = StdRandom.uniform(n) + 1;
                col = StdRandom.uniform(n) + 1;

                p.open(row, col);
            }
            avgP[k] = 1.0 * p.numberOfOpenSites() / (n * n);
            // System.out.print('.');
            // System.out.flush();
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        double mean = StdStats.mean(avgP);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double stddev = StdStats.stddev(avgP);
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double confLo = mean() - CONFCONST * stddev() / Math.sqrt(repetitionsT);
        return confLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double confHi = mean() + CONFCONST * stddev() / Math.sqrt(repetitionsT);
        return confHi;
    }

    // test client (see below)
    public static void main(String[] args) {

        PercolationStats pStats = new PercolationStats(
                Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        System.out.println("mean                    = " + pStats.mean());
        System.out.println("sdtdev                  = " + pStats.stddev());
        System.out.println(
                "95% confidence interval = [" + pStats.confidenceLo() + ", " + pStats.confidenceHi()
                        + "]");
    }
}
