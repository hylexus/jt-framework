package io.github.hylexus.jt.jt808.support.annotation.codec;

import io.github.hylexus.jt.jt808.support.annotation.msg.req.RequestFieldAlias;
import io.github.hylexus.jt.jt808.support.data.deserialize.DefaultJt808FieldDeserializerRegistry;
import io.github.hylexus.jt.utils.HexStringUtils;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimpleJt808MsgDecoderTest {

    SimpleJt808MsgDecoder decoder;

    @BeforeEach
    void setUp() {
        this.decoder = new SimpleJt808MsgDecoder(new Jt808AnnotationBasedDecoder(new DefaultJt808FieldDeserializerRegistry(true)));
    }

    @Test
    void test() {
        final String hexString = "010040560100000000013912344329007B000B00026964393837363534333231"
                + "747970653030313233343536373831323334353637383837363534333231494430303030313233"
                + "34353637383132333435363738383736353433323101B8CA4A2D31323334353925";
        System.out.println(hexString);

        final TestEntityForDecode entity = this.decoder.decode(HexStringUtils.hexString2Bytes(hexString), TestEntityForDecode.class);

        Assertions.assertEquals(0x0100, entity.getMsgId());
        Assertions.assertEquals(1, entity.getVersion());
        Assertions.assertEquals("00000000013912344329", entity.getTerminalPhoneNumber());
        Assertions.assertEquals(123, entity.getFlowId());
        Assertions.assertEquals(11, entity.getProvinceId());
        Assertions.assertEquals(2, entity.getCityId());
        Assertions.assertEquals("id987654321", entity.getManufacturerId());
        Assertions.assertEquals("type00123456781234567887654321", entity.getTerminalType());
        Assertions.assertEquals("ID0000123456781234567887654321", entity.getTerminalId());
        Assertions.assertEquals(1, entity.getColor());
        Assertions.assertEquals("甘J-123459", entity.getCarIdentifier());
        Assertions.assertEquals(37, entity.getCheckSum());
    }

    @Data
    public static class TestEntityForDecode {

        // region 消息头
        // byte[0,2) 消息ID WORD
        @RequestFieldAlias.Word(order = -100)
        private int msgId;

        // byte[2,4) 消息体属性 WORD
        @RequestFieldAlias.Word(order = -90)
        private int msgBodyProperty;

        // byte[4,5) 协议版本号 BYTE
        @RequestFieldAlias.Byte(order = -80)
        private byte version;

        // byte[5,15) 终端手机号 BCD[10]
        @RequestFieldAlias.Bcd(order = -70, length = 10)
        private String terminalPhoneNumber;

        // byte[15,17) 消息流水号 WORD
        @RequestFieldAlias.Word(order = -60)
        private int flowId;

        // 消息包封装项: 消息体属性中第 13 位为 1 时才有该属性
        // byte[17,19) 消息总包数 WORD
        @RequestFieldAlias.Word(order = -50, conditionalOn = "hasSubPackage()")
        private Integer totalPackage;

        // 消息包封装项: 消息体属性中第 13 位为 1 时才有该属性
        // byte[19,21) 包序号 WORD
        @RequestFieldAlias.Word(order = -40, conditionalOn = "hasSubPackage()")
        private Integer currentPackageNo;

        // bit[13] 0010,0000,0000,0000(2000)(是否有子包)
        public boolean hasSubPackage() {
            // return ((msgBodyProperty & 0x2000) >> 13) == 1;
            return (msgBodyProperty & 0x2000) > 0;
        }
        // endregion 消息头

        // region 消息体
        // fixme 目前的消息体和消息头只能写在同一个类里(读取类属性的时候没有考虑父类的情况)
        // 1. [0-2) WORD 省域ID
        // WORD 类型固定长度就是2字节 所以无需指定length
        @RequestFieldAlias.Word(order = 1)
        private int provinceId;

        // 2. [2-4) WORD 市县域ID
        @RequestFieldAlias.Word(order = 2)
        private int cityId;

        // 3. [4-15) BYTE[11] 制造商ID
        @RequestFieldAlias.Bytes(order = 3, length = 11)
        private String manufacturerId;

        // 4. [15-45) BYTE[30] 终端型号
        @RequestFieldAlias.Bytes(order = 4, length = 30)
        private String terminalType;

        // 5. [45-75) BYTE[30] 终端ID
        @RequestFieldAlias.Bytes(order = 5, length = 30)
        private String terminalId;

        // 6. [75]   BYTE    车牌颜色
        @RequestFieldAlias.Byte(order = 6)
        private byte color;

        // 7. [76,n)   String    车辆标识
        // 使用 SpEL 计算消息长度(上下文中的消息体总长度减去前面消费掉的字节数)
        @RequestFieldAlias.String(order = 7, lengthExpression = "msgBodyLength() - 76")
        private String carIdentifier;
        // endregion 消息体

        // region 检验码
        @RequestFieldAlias.Byte(order = 300)
        private short checkSum;
        // endregion 检验码

        // region 辅助类和工具方法
        public int msgBodyStartIndex(boolean hasSubPackage) {
            // 2019
            return hasSubPackage ? 21 : 17;
        }

        // bit[0-9] 0000,0011,1111,1111(3FF)(消息体长度)
        public int msgBodyLength() {
            return msgBodyProperty & 0x3ff;
        }
        // endregion 辅助类和工具方法
    }

}
