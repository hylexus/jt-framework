package io.github.hylexus.jt.jt808.spec.impl;

import io.github.hylexus.jt.config.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808MsgHeader;
import io.github.hylexus.jt.utils.JtProtocolUtils;

/**
 * @author hylexus
 */
public class DefaultMsgBodyPropsSpec implements Jt808MsgHeader.MsgBodyPropsSpec {

    private final int intValue;

    public DefaultMsgBodyPropsSpec(int intValue) {
        this.intValue = intValue;
    }

    @Override
    public int intValue() {
        return intValue;
    }

    @Override
    public String toString() {
        return "MsgBodyProps{"
               + "intValue=" + intValue
               + ", msgBodyLength=" + msgBodyLength()
               + ", hasSubPackage=" + hasSubPackage()
               + ", encryptionType=" + encryptionType()
               + '}';
    }

    public static class DefaultMsgBodyPropsSpecBuilder {
        private int intValue;

        public DefaultMsgBodyPropsSpecBuilder withMsgBodyLength(int msgBodyLength) {
            this.intValue = JtProtocolUtils.setBitRange(intValue, 0, msgBodyLength, 9);
            return this;
        }

        public DefaultMsgBodyPropsSpecBuilder withEncryptionType(int encryptionType) {
            this.intValue = JtProtocolUtils.setBitRange(intValue, 10, encryptionType, 3);
            return this;
        }

        public DefaultMsgBodyPropsSpecBuilder withSubPackageIdentifier(int subPackageIdentifier) {
            this.intValue = JtProtocolUtils.setBitRange(intValue, 13, subPackageIdentifier, 1);
            return this;
        }

        public DefaultMsgBodyPropsSpecBuilder withSubPackageIdentifier(boolean hasSubPackage) {
            return this.withSubPackageIdentifier(hasSubPackage ? 1 : 0);
        }

        public DefaultMsgBodyPropsSpecBuilder withVersionIdentifier(int versionIdentifier) {
            this.intValue = JtProtocolUtils.setBitRange(intValue, 14, versionIdentifier, 1);
            return this;
        }

        public DefaultMsgBodyPropsSpecBuilder withVersionIdentifier(Jt808ProtocolVersion version) {
            return this.withVersionIdentifier(version.getVersionBit());
        }

        public DefaultMsgBodyPropsSpecBuilder withReversedBit15(int reversedBit15) {
            this.intValue = JtProtocolUtils.setBitRange(intValue, 15, reversedBit15, 1);
            return this;
        }

        public DefaultMsgBodyPropsSpec build() {
            return new DefaultMsgBodyPropsSpec(intValue);
        }
    }
}
