package io.github.hylexus.jt.jt808.spec.utils;

import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808MsgEncryptionHandler;
import io.github.hylexus.jt.jt808.spec.Jt808Response;
import io.github.hylexus.jt.jt808.spec.impl.response.DefaultJt808Response;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGenerator;
import io.github.hylexus.jt.jt808.support.codec.Jt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageStorage;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgBytesProcessor;
import io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgEncoder;
import io.github.hylexus.jt.jt808.support.utils.JtProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class DynamicFieldBasedJt808MsgEncoder {
    protected final ByteBufAllocator allocator;
    protected final DynamicFieldEncoder dynamicFieldEncoder;
    protected final Jt808MsgEncoder jt808MsgEncoder;

    public static DynamicFieldBasedJt808MsgEncoder create(ByteBufAllocator allocator, Jt808MsgEncoder jt808MsgEncoder, DynamicFieldEncoder dynamicFieldEncoder) {
        return new DynamicFieldBasedJt808MsgEncoder(allocator, jt808MsgEncoder, dynamicFieldEncoder);
    }

    public static DynamicFieldBasedJt808MsgEncoder createWithoutSubPackageEventListener(ByteBufAllocator allocator, Jt808MsgEncryptionHandler encryptionHandler, Jt808ResponseSubPackageStorage subPackageStorage) {
        return new DynamicFieldBasedJt808MsgEncoder(
                allocator,
                new DefaultJt808MsgEncoder(
                        allocator,
                        new DefaultJt808MsgBytesProcessor(allocator),
                        // 忽略事件回调
                        responseSubPackage -> {
                        },
                        subPackageStorage,
                        encryptionHandler
                ),
                new DynamicFieldEncoder()
        );
    }

    public DynamicFieldBasedJt808MsgEncoder(ByteBufAllocator allocator, Jt808MsgEncoder jt808MsgEncoder, DynamicFieldEncoder dynamicFieldEncoder) {
        this.allocator = allocator;
        this.dynamicFieldEncoder = dynamicFieldEncoder;
        this.jt808MsgEncoder = jt808MsgEncoder;
    }

    /**
     * @see DynamicField#fromMap(Map)
     */
    public ByteBuf encodeWithMapList(Metadata metadata, Jt808FlowIdGenerator idGenerator, List<Map<String, Object>> fields) {
        final ByteBuf buffer = allocator.buffer();
        try {
            this.dynamicFieldEncoder.encodeFieldsWithListMap(fields, buffer);
            final Jt808Response response = metadata.toJt808Response(buffer);
            return this.jt808MsgEncoder.encode(response, idGenerator);
        } catch (Exception e) {
            JtProtocolUtils.release(buffer);
            throw e;
        }
    }

    public ByteBuf encodeWithDynamicFieldList(Metadata metadata, Jt808FlowIdGenerator idGenerator, List<DynamicField> fields) {
        final ByteBuf buffer = allocator.buffer();
        try {
            this.dynamicFieldEncoder.encodeFields(fields, buffer);
            final Jt808Response response = metadata.toJt808Response(buffer);
            return this.jt808MsgEncoder.encode(response, idGenerator);
        } catch (Exception e) {
            JtProtocolUtils.release(buffer);
            throw e;
        }
    }

    public static class Metadata {
        private Jt808ProtocolVersion version;
        private String terminalId;
        private int msgId;
        private int encryptionType = 0;
        private int maxPackageSize = 1024;
        private byte reversedBit15InHeader = 0;

        public Metadata() {
        }

        public Metadata(Jt808ProtocolVersion version, String terminalId, int msgId) {
            this.version = version;
            this.terminalId = terminalId;
            this.msgId = msgId;
        }

        protected Jt808Response toJt808Response(ByteBuf body) {
            return new DefaultJt808Response(
                    this.version,
                    this.msgId,
                    this.encryptionType,
                    this.maxPackageSize,
                    this.reversedBit15InHeader,
                    this.terminalId,
                    -1,
                    body
            );
        }

        public Jt808ProtocolVersion getVersion() {
            return version;
        }

        public Metadata setVersion(Jt808ProtocolVersion version) {
            this.version = version;
            return this;
        }

        public String getTerminalId() {
            return terminalId;
        }

        public Metadata setTerminalId(String terminalId) {
            this.terminalId = terminalId;
            return this;
        }

        public int getMsgId() {
            return msgId;
        }

        public Metadata setMsgId(int msgId) {
            this.msgId = msgId;
            return this;
        }

        public int getEncryptionType() {
            return encryptionType;
        }

        public Metadata setEncryptionType(int encryptionType) {
            this.encryptionType = encryptionType;
            return this;
        }

        public int getMaxPackageSize() {
            return maxPackageSize;
        }

        public Metadata setMaxPackageSize(int maxPackageSize) {
            this.maxPackageSize = maxPackageSize;
            return this;
        }

        public byte getReversedBit15InHeader() {
            return reversedBit15InHeader;
        }

        public Metadata setReversedBit15InHeader(byte reversedBit15InHeader) {
            this.reversedBit15InHeader = reversedBit15InHeader;
            return this;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Metadata.class.getSimpleName() + "[", "]")
                    .add("version=" + version)
                    .add("terminalId='" + terminalId + "'")
                    .add("msgId=" + msgId)
                    .add("encryptionType=" + encryptionType)
                    .add("maxPackageSize=" + maxPackageSize)
                    .add("reversedBit15InHeader=" + reversedBit15InHeader)
                    .toString();
        }
    }
}
