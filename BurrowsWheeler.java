/* *****************************************************************************
 *  Name:              Branko Milosevic
 *  Coursera User ID:  123456
 *  Last modified:     2020-05-02
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;
import java.util.List;

public class BurrowsWheeler {

    private static final int R = 8;
    private static final int NUM_CHAR_VALUES = 256;

    private static char lastForShift(String s, int shift) {
        if (shift > 0) return s.charAt(shift - 1);
        else return s.charAt(s.length() - 1);
    }

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray suffixes = new CircularSuffixArray(s);
        int first = 0;
        char[] t = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            int shift = suffixes.index(i);
            t[i] = lastForShift(s, shift);
            if (shift == 0) {
                first = i;
            }
        }
        BinaryStdOut.write(first);
        for (char c : t) {
            BinaryStdOut.write(c, R);
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        List<Integer>[] occurences = (List<Integer>[]) new List[NUM_CHAR_VALUES];
        int i = 0;

        // Read data and construct occurences
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar(R);
            List<Integer> forChar = occurences[c];
            if (forChar == null) {
                occurences[c] = new LinkedList<>();
            }
            occurences[c].add(i++);
        }
        int size = i;

        // Construct next and firstRow
        int[] next = new int[size];
        char[] firstRow = new char[size];
        int j = 0;
        for (char c = 0x00; c <= 0xFF; c++) {
            List<Integer> forChar = occurences[c];
            if (forChar != null) {
                for (int charPos : forChar) {
                    next[j] = charPos;
                    firstRow[j++] = c;
                }
            }
        }

        // Construct original string using next and firstRow
        int current = first;
        for (int k = 0; k < size; k++) {
            char ch = firstRow[current];
            BinaryStdOut.write(ch, R);
            current = next[current];
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Command line argument error !!!");

    }
}
