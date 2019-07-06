package cn.hylexus.jt.core.data;

import cn.hylexus.jt.core.utils.HexStringUtils;
import io.github.hylexus.utils.IntBitOps;
import io.github.hylexus.utils.Numbers;

import java.util.function.Function;

/**
 * @author hylexus
 * Created At 2019-07-03 23:44
 */
public class ByteSource {
    final private byte[] bytes;
    private int index = 0;

    public static byte[] range(byte[] bytes, int startIndex, int length) {
        byte[] tmp = new byte[length];
        System.arraycopy(bytes, startIndex, tmp, 0, length);
        return tmp;
    }

    public ByteSource(byte[] bytes) {
        this.bytes = bytes;
    }

    public int nextByte() {
        return IntBitOps.intFrom1Byte(range(bytes, index++, 1)[0]);
    }

    public int nextWord() {
        int i = IntBitOps.intFrom2Bytes(range(bytes, index, 2));
        index += 2;
        return i;
    }

    public int nextDWord() {
        int i = IntBitOps.intFrom2Bytes(range(bytes, index, 2));
        index += 4;
        return i;
    }

    public <E> E nextBytes(int n, Function<byte[], E> converter) {
        byte[] range = range(bytes, index, n);
        return converter.apply(range);
    }

    public String nextBcd(int n) {
        String s = new String(range(bytes, index, n));
        index += n;
        return s;
    }

    public String nextString(int n) {
        String s = new String(range(bytes, index, n));
        index += n;
        return s;
    }

    public static void main(String[] args) {
        String in = "0102000676890100565000325756494C5A4CDD";
        byte[] bytes = HexStringUtils.chars2Bytes(in.toCharArray());
        ByteSource source = new ByteSource(bytes);
        int x = source.nextWord();
        System.out.println(x);
        System.out.println(Integer.toHexString(x));
        int props = source.nextWord();
        System.out.println(props);
    }
}
