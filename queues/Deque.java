/* *****************************************************************************
 *  Name:              Brian Hambleton
 *  Last modified:     September 9 2020
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node first, last;

    private class Node {
        private Item item;
        private Node next, prev;
    }

    // generic constructor
    public Deque() {
        this.size = 0;
        this.first = null;
        this.last = null;
    }

    // return if deque is empty or has items
    public boolean isEmpty() {
        return this.size() == 0;
    }

    // return number of items in deque
    public int size() {
        return this.size;
    }

    // add item to front of the deque
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();

        Node oldFirst = this.first;
        this.first = new Node();
        this.first.item = item;
        this.first.next = oldFirst;

        if (this.last == null) {
            // if this is the first item added, reference to last is ref to first
            this.last = this.first;
        }
        else {
            // otherwise add reference to new first to prev of next item
            this.first.next.prev = this.first;
        }

        this.size++;
    }

    // add item to back of the deque
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();

        Node oldLast = this.last;
        this.last = new Node();
        this.last.item = item;
        this.last.prev = oldLast;

        if (this.first == null) this.first = this.last;
        else this.last.prev.next = this.last;

        this.size++;
    }

    // remove and return item from front of deque
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");

        Item front = this.first.item;
        this.size--;

        if (isEmpty()) {
            this.first = null;
            this.last = null;
        }
        else {
            this.first = first.next;
            this.first.prev = null;
        }

        return front;
    }

    // remove and return item from end of deque
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");

        Item back = this.last.item;
        this.size--;

        if (isEmpty()) {
            this.last = null;
            this.first = null;
        }
        else {
            this.last = this.last.prev;
            this.last.next = null;
        }

        return back;
    }

    // return iterator over items in order from front to back
    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // inner iterator class
    private class DequeIterator implements Iterator<Item> {
        private Node current = Deque.this.first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            Item nextItem = this.current.item;
            this.current = current.next;
            return nextItem;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    } // end inner DequeIterator class

    // driver to test Deque functionality
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        StdOut.println("Is empty: " + deque.isEmpty());
        deque.addFirst(8);
        deque.addLast(6);
        deque.addFirst(4);
        deque.addFirst(1);
        StdOut.println("Is empty: " + deque.isEmpty());

        StdOut.println("Current size: " + deque.size());

        for (Integer i : deque) {
            StdOut.println(i);
        }

        Integer d = deque.removeFirst();
        StdOut.println("Removed item from front: " + d);

        Integer dLast = deque.removeLast();
        StdOut.println("Removed item from back: " + dLast);

        StdOut.println("Current size: " + deque.size());

        for (Integer i : deque) {
            StdOut.println(i);
        }
    }
}
