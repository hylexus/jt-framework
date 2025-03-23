package io.github.hylexus.jt.jt808.spec.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hylexus.jt.jt808.Jt808ProtocolVersion;
import io.github.hylexus.jt.jt808.spec.Jt808MsgEncryptionHandler;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGenerator;
import io.github.hylexus.jt.jt808.support.annotation.msg.PrependLengthFieldType;
import io.github.hylexus.jt.jt808.support.codec.Jt808ResponseSubPackageStorage;
import io.github.hylexus.jt.utils.FormatUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class DynamicFieldBasedJt808MsgEncoderTest {
    final Jt808FlowIdGenerator jt808FlowIdGenerator = step -> 0;

    @Test
    void testWithDynamicFieldList() throws Exception {


        final List<DynamicField> fieldList = new ArrayList<>();
        fieldList.add(DynamicField.ofByte((short) 52));
        fieldList.add(DynamicField.ofStringGbk("12345", PrependLengthFieldType.none));
        fieldList.add(DynamicField.ofStringGbk("v-1.2.3", PrependLengthFieldType.u8));
        fieldList.add(DynamicField.ofBytes(new byte[]{1, 2, 3, 4}, PrependLengthFieldType.u32));
        // fieldList.add(DynamicField.ofBytes(Jdk8Adapter.listOf((byte) 1, (byte) 2, (byte) 3, (byte) 4), PrependLengthFieldType.u32));

        final DynamicFieldBasedJt808MsgEncoder.Metadata metadata = new DynamicFieldBasedJt808MsgEncoder.Metadata(
                Jt808ProtocolVersion.VERSION_2013,
                "013912344323",
                0x8108
        );

        final DynamicFieldBasedJt808MsgEncoder encoder = DynamicFieldBasedJt808MsgEncoder.createWithoutSubPackageEventListener(ByteBufAllocator.DEFAULT, Jt808MsgEncryptionHandler.NO_OPS, Jt808ResponseSubPackageStorage.NO_OPS_STORAGE);
        final ByteBuf encoded = encoder.encodeWithDynamicFieldList(metadata, jt808FlowIdGenerator, fieldList);
        Assertions.assertEquals("7E81080016013912344323000034313233343507762D312E322E330000000401020304887E", FormatUtils.toHexString(encoded));
        Assertions.assertEquals(1, encoded.refCnt());
        encoded.release();
    }

    @Test
    void testWithMapList() throws Exception {
        final String json = "[{\"type\":\"BYTE\",\"value\":52,\"encoding\":null,\"prependLengthFieldType\":null},{\"type\":\"STRING\",\"value\":\"12345\",\"encoding\":\"GBK\",\"prependLengthFieldType\":\"none\"},{\"type\":\"STRING\",\"value\":\"v-1.2.3\",\"encoding\":\"GBK\",\"prependLengthFieldType\":\"u8\"},{\"type\":\"BYTES\",\"value\":[1,2,3,4],\"encoding\":null,\"prependLengthFieldType\":\"u32\"}]";
        final List<Map<String, Object>> fieldList = new ObjectMapper().readValue(json, new TypeReference<List<Map<String, Object>>>() {
        });
        final DynamicFieldBasedJt808MsgEncoder.Metadata metadata = new DynamicFieldBasedJt808MsgEncoder.Metadata(
                Jt808ProtocolVersion.VERSION_2013,
                "013912344323",
                0x8108
        );
        final DynamicFieldBasedJt808MsgEncoder encoder = DynamicFieldBasedJt808MsgEncoder.createWithoutSubPackageEventListener(ByteBufAllocator.DEFAULT, Jt808MsgEncryptionHandler.NO_OPS, Jt808ResponseSubPackageStorage.NO_OPS_STORAGE);
        final ByteBuf encoded = encoder.encodeWithMapList(metadata, jt808FlowIdGenerator, fieldList);
        Assertions.assertEquals("7E81080016013912344323000034313233343507762D312E322E330000000401020304887E", FormatUtils.toHexString(encoded));
        Assertions.assertEquals(1, encoded.refCnt());
        encoded.release();
    }
}
