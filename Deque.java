/* *****************************************************************************
 *  Name:              Branko Milosevic
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        public Item item;
        public Node previous;
        public Node next;
    }

    private Node first;
    private Node last;

    private int numOfElements;

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            /* not supported */
            throw new UnsupportedOperationException("Iterator: remove not supported!");
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException("Iterator: empty deuqe!");

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public Deque() {
        first = null;
        last = null;
        numOfElements = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return numOfElements;
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Add first: not possible to add null element!");

        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.previous = null;
        if (oldFirst != null) oldFirst.previous = first;
        if (last == null) last = first;
        numOfElements++;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Add last: not possible to add null element!");

        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.previous = oldLast;
        last.next = null;
        if (oldLast != null) oldLast.next = last;
        if (first == null) first = last;
        numOfElements++;
    }

    public Item removeFirst() {
        if (numOfElements < 1)
            throw new java.util.NoSuchElementException("remove last: empty deque!");
        if (first == null) return null;

        Item itemToRemove = first.item;
        first = first.next;
        if (first != null)
            first.previous = null;
        else
            last = null;

        numOfElements--;
        return itemToRemove;
    }

    public Item removeLast() {
        if (numOfElements < 1)
            throw new java.util.NoSuchElementException("remove last: empty deque!");
        if (last == null) return null;

        Item itemToRemove = last.item;
        last = last.previous;

        if (last != null)
            last.next = null;
        else
            first = null;

        numOfElements--;
        return itemToRemove;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    public static void main(String[] args) {

        Deque<String> d = new Deque<String>();

        System.out.println("Number of elements: " + d.numOfElements);

        // adding something
        d.addFirst("prvi");
        d.addLast("drugi");
        d.addFirst("prviji");
        d.addLast("zadnjiji");

        for (String s : d) System.out.println(s);
        System.out.println("Number of elements: " + d.numOfElements);

        // removing
        System.out.println(d.removeFirst());
        System.out.println(d.removeFirst());
        System.out.println(d.removeFirst());
        System.out.println(d.removeFirst());

        System.out.println("Number of elements: " + d.numOfElements);

        d.addFirst("prvi 2nd");
        d.addLast("drugi 2nd");
        d.addFirst("prviji 2nd");
        d.addLast("zadnjiji 2nd");
        d.addFirst("prvi prvi 2nd");
        for (String s : d) System.out.println(s);
        System.out.println("Number of elements: " + d.numOfElements);

        d.addLast("zadnji 3rd");
        System.out.println(d.removeFirst());

        for (String s : d) System.out.println(s);
        System.out.println("Number of elements: " + d.numOfElements);

    }
}
