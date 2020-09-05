/* *****************************************************************************
 *  Name:              Branko Milosevic
 *  Coursera User ID:  123456
 *  Last modified:     2020-05-02
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] seq = new char[R];

        for (int i = 0; i < R; i++)
            seq[i] = (char) i;

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char t = seq[0];
            int i;
            for (i = 0; i < R - 1; i++) {
                char tmp;
                if (t == c) break;
                tmp = t;
                t = seq[i + 1];
                seq[i + 1] = tmp;
            }
            seq[0] = t;
            BinaryStdOut.write((char) i);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] seq = new char[R];

        for (int i = 0; i < R; i++)
            seq[i] = (char) i;

        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            char c = seq[index];
            BinaryStdOut.write(seq[index]);

            for (int i = index; i > 0; i--)
                seq[i] = seq[i - 1];
            seq[0] = c;
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Command line argument error !!!");
    }
}
