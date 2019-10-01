package io.github.hylexus.jt.data.msg;

import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.utils.HexStringUtils;
import io.github.hylexus.oaks.utils.BcdOps;
import io.github.hylexus.oaks.utils.IntBitOps;
import lombok.Data;

import java.util.Optional;

/**
 * @author hylexus
 * Created At 2019-09-30 4:47 下午
 */
@Data
public class AdditionalItemEntity {
    private int groupMsgId = -1;
    private int msgId;
    private int length;
    private byte[] rawBytes;

    public Optional<String> getAsBcdString() {
        try {
            return Optional.of(BcdOps.bytes2BcdString(rawBytes, 0, length));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<String> getAsString() {
        try {
            return Optional.of(new String(rawBytes, JtProtocolConstant.JT_808_STRING_ENCODING));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Integer> getAsInteger() {
        try {
            return Optional.of(IntBitOps.intFromBytes(rawBytes, 0, length));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        return "AdditionalItem {"
                + "groupMsgId="
                + "("
                + HexStringUtils.int2HexString(groupMsgId, 4, true)
                + ")"
                + groupMsgId
                + ", msgId="
                + "("
                + HexStringUtils.int2HexString(msgId, 4, true)
                + ")"
                + msgId
                + ", length=" + length
                + ", rawBytes=" + (rawBytes == null || rawBytes.length == 0 ? "" : "0x" + HexStringUtils.bytes2HexString(rawBytes))
                + '}';
    }
}
