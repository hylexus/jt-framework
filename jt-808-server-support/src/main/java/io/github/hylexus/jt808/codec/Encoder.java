package io.github.hylexus.jt808.codec;

import com.google.common.collect.Lists;
import io.github.hylexus.jt.codec.encode.CommonFieldEncoder;
import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.utils.ProtocolUtils;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.oaks.utils.Bytes;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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


    public int generateMsgBodyProps(int msgBodySize, int encryptionType, boolean isSubPackage, int reversedLastBit) {
        return ProtocolUtils.generateMsgBodyPropsForJt808(msgBodySize, encryptionType, isSubPackage, reversedLastBit);
    }

    private byte[] generateMsgHeader4Resp(int msgId, int bodyProps, String terminalId, int flowId) throws IOException {
        return ProtocolUtils.generateMsgHeaderForJt808RespMsg(msgId, bodyProps, terminalId, flowId);
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
            // return ProtocolUtils.doEscape4SendJt808Msg(noEscapedBytes, 1, noEscapedBytes.length - 2);
        }
        return noEscapedBytes;
    }

    public byte[] encodeRespMsg(RespMsgBody bodySupport, int flowId, String terminalPhone) throws IOException {
        byte[] body = bodySupport.toBytes();
        int bodyProps = this.generateMsgBodyProps(body.length, 0b000, false, 0);
        byte[] header = this.generateMsgHeader4Resp(bodySupport.replyMsgType().getMsgId(), bodyProps, terminalPhone, flowId);
        byte[] headerAndBody = Bytes.concatAll(Lists.newArrayList(header, body));
        byte checkSum = this.bytesEncoder.calculateCheckSum(headerAndBody, 0, headerAndBody.length);
        return doEncode(headerAndBody, checkSum, true);
    }
}
