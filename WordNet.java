/* *****************************************************************************
 *  Name:              Branko Milosevic
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class WordNet {
    private HashMap<Integer, String> mIntToStrTable;
    private HashMap<String, Set<Integer>> mStrToIntTable;
    private Digraph mGraph;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        mIntToStrTable = new HashMap<Integer, String>();
        mStrToIntTable = new HashMap<String, Set<Integer>>();

        In synIO = new In(synsets);
        In hyperIO = new In(hypernyms);

        while (synIO.hasNextLine()) {
            String[] line = synIO.readLine().split(",");

            if (line.length < 2) continue;
            int index = Integer.parseInt(line[0]);

            String[] words = line[1].split("\\s++");

            for (String s : words) {
                mIntToStrTable.put(index, line[1]);
                if (!mStrToIntTable.containsKey(s)) {
                    mStrToIntTable.put(s, new HashSet<Integer>());
                }
                mStrToIntTable.get(s).add(index);
            }
        }

        mGraph = new Digraph(mIntToStrTable.size());
        //        StdOut.println("Graph size is: "+this.graph.V());

        //        get information about hypernyms
        while (hyperIO.hasNextLine()) {
            String[] line = hyperIO.readLine().split(",");
            if (line.length < 2) continue;
            int index = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                int u = Integer.parseInt(line[i]);
                mGraph.addEdge(index, u);
            } // end for loop
        } // end while loop

        if (hasCycle()) {
            throw new
                    IllegalArgumentException("Input graph is not a DAG !!!");
        }
    }

    private boolean hasCycle() {
        ArrayList<Integer> rootArr = new ArrayList<Integer>();
        for (int i = 0; i < mGraph.V(); i++) {
            if (!mGraph.adj(i).iterator().hasNext()) {
                rootArr.add(i);
            }
        }
        //        there should be exactly one root
        if (rootArr.size() != 1) {
            StdOut.println("There is no root or more than one root.");
            StdOut.println("Size: " + rootArr.size());
            return true;
        }
        DirectedCycle diCycle = new DirectedCycle(mGraph);
        return diCycle.hasCycle();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return mStrToIntTable.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return mStrToIntTable.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!(mStrToIntTable.containsKey(nounA)
                && mStrToIntTable.containsKey(nounB))) {
            throw new
                    IllegalArgumentException("Argument should be in WordNet.");
        }
        Iterable<Integer> itA = mStrToIntTable.get(nounA);
        Iterable<Integer> itB = mStrToIntTable.get(nounB);
        BreadthFirstDirectedPaths g1 = new BreadthFirstDirectedPaths(mGraph, itA);
        BreadthFirstDirectedPaths g2 = new BreadthFirstDirectedPaths(mGraph, itB);

        int result = Integer.MAX_VALUE;
        for (int i : mIntToStrTable.keySet()) {
            if (g1.hasPathTo(i) && g2.hasPathTo(i)) {
                int cur = g1.distTo(i) + g2.distTo(i);
                result = Math.min(result, cur);
            }
        }
        return result;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!(mStrToIntTable.containsKey(nounA)
                && mStrToIntTable.containsKey(nounB))) {
            throw new
                    IllegalArgumentException("Argument should be in WordNet.");
        }

        Iterable<Integer> itA = mStrToIntTable.get(nounA);
        Iterable<Integer> itB = mStrToIntTable.get(nounB);
        BreadthFirstDirectedPaths g1 = new BreadthFirstDirectedPaths(mGraph, itA);
        BreadthFirstDirectedPaths g2 = new BreadthFirstDirectedPaths(mGraph, itB);

        int result = Integer.MAX_VALUE;
        int index = -1;
        for (int i : mIntToStrTable.keySet()) {
            if (g1.hasPathTo(i) && g2.hasPathTo(i)) {
                int cur = g1.distTo(i) + g2.distTo(i);
                if (cur < result) {
                    result = cur;
                    index = i;
                }
            }
        }
        String val = mIntToStrTable.get(index);
        return val;
    }

    // do unit testing of this class
    public static void main(String[] args) {
    }
}
