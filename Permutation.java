/* *****************************************************************************
 *  Name:              Branko Milosevic
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */


import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        for (int i = 0; i < k; i++) {
            String str = StdIn.readString();
            rq.enqueue(str);
            // System.out.println(str);
        }

        /*
        String token = "";

        int j = 0;
        int i = 0;
        char ch = 0;

        while (j < inputLine.length() && i < k) {
            ch = inputLine.charAt(j);
            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9'))
                token = token + inputLine.charAt(j);
            if (inputLine.charAt(j) == ' ') {
                if (token.length() > 0) {
                    // System.out.println(token);
                    rq.enqueue(token);
                    token = "";
                    i++;
                }
            }
            j++;
        }
        */

        for (String s : rq) System.out.println(s);
    }
}
