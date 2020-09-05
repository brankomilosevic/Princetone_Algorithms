/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // private QuickUnionUF site;
    private WeightedQuickUnionUF site;
    private boolean[] openMap;
    private final int siteSize;
    private int numOpen;
    private final int sizeOfArray;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) throw new IllegalArgumentException("Grid dimension not valid!");
        sizeOfArray = (n + 1) * (n + 1) + 2;
        // site = new QuickUnionUF(sizeOfArray);
        site = new WeightedQuickUnionUF(sizeOfArray);
        openMap = new boolean[sizeOfArray];
        for (int i = 0; i < sizeOfArray; i++) openMap[i] = false;
        numOpen = 0;
        openMap[0] = true;
        siteSize = n;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > siteSize)
            throw new IllegalArgumentException("Row out of range: " + row);
        if (col < 1 || col > siteSize)
            throw new IllegalArgumentException("Col out of range: " + col);

        int p = (row) * siteSize + (col);
        if (p > 0 && p < sizeOfArray) {
            int qUp = (row - 1) * siteSize + (col);
            int qDown = (row + 1) * siteSize + (col);
            int qLeft = (row) * siteSize + (col - 1);
            int qRight = (row) * siteSize + (col + 1);

            if (qUp >= 0 && qUp < sizeOfArray)
                if ((row > 1) && (isOpen(row - 1, col))) {
                    site.union(p, qUp);
                    // System.out.println("Union: row: " + row + "   col: " + col);
                    // printSite();
                }
            if (qDown >= 0 && qDown < sizeOfArray)
                if ((row < siteSize) && (isOpen(row + 1, col))) {
                    site.union(qDown, p);
                    // System.out.println("Union: row: " + row + "   col: " + col);
                    // printSite();
                }
            if (qLeft >= 0 && qLeft < sizeOfArray)
                if ((col > 1)
                        && (isOpen(row, col - 1))) {
                    site.union(p, qLeft);
                    // System.out.println("Union: row: " + row + "   col: " + col);
                    // printSite();
                }

            if (qRight >= 0 && qRight < sizeOfArray)
                if ((col < siteSize) && (isOpen(row, col + 1))) {
                    site.union(p, qRight);
                    // System.out.println("Union: row: " + row + "   col: " + col);
                    // printSite();
                }

            if (!openMap[p]) {
                openMap[p] = true;
                numOpen++;
            }
        }

        if (row == 1) site.union(p, 0);
        // if (row == siteSize) site.union(sizeOfArray, p);
    }


    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > siteSize)
            throw new IllegalArgumentException("Row out of range: " + row);
        if (col < 1 || col > siteSize)
            throw new IllegalArgumentException("Col out of range: " + row);

        int p = (row) * siteSize + (col);
        if (p > 0 && p < sizeOfArray) {
            return openMap[p];
        }
        else {
            // System.out.println("\n!!! ERROR p = " + p + " SiteCount = " + sizeOfArray);
            return false;
        }
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > siteSize)
            throw new IllegalArgumentException("Row out of range: " + row);
        if (col < 1 || col > siteSize)
            throw new IllegalArgumentException("Col out of range: " + col);

        int p;
        p = row * siteSize + col;
        if (!isOpen(row, col)) return false;

        if (site.find(p) == site.find(0))
            // System.out.println("Found FULL: row: " + row + "   col: " + col);
            return true;

        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {

        for (int i = 1; i <= siteSize; i++)
            if (isFull(siteSize, i)) return true;

        return false;

        // return site.find(sizeOfArray) == site.find(0);
    }

    // visual of site
    /*
    private void printSite() {
        for (int i = 1; i <= (siteSize * 3 + 6); i++) System.out.print('-');
        System.out.print("\n");
        for (int i = 1; i <= siteSize; i++) {
            System.out.print("|  ");
            for (int j = 1; j <= siteSize; j++)
                System.out.print(isOpen(i, j) ? "   " : "|*|");
            System.out.print("  |\n");
        }
        for (int i = 1; i <= (siteSize * 3 + 6); i++) System.out.print('-');
        System.out.println(" ");

        System.out.flush();
    }
    */

    // test client (optional)
    public static void main(String[] args) {
        System.out.println("Hello percolation!");
        int repetitionsT = Integer.parseInt(args[1]);
        System.out.println("Number of repetitions: " + repetitionsT);
        int size = Integer.parseInt(args[0]);
        double avgP = 0;

        for (int k = 0; k < repetitionsT; k++) {
            Percolation p = new Percolation(size);
            while (!p.percolates()) {
                int row;
                int col;
                row = StdRandom.uniform(p.siteSize) + 1;
                col = StdRandom.uniform(p.siteSize) + 1;

                p.open(row, col);
            }
            avgP += 1.0 * p.numOpen / (p.siteSize * p.siteSize);
            System.out.print('.');
            System.out.flush();
        }

        avgP /= repetitionsT;
        System.out.println("\nProbability after " + repetitionsT + " iterations is: " + avgP);

        /*
        System.out.println("\n !!! IT PERCOLATES !!!\n");
        System.out.println("Iteration nr: " + i);
        System.out.println("Final site: ROW: " + row + "   COL: " + col);
        System.out.println("Number of open sites: " + p.numOpen);
        System.out.println(100 * p.numOpen / (p.siteSize * p.siteSize) + " [%]");
        p.printSite();

        System.out.println("Number of open sites: " + p.numOpen);
        p.printSite();
*/
    }
}
