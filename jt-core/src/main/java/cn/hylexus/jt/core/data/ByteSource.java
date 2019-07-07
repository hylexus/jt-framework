package cn.hylexus.jt.core.data;

import cn.hylexus.jt.core.utils.HexStringUtils;
import io.github.hylexus.utils.BcdOps;
import io.github.hylexus.utils.Bytes;
import io.github.hylexus.utils.IntBitOps;
import lombok.NonNull;

import java.nio.charset.Charset;
import java.util.function.Function;

/**
 * @author hylexus
 * Created At 2019-07-03 23:44
 */
public class ByteSource {
    public static final int SIZE_OF_WORD = 2;
    public static final int SIZE_OF_D_WORD = 4;
    final private byte[] bytes;
    private int index = 0;

    public ByteSource(@NonNull final byte[] bytes) {
        this.bytes = bytes;
    }

    public byte nextByte() {
        return nextBytes(1)[0];
    }

    public byte[] nextBytes(final int bytesCount) {
        return nextBytes(bytesCount, Function.identity());
    }

    public <E> E nextBytes(final int bytesCount, @NonNull Function<byte[], E> converter) {
        byte[] range = Bytes.subSequence(bytes, index, bytesCount);
        index += bytesCount;
        return converter.apply(range);
    }

    public int nextWord() {
        int i = IntBitOps.intFrom2Bytes(bytes, index);
        index += SIZE_OF_WORD;
        return i;
    }

    public int nextDWord() {
        int i = IntBitOps.intFrom2Bytes(bytes, index);
        index += SIZE_OF_D_WORD;
        return i;
    }

    public String nextBCD(final int n) {
        String string = BcdOps.bcd2String(bytes, index, index + n);
        index += n;
        return string;
    }

    public String nextStringGBK(final int bytesCount) {
        return nextString(bytesCount, Charset.forName("gbk"));
    }

    public String nextString(final int bytesCount, Charset charset) {
        return nextString(bytesCount, bs -> new String(bs, charset));
    }

    public String nextString(final int bytesCount, @NonNull final Function<byte[], String> converter) {
        byte[] bytes = this.nextBytes(bytesCount);
        index += bytesCount;
        return converter.apply(bytes);
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
