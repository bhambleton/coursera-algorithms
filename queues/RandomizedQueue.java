/* *****************************************************************************
 *  Name:              Brian Hambleton
 *  Last modified:     September 9 2020
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;
    private Item[] items;

    public RandomizedQueue() {
        this.items = (Item[]) new Object[2];
        this.size = 0;
    }

    // return if queue is empty or not
    public boolean isEmpty() {
        return this.size == 0;
    }

    // return the number of items in queue
    public int size() {
        return this.size;
    }

    // resize the array to new capacity and copy items
    private void resize(int newCapacity) {
        Item[] tempArray = (Item[]) new Object[newCapacity];

        for (int i = 0; i < this.size; i++)
            tempArray[i] = this.items[i];

        this.items = tempArray;
    }

    // add item to queue
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();

        if (this.size == this.items.length) resize(this.items.length * 2);

        this.items[this.size++] = item;
    }

    // remove and return a random item from queue
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();

        if (this.size == this.items.length / 4) resize(this.items.length / 2);

        // get random index and item at that index
        int randomIndex = StdRandom.uniform(this.size);
        Item randomItem = this.items[randomIndex];

        // move last element to randomly returned index
        this.items[randomIndex] = this.items[--this.size];

        // set last item (previously moved) to null, to prevent loitering
        this.items[this.size] = null;

        return randomItem;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        return this.items[StdRandom.uniform(this.size)];
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // inner iterator class
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int index;
        private int[] indices;

        public RandomizedQueueIterator() {
            this.index = 0;

            // create new array of ints containing each index in queue array
            this.indices = new int[RandomizedQueue.this.size];

            for (int iter = 0; iter < RandomizedQueue.this.size; iter++)
                this.indices[iter] = iter;

            StdRandom.shuffle(this.indices);
        }

        @Override
        public boolean hasNext() {
            return index < RandomizedQueue.this.size;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return RandomizedQueue.this.items[this.indices[index++]];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rQueue = new RandomizedQueue<>();

        for (int i = 0; i < 10; i++) {
            rQueue.enqueue(i);
        }

        StdOut.println("Size: " + rQueue.size());
        for (Integer i : rQueue)
            StdOut.println(i);

        StdOut.println("Sample: " + rQueue.sample());

        StdOut.println("Size: " + rQueue.size());
        StdOut.println("Dequeue:");
        while (!rQueue.isEmpty())
            StdOut.println(rQueue.dequeue());

        StdOut.println("Size: " + rQueue.size());
    }
}
