package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.jt808.spec.builtin.msg.BaseReqRespMsgTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BuiltinMsg0801V2013AliasTest extends BaseReqRespMsgTest {

    @Test
    void testDecode() {
        final String hex = "7e0801002901391234432300000000006f0000077b000000010000000201d907f2073d336c0064003c004e1410211951090102030405757e";
        final BuiltinMsg0801V2013Alias entity = decode(hex, BuiltinMsg0801V2013Alias.class);
        assertEquals(111, entity.getMultimediaDataID());
        assertEquals(0, entity.getMultimediaType());
        assertEquals(0, entity.getMultimediaFormatCode());
        assertEquals(7, entity.getEventItemCode());
        assertEquals(123, entity.getChannelId());
        final BuiltinMsg0200V2013Alias location = entity.getLocation();
        assertEquals(1, location.getAlarmFlag());
        assertEquals(2, location.getStatus());
        assertEquals(31000562L, location.getLat());
        assertEquals(121451372L, location.getLng());
        assertEquals(100, location.getHeight());
        assertEquals(60, location.getSpeed());
        assertEquals(78, location.getDirection());
        assertEquals("141021195109", location.getTime());

        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, entity.getMediaData());
    }
}
