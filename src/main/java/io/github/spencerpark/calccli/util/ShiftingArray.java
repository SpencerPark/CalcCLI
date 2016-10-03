package io.github.spencerpark.calccli.util;

import java.util.RandomAccess;

public class ShiftingArray<T> implements RandomAccess {
    private final T[] data;
    private int zeroPointer;

    public ShiftingArray(int dataSize) {
        if (dataSize < 0)
            throw new IllegalArgumentException("dataSize must be positive");
        this.data = (T[]) new Object[dataSize];
        this.zeroPointer = 0;
    }

    public int size() {
        return this.data.length;
    }

    private int calcPointer(int shiftAmt) {
        if (shiftAmt < 0)
            shiftAmt = ( shiftAmt % this.data.length ) + this.data.length;

        return ( this.zeroPointer + shiftAmt ) % this.data.length;
    }

    /**
     * Shift the data one position towards 0 and add {@code toAdd} to the end
     * of the array.
     * @param toAdd the data to add
     * @return the evicted element
     */
    public T pushTail(T toAdd) {
        T evicted = data[this.zeroPointer];

        data[this.zeroPointer] = toAdd;
        this.zeroPointer = calcPointer(1);

        return evicted;
    }

    /**
     * Shift the data one position away from 0 and add {@code toAdd} to the beginning
     * of the array.
     * @param toAdd the data to add
     * @return the evicted element
     */
    public T pushHead(T toAdd) {
        T evicted = data[this.zeroPointer];

        this.zeroPointer = calcPointer(-1);
        data[this.zeroPointer] = toAdd;

        return evicted;
    }

    /**
     * Get the element at the given index
     * @param index the index of the element to retrieve
     * @return the element at the index
     * @throws IndexOutOfBoundsException if the index is not in the array.
     *                                   bounds [0, {@link #size()})
     */
    public T get(int index) {
        if (index >= data.length)
            throw new IndexOutOfBoundsException("Size: "+data.length+", Index: "+index);

        return data[calcPointer(index)];
    }
}
