package io.github.hylexus.jt808.codec;

import com.google.common.collect.Lists;
import io.github.hylexus.jt.codec.encode.CommonFieldEncoder;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.utils.ProtocolUtils;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.oaks.utils.BcdOps;
import io.github.hylexus.oaks.utils.Bytes;
import io.github.hylexus.oaks.utils.IntBitOps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author hylexus
 * createdAt 2019/2/5
 **/
@Slf4j
public class Encoder {

    @Getter
    private final BytesEncoder bytesEncoder;

    private final CommonFieldEncoder commonFieldEncoder;

    public Encoder(BytesEncoder bytesEncoder) {
        this.bytesEncoder = bytesEncoder;
        commonFieldEncoder = new CommonFieldEncoder();
    }

    public byte[] encodeCommandBody(Object commandInstance) throws InstantiationException, IllegalAccessException {
        List<byte[]> result = Lists.newArrayList();
        commonFieldEncoder.encodeMsgBodyRecursively(commandInstance, result);
        return Bytes.concatAll(result);
    }

    private byte[] doEncode(byte[] headerAndBody, byte checkSum, boolean escape) throws IOException {
        byte[] noEscapedBytes = Bytes.concatAll(Lists.newArrayList(//
                new byte[]{JtProtocolConstant.PACKAGE_DELIMITER}, // 0x7e
                headerAndBody, // 消息头+ 消息体
                new byte[]{checkSum},// 校验码
                new byte[]{JtProtocolConstant.PACKAGE_DELIMITER}// 0x7e
        ));
        if (escape) {
            return this.bytesEncoder.doEscapeForSend(noEscapedBytes, 1, noEscapedBytes.length - 2);
        }
        return noEscapedBytes;
    }

    public byte[] encodeRespMsg(RespMsgBody bodySupport, Jt808ProtocolVersion version, int flowId, String terminalPhone) throws IOException {
        final byte[] body = bodySupport.toBytes();
        final int bodyProps = ProtocolUtils.generateMsgBodyPropsForJt808(body.length, 0b00, false, version, 0);
        final byte[] header = ProtocolUtils.generateMsgHeaderForJt808RespMsg(bodySupport.replyMsgType().getMsgId(), bodyProps, version, terminalPhone, flowId);
        byte[] headerAndBody = Bytes.concatAll(Lists.newArrayList(header, body));
        byte checkSum = this.bytesEncoder.calculateCheckSum(headerAndBody, 0, headerAndBody.length);
        return doEncode(headerAndBody, checkSum, true);
    }
}
