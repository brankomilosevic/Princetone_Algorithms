/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

public class Outcast {

    private WordNet mWordNet;

    public Outcast(WordNet wordnet) {
        mWordNet = wordnet;
    }

    public String outcast(String[] nouns) {
        int result = -1;
        int maxDis = -1;

        int[] dis = new int[nouns.length];

        for (int i = 0; i < nouns.length; i++) {
            for (int j = i + 1; j < nouns.length; j++) {
                int curDis = mWordNet.distance(nouns[i], nouns[j]);
                dis[i] += curDis;
                dis[j] += curDis;
            }
        }
        for (int i = 0; i < dis.length; i++) {
            if (dis[i] > maxDis) {
                maxDis = dis[i];
                result = i;
            }
        }
        return nouns[result];
    }

    public static void main(String[] args) {
    }
}
