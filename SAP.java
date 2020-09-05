/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private Digraph mGraph;
    private BreadthFirstDirectedPaths g1;
    private BreadthFirstDirectedPaths g2;

    public SAP(Digraph G) {
        mGraph = new Digraph(G);
    }

    public int length(int v, int w) {
        g1 = new BreadthFirstDirectedPaths(mGraph, v);
        g2 = new BreadthFirstDirectedPaths(mGraph, w);
        int vertex = mGraph.V();
        int minLength = Integer.MAX_VALUE;
        for (int i = 0; i < vertex; i++) {
            if (g1.hasPathTo(i) && g2.hasPathTo(i)) {
                minLength = Math.min(minLength,
                                     g1.distTo(i) + g2.distTo(i));
            }
        }
        if (minLength == Integer.MAX_VALUE) return -1;
        return minLength;
    }

    public int ancestor(int v, int w) {
        g1 = new BreadthFirstDirectedPaths(mGraph, v);
        g2 = new BreadthFirstDirectedPaths(mGraph, w);
        int vertex = mGraph.V();
        int minLength = Integer.MAX_VALUE;
        int result = -1;
        for (int i = 0; i < vertex; i++) {
            if (g1.hasPathTo(i) && g2.hasPathTo(i)) {
                int cur = g1.distTo(i) + g2.distTo(i);
                if (cur < minLength) {
                    minLength = cur;
                    result = i;
                }
            }
        }
        if (minLength == Integer.MAX_VALUE) return -1;
        return result;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        g1 = new BreadthFirstDirectedPaths(mGraph, v);
        g2 = new BreadthFirstDirectedPaths(mGraph, w);
        int vertex = mGraph.V();
        int minLength = Integer.MAX_VALUE;
        for (int i = 0; i < vertex; i++) {
            if (g1.hasPathTo(i) && g2.hasPathTo(i)) {
                minLength = Math.min(minLength,
                                     g1.distTo(i) + g2.distTo(i));
            }
        }
        if (minLength == Integer.MAX_VALUE) return -1;
        return minLength;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        g1 = new BreadthFirstDirectedPaths(mGraph, v);
        g2 = new BreadthFirstDirectedPaths(mGraph, w);
        int vertex = mGraph.V();
        int minLength = Integer.MAX_VALUE;
        int result = -1;
        for (int i = 0; i < vertex; i++) {
            if (g1.hasPathTo(i) && g2.hasPathTo(i)) {
                int cur = g1.distTo(i) + g2.distTo(i);
                if (cur < minLength) {
                    minLength = cur;
                    result = i;
                }
            }
        }
        if (minLength == Integer.MAX_VALUE) return -1;
        return result;
    }

    public static void main(String[] args) {

    }

}
