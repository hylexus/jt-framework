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


    public int generateMsgBodyProps(int msgBodySize, int encryptionType, boolean isSubPackage, int reversedLastBit) {
        return ProtocolUtils.generateMsgBodyPropsForJt808(msgBodySize, encryptionType, isSubPackage, reversedLastBit);
    }

    private byte[] generateMsgHeader4Resp(int msgId, int bodyProps, String terminalId, int flowId, Jt808ProtocolVersion version) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // 1. 消息ID word(16)
            baos.write(IntBitOps.intTo2Bytes(msgId));
            // 2. 消息体属性 word(16)
            baos.write(IntBitOps.intTo2Bytes(bodyProps));
            // 协议版本
            if (version == Jt808ProtocolVersion.VERSION_2019) {
                baos.write(0x01);
            }
            // 3. 终端手机号 bcd[6]
            baos.write(BcdOps.bcdString2bytes(terminalId));
            // 4. 消息流水号 word(16),按发送顺序从 0 开始循环累加
            baos.write(IntBitOps.intTo2Bytes(flowId));
            // 消息包封装项 此处不予考虑
            return baos.toByteArray();
        }
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
        int reversedLastBit = 0;
        Jt808ProtocolVersion version = Jt808ProtocolVersion.VERSION_2011;
        // 根据终端手机判断版本: BcdOps.bcd2String 中截断最前面的0,所以2019版本为19位
        if (19 == terminalPhone.length()) {
            // 2019 14bit=1
            reversedLastBit = 1;
            version = Jt808ProtocolVersion.VERSION_2019;
        }

        int bodyProps = this.generateMsgBodyProps(body.length, 0b000, false, reversedLastBit);
        byte[] header = this.generateMsgHeader4Resp(bodySupport.replyMsgType().getMsgId(), bodyProps, terminalPhone, flowId, version);
        byte[] headerAndBody = Bytes.concatAll(Lists.newArrayList(header, body));
        byte checkSum = this.bytesEncoder.calculateCheckSum(headerAndBody, 0, headerAndBody.length);
        return doEncode(headerAndBody, checkSum, true);
    }
}
