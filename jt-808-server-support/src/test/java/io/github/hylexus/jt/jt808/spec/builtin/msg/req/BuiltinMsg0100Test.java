package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.jt808.spec.builtin.msg.BaseReqRespMsgTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuiltinMsg0100Test extends BaseReqRespMsgTest {

    final String hex2019 = "7E010040560100000000013912344329007B000B00026964393837363534333231"
            + "747970653030313233343536373831323334353637383837363534333231494430303030313233"
            + "34353637383132333435363738383736353433323101B8CA4A2D313233343539257E";

    final String hex2011 = "7E01000023013912344321007B000B0002696431323361626364656667684944313233343501B8CA4A2D313233343531317E";

    @Test
    void test2011() {
        final BuiltinMsg0100V2011 msg = decode(hex2011, BuiltinMsg0100V2011.class);
        assertEquals(11, msg.getProvinceId());
        assertEquals(2, msg.getCityId());
        assertEquals("id123", msg.getManufacturerId());
        assertEquals("abcdefgh", msg.getTerminalType());
        assertEquals("ID12345", msg.getTerminalId());
        assertEquals("甘J-123451", msg.getCarIdentifier());
        assertEquals(1, msg.getColor());
    }

    @Test
    void test2011Alias() {
        final BuiltinMsg0100V2011Alias msg = decodeWithConsumer(hex2011, BuiltinMsg0100V2011Alias.class, msg1 -> {
            // assertEquals("甘J-123451", msg1.getCarIdentifier().stringValue());
        });
        assertEquals(11, msg.getProvinceId());
        assertEquals(2, msg.getCityId());
        assertEquals("id123", msg.getManufacturerId());
        assertEquals("abcdefgh", msg.getTerminalType());
        assertEquals("ID12345", msg.getTerminalId());
        assertEquals("甘J-123451", msg.getCarIdentifier());
        assertEquals(1, msg.getColor());
    }

    @Test
    void test2019() {
        final BuiltinMsg0100V2019 msg = decode(hex2019, BuiltinMsg0100V2019.class);
        assertEquals(11, msg.getProvinceId());
        assertEquals(2, msg.getCityId());
        assertEquals("id987654321", msg.getManufacturerId());
        assertEquals("type00123456781234567887654321", msg.getTerminalType());
        assertEquals("ID0000123456781234567887654321", msg.getTerminalId());
        assertEquals("甘J-123459", msg.getCarIdentifier());
        assertEquals(1, msg.getColor());
    }

    @Test
    void test2019Alias() {
        final BuiltinMsg0100V2019Alias msg = decode(hex2019, BuiltinMsg0100V2019Alias.class);
        assertEquals(11, msg.getProvinceId());
        assertEquals(2, msg.getCityId());
        assertEquals("id987654321", msg.getManufacturerId());
        assertEquals("type00123456781234567887654321", msg.getTerminalType());
        assertEquals("ID0000123456781234567887654321", msg.getTerminalId());
        assertEquals("甘J-123459", msg.getCarIdentifier());
        assertEquals(1, msg.getColor());
    }
}