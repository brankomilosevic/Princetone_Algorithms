/* *****************************************************************************
 *  Name:              Branko Milosevic
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class Board {

    private int[][] mGrid;
    private final int mDim;
    private int[][] mGoalGrid;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles.length < 2 || tiles.length >= 128 || tiles[0].length < 2
                || tiles[0].length >= 128)
            throw new IllegalArgumentException("No grid to work with !!!");

        if (tiles.length != tiles[0].length) throw new IllegalArgumentException(
                "Number of Rows need to be same as Number of Cols !!!");
        else mDim = tiles.length;

        mGrid = new int[mDim][mDim];
        mGoalGrid = new int[mDim][mDim];

        for (int i = 0; i < mDim; i++) {
            for (int j = 0; j < mDim; j++) {
                mGrid[i][j] = tiles[i][j];
            }
        }

        for (int i = 0; i < mDim; i++) {
            for (int j = 0; j < mDim; j++) {
                mGoalGrid[i][j] = i * mDim + j + 1;
            }
        }
        mGoalGrid[mDim - 1][mDim - 1] = 0;

    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();

        // mGrid = mGoalGrid;

        s.append(mDim);
        s.append('\n');

        for (int i = 0; i < mDim; i++) {
            for (int j = 0; j < mDim; j++) {

                String str;
                if (mDim < 4) str = String.format("%2d", mGrid[i][j]);
                else if (mDim < 10) str = String.format("%3d", mGrid[i][j]);
                else if (mDim < 100) str = String.format("%4d", mGrid[i][j]);
                else str = String.format("%5d", mGrid[i][j]);
                s.append(str);
            }
            s.append('\n');
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return mDim;
    }

    // number of tiles out of place
    public int hamming() {
        int hamm = 0;

        for (int i = 0; i < mDim; i++)
            for (int j = 0; j < mDim; j++)
                if (mGoalGrid[i][j] != mGrid[i][j]) hamm++;

        return hamm;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manh = 0;

        for (int i = 0; i < mDim; i++) {
            for (int j = 0; j < mDim; j++) {
                if (mGrid[i][j] != 0) {
                    int hor = (mGrid[i][j] - 1) % mDim - j;
                    if (hor < 0) hor = -hor;
                    int ver = (mGrid[i][j] - 1) / mDim - i;
                    if (ver < 0) ver = -ver;
                    manh += hor + ver;
                }
            }
        }

        return manh;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < mDim; i++)
            for (int j = 0; j < mDim; j++)
                if (mGrid[i][j] != mGoalGrid[i][j]) return false;
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null || this.getClass() != y.getClass()) return false;

        Board tmp = (Board) y;
        if (this.dimension() != tmp.dimension()) return false;


        for (int i = 0; i < mDim; i++)
            for (int j = 0; j < mDim; j++)
                if (mGrid[i][j] != tmp.mGrid[i][j]) return false;

        return true;

    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new Neighbors(this.mGrid);
    }

    private class Neighbors implements Iterable<Board> {
        private int[][] tmp;
        private Stack<Board> neighbors = new Stack<Board>();

        public Neighbors(int[][] t) {
            this.tmp = t;
            getNeighbors();
        }

        private void getNeighbors() {
            int iI = 0;
            int jJ = 0;

            for (int i = 0; i < mDim; i++)
                for (int j = 0; j < mDim; j++)
                    if (tmp[i][j] == 0) {
                        iI = i;
                        jJ = j;
                    }


            if (iI > 0) neighbors.push(new Board(replace(tmp, iI, jJ, iI - 1, jJ)));
            if (iI < mDim - 1) neighbors.push(new Board(replace(tmp, iI, jJ, iI + 1, jJ)));
            if (jJ > 0) neighbors.push(new Board(replace(tmp, iI, jJ, iI, jJ - 1)));
            if (jJ < mDim - 1) neighbors.push(new Board(replace(tmp, iI, jJ, iI, jJ + 1)));
        }

        public Iterator<Board> iterator() {
            return new NeighborsIterator();
        }

        private class NeighborsIterator implements Iterator<Board> {
            public boolean hasNext() {
                return !neighbors.isEmpty();
            }

            public Board next() {
                return neighbors.pop();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        }

    }

    private static int[][] replace(int[][] inGrid, int finalI, int finalJ, int startI, int startJ) {
        int dim = inGrid.length;
        int[][] replGrid = new int[dim][dim];

        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                replGrid[i][j] = inGrid[i][j];

        int tmp = replGrid[finalI][finalJ];
        replGrid[finalI][finalJ] = replGrid[startI][startJ];
        replGrid[startI][startJ] = tmp;

        return replGrid;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int finalI = 0;
        int finalJ = 0;

        loop:
        for (int i = 0; i < mDim; i++) {
            for (int j = 0; j < mDim; j++) {
                if (mGrid[i][j] != 0) {
                    finalJ = i;
                    finalJ = j;
                    break loop;
                }
            }
        }

        int startI = mDim - 1, startJ = mDim - 1;
        loop:
        for (int i = mDim - 1; i >= 0; i--) {
            for (int j = mDim - 1; j >= 0; j--) {
                if (mGrid[i][j] != 0 && i != finalJ && j != finalJ) {
                    startI = i;
                    startJ = j;
                    break loop;
                }
            }
        }

        return new Board(replace(mGrid, finalI, finalJ, startI, startJ));
    }

    // unit testing (not graded)
    public static void main(String[] args) {

        int dim;
        if (args.length > 0) dim = Integer.parseInt(args[0]);
        else dim = 5;

        int[][] tiles = new int[dim][dim];

        // test grid
        for (int i = 0; i < dim; i++) for (int j = 0; j < dim; j++) tiles[i][j] = i * dim + j;
        for (int i = 0; i < dim * dim; i++) {
            int r = StdRandom.uniform(dim * dim);
            int tmp = tiles[i / dim][i % dim];
            tiles[i / dim][i % dim] = tiles[r / dim][r % dim];
            tiles[r / dim][r % dim] = tmp;
        }

        Board b = new Board(tiles);
        System.out.print(b.toString());


    }

}
