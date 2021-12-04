package io.github.hylexus.jt.jt808.spec;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.exception.JtIllegalStateException;

import java.util.Optional;

/**
 * @author hylexus
 */
public interface Jt808MsgHeader {

    static int msgBodyStartIndex(Jt808ProtocolVersion version, boolean hasSubPackage) {
        // 2011, 2013
        if (version.getVersionBit() == Jt808ProtocolVersion.VERSION_2011.getVersionBit()) {
            return hasSubPackage ? 16 : 12;
        }
        // 2019
        if (version.getVersionBit() == Jt808ProtocolVersion.VERSION_2019.getVersionBit()) {
            return hasSubPackage ? 21 : 17;
        }
        throw new JtIllegalStateException("未知版本,version=" + version);
    }

    default int msgBodyStartIndex() {
        return Jt808MsgHeader.msgBodyStartIndex(version(), msgBodyProps().hasSubPackage());
    }

    Jt808ProtocolVersion version();

    int msgType();

    MsgBodyPropsSpec msgBodyProps();

    default int msgBodyLength() {
        return msgBodyProps().msgBodyLength();
    }

    String terminalId();

    int flowId();

    Optional<SubPackageSpec> subPackageSpec();

    String toString();

    interface MsgBodyPropsSpec {

        int intValue();

        // bit[0-9] 0000,0011,1111,1111(3FF)(消息体长度)
        default int msgBodyLength() {
            return intValue() & 0x3ff;
        }

        // bit[10-12] 0001,1100,0000,0000(1C00)(加密类型)
        default int encryptionType() {
            return (intValue() & 0x1c00) >> 10;
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

    }

    interface SubPackageSpec {
        int totalSubPackageCount();

        int currentPackageNo();
    }
}
