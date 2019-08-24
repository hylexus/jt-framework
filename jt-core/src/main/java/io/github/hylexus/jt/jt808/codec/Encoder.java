package io.github.hylexus.jt.jt808.codec;

import com.google.common.collect.Lists;
import io.github.hylexus.jt.jt808.msg.RespMsgBody;
import io.github.hylexus.jt.utils.ProtocolUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static io.github.hylexus.jt.config.JtProtocolConstant.PACKAGE_DELIMITER;
import static io.github.hylexus.oaks.utils.BcdOps.bcdString2bytes;
import static io.github.hylexus.oaks.utils.Bytes.concatAll;
import static io.github.hylexus.oaks.utils.IntBitOps.intTo2Bytes;

/**
 * @author hylexus
 * createdAt 2019/2/5
 **/
@Slf4j
public class Encoder {

    public int generateMsgBodyProps(int msgBodySize, int encryptionType, boolean isSubPackage, int reversedLastBit) {
        // [ 0-9 ] 0000,0011,1111,1111(3FF)(消息体长度)
        // [10-12] 0001,1100,0000,0000(1C00)(加密类型)
        // [ 13_ ] 0010,0000,0000,0000(2000)(是否有子包)
        // [14-15] 1100,0000,0000,0000(C000)(保留位)
        if (msgBodySize >= 1024)
            log.warn("The max value of msgBodySize is 1024, but {} .", msgBodySize);
        int props = (msgBodySize & 0x3FF)
                | ((encryptionType << 10) & 0x1C00)
                | (((isSubPackage ? 1 : 0) << 13) & 0x2000)
                | ((reversedLastBit << 14) & 0xC000);
        return props & 0xFFFF;
    }

    public byte[] generateMsgHeader4Resp(int msgId, int bodyProps, String terminalId, int flowId) throws IOException {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            // 1. 消息ID word(16)
            baos.write(intTo2Bytes(msgId));
            // 2. 消息体属性 word(16)
            baos.write(intTo2Bytes(bodyProps));
            // 3. 终端手机号 bcd[6]
            baos.write(bcdString2bytes(terminalId));
            // 4. 消息流水号 word(16),按发送顺序从 0 开始循环累加
            baos.write(intTo2Bytes(flowId));
            // 消息包封装项 此处不予考虑
            return baos.toByteArray();
        } finally {
            if (baos != null) {
                baos.close();
            }
        }
    }

    private byte[] doEncode(byte[] headerAndBody, byte checkSum, boolean escape) throws IOException {
        byte[] noEscapedBytes = concatAll(Lists.newArrayList(//
                new byte[]{PACKAGE_DELIMITER}, // 0x7e
                headerAndBody, // 消息头+ 消息体
                new byte[]{checkSum},// 校验码
                new byte[]{PACKAGE_DELIMITER}// 0x7e
        ));
        if (escape) {
            return ProtocolUtils.doEscape4Send(noEscapedBytes, 1, noEscapedBytes.length - 2);
        }
        return noEscapedBytes;
    }

    public byte[] encodeRespMsg(RespMsgBody bodySupport, int flowId, String terminalPhone) throws IOException {
        byte[] body = bodySupport.toBytes();
        int bodyProps = this.generateMsgBodyProps(body.length, 0b000, false, 0);
        byte[] header = this.generateMsgHeader4Resp(bodySupport.replyMsgType().getMsgId(), bodyProps, terminalPhone, flowId);
        byte[] headerAndBody = concatAll(Lists.newArrayList(header, body));
        byte checkSum = ProtocolUtils.calculateCheckSum4Jt808(headerAndBody, 0, headerAndBody.length);
        return doEncode(headerAndBody, checkSum, true);
    }
}
