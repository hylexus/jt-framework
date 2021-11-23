package io.github.hylexus.jt808.codec.impl;

import io.github.hylexus.jt.annotation.BuiltinComponent;
import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.exception.JtUnsupportedProtocolVersionException;
import io.github.hylexus.jt808.codec.BytesEncoder;
import io.github.hylexus.jt808.codec.RequestMsgMetadataDecoder;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.msg.RequestMsgMetadata;
import io.github.hylexus.oaks.utils.BcdOps;
import io.github.hylexus.oaks.utils.Bytes;
import lombok.extern.slf4j.Slf4j;

import static io.github.hylexus.oaks.utils.IntBitOps.intFromBytes;

/**
 * @author hylexus
 */
@Slf4j
@BuiltinComponent
public class DefaultRequestMsgMetadataDecoder implements RequestMsgMetadataDecoder {

    private final BytesEncoder bytesEncoder;

    public DefaultRequestMsgMetadataDecoder(BytesEncoder bytesEncoder) {
        this.bytesEncoder = bytesEncoder;
    }

    @Override
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
            log.error("Parse MsgHeader error, expected byte[2,3][0-9](bodyLength) is {}, actual : {}", msgHeader.getMsgBodyLength(), ret.getBodyBytes().length);
        }

        // 3. 去掉分隔符之后，最后一位就是校验码
        final byte checkSumInPkg = bytes[bytes.length - 1];
        ret.setCheckSum(checkSumInPkg);
        validateCheckSum(bytes, msgHeader, checkSumInPkg);
        return ret;
    }

    protected int getMsgBodyStartIndex(Jt808ProtocolVersion version, boolean hasSubPackage) {
        // 2011, 2013
        if (version.getVersionBit() == Jt808ProtocolVersion.VERSION_2011.getVersionBit()) {
            return hasSubPackage ? 16 : 12;
        }
        // 2019
        if (version.getVersionBit() == Jt808ProtocolVersion.VERSION_2019.getVersionBit()) {
            return hasSubPackage ? 21 : 17;
        }
        throw new JtIllegalArgumentException("未知版本,version=" + version);
    }

    protected void validateCheckSum(byte[] bytes, RequestMsgHeader msgHeader, byte checkSumInPkg) {
        final int calculatedCheckSum = this.bytesEncoder.calculateCheckSum(bytes, 0, bytes.length - 1);
        if (checkSumInPkg != calculatedCheckSum) {
            log.warn("检验码不一致. msgId = {}, expected : {},calculated : {}", msgHeader.getMsgId(), checkSumInPkg, calculatedCheckSum);
        }
    }

    /**
     * 该方法只处理消息头前4字节
     *
     * @param bytes bytes
     * @return RequestMsgHeader
     */
    protected RequestMsgHeader parseMsgHeader(byte[] bytes) {
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

        // 2019版 消息体属性中第14位为1
        final byte version = (byte) ((bodyProps & 0x4000) >> 14);
        if (version == 1) {
            header.setVersion(Jt808ProtocolVersion.VERSION_2019);
        } else {
            header.setVersion(Jt808ProtocolVersion.VERSION_2011);
        }
        return header;
    }

    protected RequestMsgHeader parseMsgHeaderFromBytes(Jt808ProtocolVersion version, byte[] bytes) {
        switch (version) {
            case AUTO_DETECTION: {
                return parseMsgHeaderFromBytes(bytes);
            }
            case VERSION_2019: {
                return parseMsgHeaderFromBytesForVersion2019(bytes);
            }
            case VERSION_2011: {
                return parseMsgHeaderFromBytesForVersion2011(bytes);
            }
            default: {
                throw new JtIllegalArgumentException("未知版本: " + version);
            }
        }
    }

    protected RequestMsgHeader parseMsgHeaderFromBytes(byte[] bytes) {
        final RequestMsgHeader header = this.parseMsgHeader(bytes);

        if (header.getVersion() == Jt808ProtocolVersion.VERSION_2019) {
            this.parseMsgHeaderTailForVersion2019(bytes, header);
        } else if (header.getVersion() == Jt808ProtocolVersion.VERSION_2011) {
            this.parseMsgHeaderTailForVersion2011(bytes, header);
        } else {
            throw new JtUnsupportedProtocolVersionException("不支持的协议版本", bytes);
        }
        return header;
    }

    protected RequestMsgHeader parseMsgHeaderFromBytesForVersion2019(byte[] bytes) {
        final RequestMsgHeader header = this.parseMsgHeader(bytes);
        if (header.getVersion() != Jt808ProtocolVersion.VERSION_2019) {
            throw new JtIllegalStateException("Expected version : 2019, actual : " + header.getVersion());
        }
        parseMsgHeaderTailForVersion2019(bytes, header);
        return header;
    }

    protected void parseMsgHeaderTailForVersion2019(byte[] bytes, RequestMsgHeader header) {
        //  byte[2-3]   消息体属性 word(16)
        final int bodyProps = intFromBytes(bytes, 2, 2);
        // [14] 0100,0000,0000,0000(C000)(保留位)
        // version2019  bit_14 == 1
        byte versionBit = (byte) ((bodyProps & 0x4000) >> 14);
        assert versionBit == 1 : "Jt-808协议-2019版消息体属性第14位为1";

        // [15] 1000,0000,0000,0000(8000)(保留位)
        header.setReservedBit(((bodyProps & 0x8000) >> 15));
        // 3. byte[5-14]   终端手机号或设备ID bcd[10]
        header.setTerminalId(BcdOps.bytes2BcdStringV2(bytes, 5, 10));

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

    protected RequestMsgHeader parseMsgHeaderFromBytesForVersion2011(byte[] bytes) {
        final RequestMsgHeader header = this.parseMsgHeader(bytes);
        if (header.getVersion() != Jt808ProtocolVersion.VERSION_2011) {
            throw new JtIllegalStateException("expected version 2011, actual : " + header.getVersion());
        }
        parseMsgHeaderTailForVersion2011(bytes, header);
        return header;
    }

    protected void parseMsgHeaderTailForVersion2011(byte[] bytes, RequestMsgHeader header) {
        // 3. byte[4-9]   终端手机号或设备ID bcd[6]
        header.setTerminalId(BcdOps.bytes2BcdStringV2(bytes, 4, 6));

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

}
