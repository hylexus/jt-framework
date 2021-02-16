package io.github.hylexus.jt.msg.builder;

import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.utils.Assertions;
import io.github.hylexus.oaks.utils.BcdOps;

/**
 * Created At 2020/12/5 2:22 下午
 *
 * @author hylexus
 */
public abstract class MsgBuilder {

    private static final int MASK = 0xFF;
    public static final int DEFAULT_INITIAL_CAPACITY = 128;
    public static final int MAX_CAPACITY = Integer.MAX_VALUE;

    protected byte[] array;
    protected int writeIndex = 0;
    private final int maxCapacity;

    protected MsgBuilder() {
        this(DEFAULT_INITIAL_CAPACITY, MAX_CAPACITY);
    }

    protected MsgBuilder(int initialCapacity, int maxCapacity) {
        Assertions.isTrue(maxCapacity > 0, "maxCapacity > 0");
        Assertions.isTrue(initialCapacity > 0 && initialCapacity <= maxCapacity, "initialCapacity > 0 && initialCapacity <= maxCapacity");

        this.maxCapacity = maxCapacity;
        this.array = new byte[initialCapacity];
    }

    protected void ensureWritable(int minSize) {
        if (minSize <= writableBytes()) {
            return;
        }

        if (minSize > maxCapacity - writeIndex) {
            throw new IndexOutOfBoundsException(String.format(
                    "writeIndex(%d) + minWritableBytes(%d) exceeds maxCapacity(%d): %s",
                    writeIndex, minSize, maxCapacity, this)
            );
        }

        final int calculateNewCapacity = calculateNewCapacity(minSize + writeIndex, maxCapacity);
        ensureCapacity(calculateNewCapacity);
    }

    protected void ensureCapacity(int newCapacity) {
        byte[] newArray = new byte[newCapacity];
        System.arraycopy(array, 0, newArray, 0, array.length);
        this.array = newArray;
    }

    public int capacity() {
        return array.length;
    }

    protected final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= maxCapacity) ? maxCapacity : n + 1;
    }

    int calculateNewCapacity(int minNewCapacity, int maxCapacity) {
        int newCapacity = tableSizeFor(minNewCapacity * 2);

        checkNewCapacity(newCapacity);
        return newCapacity;
    }

    protected final void checkNewCapacity(int newCapacity) {
        if (newCapacity < 0 || newCapacity > maxCapacity) {
            throw new IllegalArgumentException("newCapacity: " + newCapacity + " (expected: 0-" + maxCapacity + ')');
        }
    }

    public int writableBytes() {
        return capacity() - writeIndex;
    }

    protected void setByte(byte b) {
        array[writeIndex++] = b;
    }

    public MsgBuilder append(byte[] bytes) {
        final int length = bytes.length;
        ensureWritable(length);
        System.arraycopy(bytes, 0, array, writeIndex, length);
        this.writeIndex += length;
        return this;
    }

    public MsgBuilder appendByte(int b) {
        return this.appendByte((byte) b);
    }

    public MsgBuilder appendByte(byte b) {
        ensureWritable(1);
        setByte(b);
        return this;
    }

    public MsgBuilder appendBytes(byte[] bytes) {
        return this.append(bytes);
    }

    public MsgBuilder appendWord(int data) {
        ensureWritable(MsgDataType.WORD.getByteCount());
        setByte((byte) ((data >>> 8) & MASK));
        setByte((byte) (data & MASK));

        return this;
    }

    public MsgBuilder appendDword(int data) {
        return this.appendDword((long) data);
    }

    public MsgBuilder appendDword(long data) {
        ensureWritable(MsgDataType.DWORD.getByteCount());
        setByte((byte) ((data >>> 24) & MASK));
        setByte((byte) ((data >>> 16) & MASK));
        setByte((byte) ((data >>> 8) & MASK));
        setByte((byte) (data & MASK));
        return this;
    }

    public MsgBuilder appendBcd(String bcd) {
        append(BcdOps.bcdString2bytes(bcd));
        return this;
    }

    public MsgBuilder appendString(String string) {
        append(string.getBytes(JtProtocolConstant.JT_808_STRING_ENCODING));
        return this;
    }

    public byte[] build() {
        byte[] result = new byte[writeIndex];
        System.arraycopy(array, 0, result, 0, writeIndex);
        return result;
    }

}
