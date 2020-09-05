/* *****************************************************************************
 *  Name:              Branko Milosevic
 *  Coursera User ID:  123456
 *  Last modified:     2020-05-02
 **************************************************************************** */

public class CircularSuffixArray {
    private static final int CUTOFF = 12;   // arbitrary, it could be different

    private final char[] mText;
    private final int[] mIndex;
    private final int mN;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        mN = s.length();

        mText = s.toCharArray();
        mIndex = new int[mN];
        for (int i = 0; i < mN; i++)
            mIndex[i] = i;

        sort(0, mN - 1, 0);
    }

    private void sort(int lo, int hi, int d) {

        if (lo + d >= 2 * mN || hi + d >= 2 * mN) return;

        // small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(lo, hi, d);
            return;
        }

        int lt = lo;
        int gt = hi;
        char v = mText[(mIndex[lo] + d) % mN];
        int i = lo + 1;
        while (i <= gt) {
            int t = mText[(mIndex[i] + d) % mN];
            if (t < v) exch(lt++, i++);
            else if (t > v) exch(i, gt--);
            else i++;
        }

        sort(lo, lt - 1, d);
        sort(lt, gt, d + 1);
        sort(gt + 1, hi, d);
    }

    private void insertion(int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(mIndex[j], mIndex[j - 1], d); j--)
                exch(j, j - 1);
    }

    private boolean less(int i, int j, int d) {
        if (i == j) return false;
        i = i + d;
        j = j + d;
        while (i < 2 * mN && j < 2 * mN) {
            if (mText[i % mN] < mText[j % mN]) return true;
            if (mText[i % mN] > mText[j % mN]) return false;
            i++;
            j++;
        }
        return false;
    }

    // exchange index[i] and index[j]
    private void exch(int i, int j) {
        int swap = mIndex[i];
        mIndex[i] = mIndex[j];
        mIndex[j] = swap;
    }


    // length of s
    public int length() {
        return mN;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= mN) throw new IndexOutOfBoundsException();
        return mIndex[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        // not required
    }
}



