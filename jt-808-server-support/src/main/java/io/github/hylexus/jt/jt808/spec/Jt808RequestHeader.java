package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.exception.JtIllegalStateException;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808MsgBodyProps;
import io.github.hylexus.jt.jt808.spec.impl.DefaultJt808RequestHeader;

/**
 * @author hylexus
 */
public interface Jt808RequestHeader {

    static int msgBodyStartIndex(Jt808ProtocolVersion version, boolean hasSubPackage) {
        // 2011, 2013
        if (version.getVersionBit() == Jt808ProtocolVersion.VERSION_2013.getVersionBit()) {
            return hasSubPackage ? 16 : 12;
        }
        // 2019
        if (version.getVersionBit() == Jt808ProtocolVersion.VERSION_2019.getVersionBit()) {
            return hasSubPackage ? 21 : 17;
        }
        throw new JtIllegalStateException("未知版本,version=" + version);
    }

    default int msgBodyStartIndex() {
        return Jt808RequestHeader.msgBodyStartIndex(version(), msgBodyProps().hasSubPackage());
    }

    Jt808ProtocolVersion version();

    int msgId();

    Jt808MsgBodyProps msgBodyProps();

    default int msgBodyLength() {
        return msgBodyProps().msgBodyLength();
    }

    String terminalId();

    int flowId();

    Jt808SubPackageProps subPackage();

    String toString();

    static Jt808MsgHeaderBuilder newBuilder() {
        return new DefaultJt808RequestHeader.DefaultJt808MsgHeaderBuilder();
    }

    default Jt808MsgHeaderBuilder mutate() {
        return new DefaultJt808RequestHeader.DefaultJt808MsgHeaderBuilder(this);
    }

    interface Jt808MsgBodyProps {

        int intValue();

        // bit[0-9] 0000,0011,1111,1111(3FF)(消息体长度)
        default int msgBodyLength() {
            return intValue() & 0x3ff;
        }

        /**
         * bit[10-12] 0001,1100,0000,0000(1C00)(加密类型)
         *
         * @see #dataEncryptionType()
         */
        default int encryptionType() {
            return (intValue() & 0x1c00) >> 10;
        }

        /**
         * @see #encryptionType()
         * @since 2.1.4
         */
        default Jt808MsgEncryptionType dataEncryptionType() {
            return Jt808MsgEncryptionType.fromIntValue(this.encryptionType());
        }

        // bit[13] 0010,0000,0000,0000(2000)(是否有子包)
        default boolean hasSubPackage() {
            return ((intValue() & 0x2000) >> 13) == 1;
        }

        // bit[14] 0100,0000,0000,0000(4000)(版本标识)
        default int versionIdentifier() {
            return (intValue() & 0x4000) >> 14;
        }

        // bit[15] 1000,0000,0000,0000(8000)(保留位)
        default int reversedBit15() {
            return (intValue() & 0x8000) >> 15;
        }

        default Jt808MsgBodyPropsBuilder mutate() {
            return new DefaultJt808MsgBodyProps.DefaultJt808MsgBodyPropsBuilder(this.intValue());
        }
    }

    interface Jt808SubPackageProps {

        int totalSubPackageCount();

        int currentPackageNo();
    }

    interface Jt808MsgBodyPropsBuilder {

        Jt808MsgBodyPropsBuilder msgBodyLength(int msgBodyLength);

        Jt808MsgBodyPropsBuilder encryptionType(int encryptionType);

        Jt808MsgBodyPropsBuilder hasSubPackage(int subPackageIdentifier);

        Jt808MsgBodyPropsBuilder hasSubPackage(boolean hasSubPackage);

        Jt808MsgBodyPropsBuilder versionIdentifier(int versionIdentifier);

        Jt808MsgBodyPropsBuilder versionIdentifier(Jt808ProtocolVersion version);

        Jt808MsgBodyPropsBuilder reversedBit15(int reversedBit15);

        Jt808MsgBodyProps build();
    }

    interface Jt808MsgHeaderBuilder {
        Jt808MsgHeaderBuilder version(Jt808ProtocolVersion version);

        Jt808MsgHeaderBuilder msgType(int msgType);

        Jt808MsgHeaderBuilder msgBodyProps(Jt808MsgBodyProps msgBodyProps);

        Jt808MsgHeaderBuilder terminalId(String terminalId);

        Jt808MsgHeaderBuilder flowId(int flowId);

        Jt808MsgHeaderBuilder subPackageProps(Jt808SubPackageProps subPackageProps);

        Jt808RequestHeader build();
    }
}
