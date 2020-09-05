/* *****************************************************************************
 *  Name:              Branko Milosevic
 *  Coursera User ID:  123456
 *  Last modified:     2020-04-26
 **************************************************************************** */

import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;

public class BoggleSolver {

    private Dictionary mDict;

    private class Dictionary {
        private static final char OFFSET = 'A';
        private Node root = new Node();

        private class Node {
            private boolean isLast = false;
            private Node[] next = new Node[26];
        }

        public void add(String s) {
            root = add(root, s, 0);
        }

        public boolean containsWord(String s) {
            Node x = get(root, s, 0);

            if (x == null) {
                return false;
            }

            return x.isLast;
        }

        public boolean containsPrefix(String s) {
            Node x = get(root, s, 0);

            return x != null;
        }

        private Node get(Node x, String s, int d) {
            if (x == null) {
                return null;
            }

            if (d == s.length()) {
                return x;
            }

            int c = s.charAt(d) - OFFSET;
            return get(x.next[c], s, d + 1);
        }

        private Node add(Node x, String s, int d) {
            if (x == null) {
                x = new Node();
            }

            if (d == s.length()) {
                x.isLast = true;
                return x;
            }

            int c = s.charAt(d) - OFFSET;
            x.next[c] = add(x.next[c], s, d + 1);

            return x;
        }
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.mDict = new Dictionary();
        for (String word : dictionary) {
            this.mDict.add(word);
        }
    }

    private class Tile {
        public int i;
        public int j;

        public Tile(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    private Iterable<Tile> adj(BoggleBoard board, Tile tile) {
        return adj(board, tile.i, tile.j);
    }

    private Iterable<Tile> adj(BoggleBoard board, int i, int j) {
        Stack<Tile> adj = new Stack<>();
        if (i > 0) {
            adj.push(new Tile(i - 1, j));
            if (j > 0) adj.push(new Tile(i - 1, j - 1));
            if (j < board.cols() - 1) adj.push(new Tile(i - 1, j + 1));
        }
        if (i < board.rows() - 1) {
            adj.push(new Tile(i + 1, j));
            if (j > 0) adj.push(new Tile(i + 1, j - 1));
            if (j < board.cols() - 1) adj.push(new Tile(i + 1, j + 1));
        }
        if (j > 0) adj.push(new Tile(i, j - 1));
        if (j < board.cols() - 1) adj.push(new Tile(i, j + 1));
        return adj;
    }

    private SET<String> dfs(BoggleBoard board, int i, int j) {
        Tile s = new Tile(i, j);
        boolean[][] marked = new boolean[board.rows()][board.cols()];
        return dfs(board, s, "" + board.getLetter(i, j), marked);
    }

    private SET<String> dfs(BoggleBoard g, Tile v, String prefix,
                            boolean[][] marked) {
        marked[v.i][v.j] = true;
        SET<String> words = new SET<>();

        if (prefix.charAt(prefix.length() - 1) == 'Q') {
            prefix += 'U';
        }

        if (mDict.containsWord(prefix) && prefix.length() > 2) {
            words.add(prefix);
        }

        for (Tile w : adj(g, v)) {
            if (!marked[w.i][w.j]) {
                char letter = g.getLetter(w.i, w.j);
                String word = prefix + letter;
                if (mDict.containsPrefix(word)) {
                    words = words.union(dfs(g, w, word, marked));
                }
            }
        }

        marked[v.i][v.j] = false;

        return words;
    }


    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        SET<String> words = new SET<>();

        for (int i = 0; i < board.rows(); ++i) {
            for (int j = 0; j < board.cols(); ++j) {
                words = words.union(dfs(board, i, j));
            }
        }

        return words;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!mDict.containsWord(word)) return 0;
        int len = word.length();
        if (len < 3) return 0;
        else if (len < 5) return 1;
        else if (len == 5) return 2;
        else if (len == 6) return 3;
        else if (len == 7) return 5;
        return 11;
    }


    public static void main(String[] args) {
        // not required
    }
}
