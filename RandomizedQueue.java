/* *****************************************************************************
 *  Name:              Branko Milosevic
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] nodes;
    private boolean[] empty;
    private int numOfElements;
    private int numOfItems;

    private class ArrayIterator implements Iterator<Item> {
        private int current = numOfElements - 1;
        private final int[] sh = generateShuffle();

        private int[] generateShuffle() {
            if (numOfElements < 1) return null;
            int[] t = new int[numOfElements];
            for (int i = 0; i < numOfElements; i++) t[i] = i;

            for (int i = 0; i < numOfElements; i++) {
                int index = StdRandom.uniform(numOfElements);
                if (i == index) continue;
                int temp = t[index];
                t[index] = t[i];
                t[i] = temp;
            }

            return t;
        }

        public boolean hasNext() {
            if (sh == null) return false;
            while (current >= 0 && empty[sh[current]]) current--;
            return current >= 0;
        }

        public void remove() {
            /* not suppored */
            throw new UnsupportedOperationException("remove() not supported in Iterator!");
        }

        public Item next() {
            if (size() < 1) throw new java.util.NoSuchElementException("Empty dequeue!");
            if (sh == null) throw new java.util.NoSuchElementException("Empty dequeue!");
            if ((sh[current] < 0) || (sh[current]) >= numOfElements)
                throw new java.util.NoSuchElementException("Empty dequeue!");
            return nodes[sh[current--]];
        }
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        nodes = (Item[]) new Object[1];
        empty = new boolean[1];
        numOfElements = 0;
        empty[0] = true;
    }

    private void resize(int n) {
        Item[] copy = (Item[]) new Object[n];
        boolean[] emCopy = new boolean[n];
        for (int i = 0; i < numOfElements; i++) {
            copy[i] = nodes[i];
            emCopy[i] = empty[i];
        }
        nodes = copy;
        empty = emCopy;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return numOfItems == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return numOfItems;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Enqueue: bad item!");
        if (numOfElements == nodes.length) resize(2 * numOfElements);
        nodes[numOfElements] = item;
        empty[numOfElements] = false;
        numOfItems++;
        numOfElements++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size() < 1) throw new java.util.NoSuchElementException("Deqeue: empty dequueu!");
        if (numOfItems < 1) return null;
        int i = StdRandom.uniform(numOfElements);

        empty[i] = true;
        numOfItems--;
        return nodes[i];
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size() < 1) throw new java.util.NoSuchElementException("Deqeue: empty dequueu!");
        if (numOfItems < 1) return null;
        int i = StdRandom.uniform(numOfElements);
        return nodes[i];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        System.out.println("Empty: " + rq.isEmpty() + "   Size: " + rq.size() + "   Array size: "
                                   + rq.numOfElements);
        for (String s : rq) System.out.println(s);

        rq.enqueue("prvi");
        rq.enqueue("drugi");
        rq.enqueue("treci");
        rq.enqueue("cetvrti");
        rq.enqueue("peti");
        rq.enqueue("sesti");

        System.out.println("Empty: " + rq.isEmpty() + "   Size: " + rq.size() + "   Array size: "
                                   + rq.numOfElements);
        for (String s : rq) System.out.println(s);

        System.out
                .println("Empty: " + rq.isEmpty() + "   Size: " + rq.size() + "   Elements size: "
                                 + rq.numOfElements);
        System.out.println(rq.sample());
        System.out.println(rq.sample());
        System.out.println(rq.sample());

        System.out
                .println("Empty: " + rq.isEmpty() + "   Size: " + rq.size() + "   Elements size: "
                                 + rq.numOfElements);
        System.out.println(rq.dequeue());
        System.out.println(rq.dequeue());
        System.out.println(rq.dequeue());

        System.out
                .println("Empty: " + rq.isEmpty() + "   Size: " + rq.size() + "   Elements size: "
                                 + rq.numOfElements);
        for (String s : rq) System.out.println(s);

    }
}
