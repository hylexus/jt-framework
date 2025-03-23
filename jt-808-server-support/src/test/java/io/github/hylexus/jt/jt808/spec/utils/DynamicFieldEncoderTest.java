package io.github.hylexus.jt.jt808.spec.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hylexus.jt.jt808.support.annotation.msg.PrependLengthFieldType;
import io.github.hylexus.jt.jt808.support.data.MsgDataType;
import io.github.hylexus.jt.utils.FormatUtils;
import io.github.hylexus.jt.utils.Jdk8Adapter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class DynamicFieldEncoderTest {

    @Test
    void test() throws Exception {
        final List<DynamicField> fieldList = new ArrayList<>();
        fieldList.add(new DynamicField(MsgDataType.BYTE, 52));
        fieldList.add(new DynamicField(MsgDataType.STRING, "12345", "GBK"));
        fieldList.add(new DynamicField(MsgDataType.STRING, "v-1.2.3", "GBK").setPrependLengthFieldType(PrependLengthFieldType.u8));
        // fieldList.add(new DynamicField(MsgDataType.BYTES, new byte[]{1, 2, 3, 4}, "GBK").setPrependLengthFieldType(PrependLengthFieldType.u32));
        fieldList.add(new DynamicField(MsgDataType.BYTES, Jdk8Adapter.listOf((byte) 1, (byte) 2, (byte) 3, (byte) 4), "GBK").setPrependLengthFieldType(PrependLengthFieldType.u32));

        final List<DynamicField> innerList = new ArrayList<>();
        innerList.add(new DynamicField(MsgDataType.BYTE, 52));
        innerList.add(new DynamicField(MsgDataType.STRING, "12345", "GBK"));
        innerList.add(new DynamicField(MsgDataType.STRING, "v-1.2.3", "GBK").setPrependLengthFieldType(PrependLengthFieldType.u8));
        innerList.add(new DynamicField(MsgDataType.BYTES, new byte[]{1, 2, 3, 4}, "GBK").setPrependLengthFieldType(PrependLengthFieldType.u32));
        fieldList.add(new DynamicField(MsgDataType.LIST, innerList));

        final ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();

        try {
            final DynamicFieldEncoder encoder = new DynamicFieldEncoder();
            encoder.encodeFields(fieldList, buffer);
            System.out.println(FormatUtils.toHexString(buffer));

            final List<Map<String, Object>> dynamicFields = toListMap(fieldList);
            System.out.println(dynamicFields);
            buffer.clear();
            encoder.encodeFieldsWithListMap(dynamicFields, buffer);
            System.out.println(FormatUtils.toHexString(buffer));
        } finally {
            Assertions.assertEquals(1, buffer.refCnt());
            buffer.release();
            Assertions.assertEquals(0, buffer.refCnt());
        }
    }

    private static List<Map<String, Object>> toListMap(List<DynamicField> fieldList) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writeValueAsString(fieldList);
        return objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
        });
    }
}
