package finished;/* *****************************************************************************
 *  Name:              Branko Milosevic
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {

    private static final int BOUND_ENERGY = 1000;
    private Picture mPic;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.mPic = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return mPic;
    }

    // width of current picture
    public int width() {
        return mPic.width();
    }

    // height of current picture
    public int height() {
        return mPic.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= mPic.width() || y < 0 || y >= mPic.height()) {
            throw new IndexOutOfBoundsException("[x:y] Out of boundaries!!!");
        }

        if (x == 0 || x == (mPic.width() - 1) || y == 0 || y == (mPic.height() - 1)) {
            return SeamCarver.BOUND_ENERGY;
        }

        Color c1;
        Color c2;

        c1 = mPic.get(x - 1, y);
        c2 = mPic.get(x + 1, y);

        int RX = c2.getRed() - c1.getRed();
        int GX = c2.getGreen() - c1.getGreen();
        int BX = c2.getBlue() - c1.getBlue();
        int DX = RX * RX + GX * GX + BX * BX;

        c1 = mPic.get(x, y - 1);
        c2 = mPic.get(x, y + 1);
        int RY = c2.getRed() - c1.getRed();
        int GY = c2.getGreen() - c1.getGreen();
        int BY = c2.getBlue() - c1.getBlue();
        int DY = RY * RY + GY * GY + BY * BY;

        return Math.sqrt(DX + DY);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int h = mPic.height();
        int w = mPic.width();
        int total = h * w;
        double[] disTo = new double[total + 2];
        int[] edgeTo = new int[total + 2];
        Arrays.fill(disTo, Double.POSITIVE_INFINITY);

        // source vertex
        disTo[0] = 0;
        edgeTo[0] = -1;
        for (int i = 0; i <= total; i++) {
            if (i == 0) {
                for (int j = 0; j < w; j++) {
                    double cost = this.energy(j, 0);
                    int index = j + 1;
                    if (disTo[0] + cost < disTo[index]) {
                        disTo[index] = disTo[0] + cost;
                        edgeTo[index] = 0;
                    }
                } // end for loop
                continue;
            }
            int row = (i - 1) / w;
            int col = (i - 1) % w;
            if (row == h - 1) {
                double cost = 0;
                if (disTo[i] + cost < disTo[total + 1]) {
                    disTo[total + 1] = disTo[i];
                    edgeTo[total + 1] = i;
                }
                continue;
            }
            for (int j = -1; j <= 1; j++) {
                int nrow = row + 1;
                int ncol = col + j;
                if (ncol < 0 || ncol >= w) continue;
                int index = nrow * w + ncol + 1;
                double cost = this.energy(ncol, nrow);
                if (disTo[i] + cost < disTo[index]) {
                    disTo[index] = disTo[i] + cost;
                    edgeTo[index] = i;
                }
            }
        }

        int[] result = new int[h];
        int current = total + 1;
        int ptr = h - 1;
        while (edgeTo[current] > 0) {
            result[ptr] = (edgeTo[current] - 1) % w;
            ptr--;
            current = edgeTo[current];
            // if (ptr < -1) System.err.println("Error occurs");
        }
        return result;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int h = mPic.height();
        int w = mPic.width();
        int total = h * w;

        double[] disTo = new double[total + 2];
        int[] edgeTo = new int[total + 2];
        Arrays.fill(disTo, Double.POSITIVE_INFINITY);

        // source vertex
        disTo[0] = 0;
        edgeTo[0] = -1;

        for (int i = 0; i <= total; i++) {
            if (i == 0) {
                for (int j = 0; j < h; j++) {
                    double cost = this.energy(0, j);
                    int index = j + 1;
                    if (disTo[0] + cost < disTo[index]) {
                        disTo[index] = disTo[0] + cost;
                        edgeTo[index] = 0;
                    }
                }
                continue;
            }
            int col = (i - 1) / h;
            int row = (i - 1) % h;
            if (col == w - 1) {
                double cost = 0;
                if (disTo[i] + cost < disTo[total + 1]) {
                    disTo[total + 1] = disTo[i];
                    edgeTo[total + 1] = i;
                }
                continue;
            }
            for (int j = -1; j <= 1; j++) {
                int nrow = row + j;
                int ncol = col + 1;
                if (nrow < 0 || nrow >= h) continue;
                int index = nrow + ncol * h + 1;
                double cost = this.energy(ncol, nrow);
                if (disTo[i] + cost < disTo[index]) {
                    disTo[index] = disTo[i] + cost;
                    edgeTo[index] = i;
                }
            }
        }

        int[] result = new int[w];
        int current = total + 1;
        int ptr = w - 1;
        while (edgeTo[current] > 0) {
            result[ptr] = (edgeTo[current] - 1) % h;
            ptr--;
            current = edgeTo[current];
            // if (ptr < -1) System.err.println("Error occurs");
        }
        return result;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        int w = mPic.width();
        int h = mPic.height();
        if (h != seam.length) throw new IllegalArgumentException("Array length not good!!!");
        if (w == 1) throw new IllegalArgumentException("Width should not be 1!!!");

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= w)
                throw new IndexOutOfBoundsException("Array not good!!!");

            if (i > 0 && Math.abs((seam[i] - seam[i - 1])) > 1)
                throw new IllegalArgumentException("Diff should not be more than 1!!!");
        }

        Picture npic = new Picture(w - 1, h);
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w - 1; i++) {
                if (i < seam[j]) {
                    npic.set(i, j, mPic.get(i, j));
                }
                else {
                    npic.set(i, j, mPic.get(i + 1, j));
                }
            }
        }
        this.mPic = npic;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        int w = mPic.width();
        int h = mPic.height();
        if (w != seam.length)
            throw new IllegalArgumentException("Something is wrong with the array lenght!!!");
        if (h == 1) throw new IllegalArgumentException("Height should be more than 1!!!");

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= h)
                throw new IndexOutOfBoundsException("Something is wrong with the array!!!");
            if (i > 0 && Math.abs((seam[i] - seam[i - 1])) > 1)
                throw new IllegalArgumentException("Diff should not be more than 1!!!");
        }

        Picture npic = new Picture(w, h - 1);

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h - 1; j++) {
                if (j < seam[i]) {
                    npic.set(i, j, mPic.get(i, j));
                }
                else {
                    npic.set(i, j, mPic.get(i, j + 1));
                }
            }
        }
        this.mPic = npic;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        // implemented outside
    }
}
