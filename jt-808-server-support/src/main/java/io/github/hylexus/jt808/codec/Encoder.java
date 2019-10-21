package io.github.hylexus.jt808.codec;

import com.google.common.collect.Lists;
import io.github.hylexus.jt.annotation.msg.resp.CommandField;
import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.data.MsgDataType;
import io.github.hylexus.jt.data.resp.DataType;
import io.github.hylexus.jt.mata.JavaBeanFieldMetadata;
import io.github.hylexus.jt.mata.JavaBeanMetadata;
import io.github.hylexus.jt.utils.JavaBeanMetadataUtils;
import io.github.hylexus.jt.utils.ProtocolUtils;
import io.github.hylexus.jt808.msg.RespMsgBody;
import io.github.hylexus.oaks.utils.BcdOps;
import io.github.hylexus.oaks.utils.Bytes;
import io.github.hylexus.oaks.utils.IntBitOps;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author hylexus
 * createdAt 2019/2/5
 **/
@Slf4j
public class Encoder {

    public byte[] encodeCommandBody(Object commandInstance) throws InstantiationException, IllegalAccessException {
        List<byte[]> result = Lists.newArrayList();
        encodeMsgBodyRecursively(commandInstance, result);
        return Bytes.concatAll(result);
    }

    private List<byte[]> encodeMsgBodyRecursively(Object instance, List<byte[]> byteList) throws IllegalAccessException, InstantiationException {
        Class<?> cls = instance.getClass();
        JavaBeanMetadata beanMetadata = JavaBeanMetadataUtils.getBeanMetadata(cls);
        for (JavaBeanFieldMetadata fieldMetadata : beanMetadata.getFieldMetadataList()) {
            if (fieldMetadata.isAnnotationPresent(CommandField.class)) {
                CommandField annotation = fieldMetadata.getAnnotation(CommandField.class);
                MsgDataType msgDataType = annotation.dataType();
                Object value = fieldMetadata.getFieldValue(instance, true);
                if (value instanceof DataType) {
                    byteList.add(((DataType) value).getAsBytes());
                } else if (msgDataType != MsgDataType.UNKNOWN) {
                    // TODO msgConverter
                    byte[] bytes = IntBitOps.intTo1Byte((Integer) value);
                    byteList.add(bytes);
                } else {
                    if (value instanceof Collection) {
                        for (Object item : ((Collection<?>) value)) {
                            encodeMsgBodyRecursively(item, byteList);
                        }
                    }
                }
            } else {
                log.debug("No annotation @{} found on field [{}], encoding skipped.", CommandField.class.getSimpleName(), fieldMetadata.getField());
            }
        }
        return byteList;
    }

    public int generateMsgBodyProps(int msgBodySize, int encryptionType, boolean isSubPackage, int reversedLastBit) {

        if (msgBodySize >= 1024) {
            log.warn("The max value of msgBodySize is 1024, but {} .", msgBodySize);
        }

        // [ 0-9 ] 0000,0011,1111,1111(3FF)(消息体长度)
        int props = (msgBodySize & 0x3FF)
                // [10-12] 0001,1100,0000,0000(1C00)(加密类型)
                | ((encryptionType << 10) & 0x1C00)
                // [ 13_ ] 0010,0000,0000,0000(2000)(是否有子包)
                | (((isSubPackage ? 1 : 0) << 13) & 0x2000)
                // [14-15] 1100,0000,0000,0000(C000)(保留位)
                | ((reversedLastBit << 14) & 0xC000);
        return props & 0xFFFF;
    }

    private byte[] generateMsgHeader4Resp(int msgId, int bodyProps, String terminalId, int flowId) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // 1. 消息ID word(16)
            baos.write(IntBitOps.intTo2Bytes(msgId));
            // 2. 消息体属性 word(16)
            baos.write(IntBitOps.intTo2Bytes(bodyProps));
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
            return ProtocolUtils.doEscape4Send(noEscapedBytes, 1, noEscapedBytes.length - 2);
        }
        return noEscapedBytes;
    }

    public byte[] encodeRespMsg(RespMsgBody bodySupport, int flowId, String terminalPhone) throws IOException {
        byte[] body = bodySupport.toBytes();
        int bodyProps = this.generateMsgBodyProps(body.length, 0b000, false, 0);
        byte[] header = this.generateMsgHeader4Resp(bodySupport.replyMsgType().getMsgId(), bodyProps, terminalPhone, flowId);
        byte[] headerAndBody = Bytes.concatAll(Lists.newArrayList(header, body));
        byte checkSum = ProtocolUtils.calculateCheckSum4Jt808(headerAndBody, 0, headerAndBody.length);
        return doEncode(headerAndBody, checkSum, true);
    }
}
