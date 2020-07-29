package io.github.hylexus.jt808.codec;

import io.github.hylexus.jt.codec.decode.FieldDecoder;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.jt808.support.entity.scan.RequestMsgHeaderAware;
import io.github.hylexus.jt808.support.entity.scan.RequestMsgMetadataAware;
import io.github.hylexus.oaks.utils.BcdOps;
import io.github.hylexus.oaks.utils.Bytes;
import io.github.hylexus.oaks.utils.Numbers;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

import static io.github.hylexus.oaks.utils.IntBitOps.intFromBytes;

/**
 * @author hylexus
 * createdAt 2019/1/28
 **/
@Slf4j
public class Decoder {

    private final FieldDecoder fieldDecoder = new FieldDecoder();
    private final BytesEncoder bytesEncoder;

    public Decoder(BytesEncoder bytesEncoder) {
        this.bytesEncoder = bytesEncoder;
    }

    public RequestMsgMetadata parseMsgMetadata(Jt808ProtocolVersion version, byte[] bytes) {
        final RequestMsgMetadata ret = new RequestMsgMetadata();

        // 1. 消息头 16byte 或 12byte
        final RequestMsgHeader msgHeader = this.parseMsgHeaderFromBytes(version, bytes);
        ret.setHeader(msgHeader);

        // 2. 消息体
        // 有子包信息,消息体起始字节后移四个字节:消息包总数(word(16))+包序号(word(16))
        final int msgBodyByteStartIndex = this.getMsgBodyStartIndex(msgHeader.getVersion(), msgHeader.isHasSubPackage());
        // ret.setBodyBytes(Bytes.subSequence(bytes, msgBodyByteStartIndex, msgHeader.getMsgBodyLength()));
        ret.setBodyBytes(Bytes.range(bytes, msgBodyByteStartIndex, bytes.length - 1));
        if (msgHeader.getMsgBodyLength() != ret.getBodyBytes().length) {
            log.error("parse MsgHeader error, expected(byte[2,3]) bodyLength is {}, actual : {}", msgHeader.getMsgBodyLength(), ret.getBodyBytes().length);
        }

        // 3. 去掉分隔符之后，最后一位就是校验码
        final byte checkSumInPkg = bytes[bytes.length - 1];
        ret.setCheckSum(checkSumInPkg);
        validateCheckSum(bytes, msgHeader, checkSumInPkg);
        return ret;
    }

    private int getMsgBodyStartIndex(Jt808ProtocolVersion version, boolean hasSubPackage) {
        switch (version) {
            case VERSION_2011:
                return hasSubPackage ? 16 : 12;
            case VERSION_2019:
                return hasSubPackage ? 21 : 17;
            default:
                throw new JtIllegalArgumentException("未知版本,version=" + version);
        }
    }

    private void validateCheckSum(byte[] bytes, RequestMsgHeader msgHeader, byte checkSumInPkg) {
        final int calculatedCheckSum = this.bytesEncoder.calculateCheckSum(bytes, 0, bytes.length - 1);
        if (checkSumInPkg != calculatedCheckSum) {
            log.warn("检验码不一致,msgId:{},expected : {},calculated : {}", msgHeader.getMsgId(), checkSumInPkg, calculatedCheckSum);
        }
    }

    /**
     * 该方法只处理消息头前4字节
     *
     * @param bytes bytes
     * @return RequestMsgHeader
     */
    private RequestMsgHeader parseMsgHeader(byte[] bytes) {
        final RequestMsgHeader header = new RequestMsgHeader();

        // 1. byte[0-1]   消息ID word(16)
        header.setMsgId(intFromBytes(bytes, 0, 2));

        // 2. byte[2-3]   消息体属性 word(16)
        final int bodyProps = intFromBytes(bytes, 2, 2);
        header.setMsgBodyPropsField(bodyProps);

        // [ 0-9 ] 0000,0011,1111,1111(3FF)(消息体长度)
        header.setMsgBodyLength(bodyProps & 0x3ff);
        // [10-12] 0001,1100,0000,0000(1C00)(加密类型)
        header.setEncryptionType((bodyProps & 0x1c00) >> 10);
        // [ 13_ ] 0010,0000,0000,0000(2000)(是否有子包)
        header.setHasSubPackage(((bodyProps & 0x2000) >> 13) == 1);
        // [14-15] 1100,0000,0000,0000(C000)(保留位) (version-2011)
        header.setReservedBit(((bodyProps & 0xc000) >> 14));
        return header;
    }

    private RequestMsgHeader parseMsgHeaderFromBytes(Jt808ProtocolVersion version, byte[] bytes) {
        switch (version) {
            case VERSION_2011:
                return parseMsgHeaderFromBytesForVersion2011(bytes);
            case VERSION_2019:
                return parseMsgHeaderFromBytesForVersion2019(bytes);
            case AUTO_DETECTION: {
                return parseMsgHeaderFromBytes(bytes);
            }
            default: {
                throw new JtIllegalArgumentException("未知版本: " + version);
            }
        }
    }

    private RequestMsgHeader parseMsgHeaderFromBytes(byte[] bytes) {
        final RequestMsgHeader header = this.parseMsgHeader(bytes);
        //  byte[2-3]   消息体属性 word(16)
        final int bodyProps = intFromBytes(bytes, 2, 2);
        if (Numbers.getBitAt(bodyProps, 14) == 1) {
            this.parseMsgHeaderTailForVersion2019(bytes, header);
        } else {
            this.parseMsgHeaderTailForVersion2011(bytes, header);
        }

        return header;
    }

    private RequestMsgHeader parseMsgHeaderFromBytesForVersion2019(byte[] bytes) {
        final RequestMsgHeader header = this.parseMsgHeader(bytes);
        parseMsgHeaderTailForVersion2019(bytes, header);
        return header;
    }

    private void parseMsgHeaderTailForVersion2019(byte[] bytes, RequestMsgHeader header) {
        header.setVersion(Jt808ProtocolVersion.VERSION_2019);
        //  byte[2-3]   消息体属性 word(16)
        final int bodyProps = intFromBytes(bytes, 2, 2);
        // [14] 0100,0000,0000,0000(C000)(保留位)
        // version2019  bit_14 == 1
        byte versionBit = (byte) ((bodyProps & 0x4000) >> 14);
        assert versionBit == 1 : "Jt-808协议-2019版消息体属性第14位为1";

        // [15] 1000,0000,0000,0000(8000)(保留位)
        header.setReservedBit(((bodyProps & 0x8000) >> 15));
        // 3. byte[5-14]   终端手机号或设备ID bcd[10]
        header.setTerminalId(BcdOps.bytes2BcdString(bytes, 5, 10));

        // 4. byte[15-16]     消息流水号
        header.setFlowId(intFromBytes(bytes, 15, 2));

        // 5. byte[17-20]     消息包封装项
        if (header.isHasSubPackage()) {
            // byte[0-1]   消息包总数(word(16))
            header.setTotalSubPackage(intFromBytes(bytes, 17, 2));
            // byte[2-3]   包序号(word(16))
            header.setSubPackageSeq(intFromBytes(bytes, 19, 2));
        }
    }

    private RequestMsgHeader parseMsgHeaderFromBytesForVersion2011(byte[] bytes) {
        final RequestMsgHeader header = this.parseMsgHeader(bytes);
        parseMsgHeaderTailForVersion2011(bytes, header);
        return header;
    }

    private void parseMsgHeaderTailForVersion2011(byte[] bytes, RequestMsgHeader header) {
        header.setVersion(Jt808ProtocolVersion.VERSION_2011);
        // 3. byte[4-9]   终端手机号或设备ID bcd[6]
        header.setTerminalId(BcdOps.bytes2BcdString(bytes, 4, 6));

        // 4. byte[10-11]     消息流水号 word(16)
        header.setFlowId(intFromBytes(bytes, 10, 2));

        // 5. byte[12-15]     消息包封装项
        if (header.isHasSubPackage()) {
            // byte[0-1]   消息包总数(word(16))
            header.setTotalSubPackage(intFromBytes(bytes, 12, 2));
            // byte[2-3]   包序号(word(16))
            header.setSubPackageSeq(intFromBytes(bytes, 14, 2));
        }
    }

    public <T> T decodeRequestMsgBody(Class<T> cls, byte[] bytes, RequestMsgMetadata metadata)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {

        T instance = cls.newInstance();

        processAwareMethod(cls, instance, metadata);

        fieldDecoder.decode(instance, bytes);

        return instance;
    }

    private <T> void processAwareMethod(Class<T> cls, Object instance, RequestMsgMetadata metadata) {
        if (instance instanceof RequestMsgHeaderAware) {
            ((RequestMsgHeaderAware) instance).setRequestMsgHeader(metadata.getHeader());
        }

        if (instance instanceof RequestMsgMetadataAware) {
            ((RequestMsgMetadataAware) instance).setRequestMsgMetadata(metadata);
        }
    }

}
