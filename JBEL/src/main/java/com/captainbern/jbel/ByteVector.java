package com.captainbern.jbel;

/**
 * Class to easy work with bytes etc.
 */
public class ByteVector {

    public static final int DEFAULT_SIZE = 64;

    private byte[] data;

    private int length;

    public ByteVector() {
        this(DEFAULT_SIZE);
    }

    public ByteVector(final int length) {
        this.data = new byte[length];
    }

    public byte[] getBytes() {
        return this.data;
    }

    public int getLength() {
        return this.length;
    }

    public ByteVector putByte(final int b) {
        ensureCapacity(1);

        this.data[this.length++] = (byte) (b & 0xFF);

        return this;
    }

    public ByteVector put11(final int b1, final int b2) {
        ensureCapacity(2);

        this.data[this.length++] = (byte) (b1 & 0xFF);
        this.data[this.length++] = (byte) (b2 & 0xFF);

        return this;
    }

    public ByteVector putShort(final short s) {
        ensureCapacity(2);

        this.data[this.length++] = (byte) ((s >>> 8) & 0xFF);
        this.data[this.length++] = (byte) (s & 0xFF);

        return this;
    }

    public ByteVector put12(final int b, final short s) {
        ensureCapacity(3);

        this.data[this.length++] = (byte) (b & 0xFF);
        this.data[this.length++] = (byte) ((s >>> 8) & 0xFF);
        this.data[this.length++] = (byte) (s & 0xFF);

        return this;
    }

    public ByteVector putInt(final int i) {
        ensureCapacity(4);

        this.data[this.length++] = (byte) ((i >>> 24) & 0xFF);
        this.data[this.length++] = (byte) ((i >>> 16) & 0xFF);
        this.data[this.length++] = (byte) ((i >>> 8) & 0xFF);
        this.data[this.length++] = (byte) (i & 0xFF);

        return this;
    }

    public ByteVector putLong(final long l) {
        ensureCapacity(8);

        this.data[this.length++] = (byte) ((l >>> 56) & 0xFF);
        this.data[this.length++] = (byte) ((l >>> 48) & 0xFF);
        this.data[this.length++] = (byte) ((l >>> 40) & 0xFF);
        this.data[this.length++] = (byte) ((l >>> 32) & 0xFF);
        this.data[this.length++] = (byte) ((l >>> 24) & 0xFF);
        this.data[this.length++] = (byte) ((l >>> 16) & 0xFF);
        this.data[this.length++] = (byte) ((l >>> 8) & 0xFF);
        this.data[this.length++] = (byte) (l & 0xFF);

        return this;
    }

    public void ensureCapacity(final int size) {
        if (size + this.length <= this.data.length) {
            return;
        }

        int length1 = 2 * this.data.length;
        int length2 = this.length + size;
        byte[] newData = new byte[length1 > length2 ? length1 : length2];
        System.arraycopy(data, 0, newData, 0, this.length);
        this.data = newData;
    }
}