package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.jt808.spec.builtin.msg.BaseReqRespMsgTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BuiltinMsg0704Test extends BaseReqRespMsgTest {
    @Test
    void test2013() {
        final String hex = "7E070400E401583860765500040003010049000000000004000301D9F190073CA3C1000C000000002"
                + "11204082941010400D728AD300100310109250400000000140400000004150400000000"
                + "1604000000001702000118030000000049000000000004000301D9F190073CA3C1000C00000000"
                + "211130171352010400D728AD300100310109250400000000140400000004150400000000"
                + "1604000000001702000118030000000049000000000004000301D9F190073CA3C1000C00000000"
                + "211130171357010400D728AD300115310109250400000000140400000004150400000000"
                + "160400000000170200011803000000407E";
        System.out.println(hex);
        final BuiltinMsg0704V2013 msg = decode(hex, BuiltinMsg0704V2013.class);
        Assertions.assertEquals(3, msg.getCount());
        Assertions.assertEquals(1, msg.getType());
        Assertions.assertEquals(msg.getCount(), msg.getItemList().size());
        System.out.println(msg);
    }

    @Test
    void test2013Alias() {
        final String hex = "7E070400E401583860765500040003010049000000000004000301D9F190073CA3C1000C00000000"
                + "211204082941010400D728AD300100310109250400000000140400000004150400000000"
                + "1604000000001702000118030000000049000000000004000301D9F190073CA3C1000C00000000"
                + "211130171352010400D728AD300100310109250400000000140400000004150400000000"
                + "1604000000001702000118030000000049000000000004000301D9F190073CA3C1000C00000000"
                + "211130171357010400D728AD300115310109250400000000140400000004150400000000"
                + "160400000000170200011803000000407E";
        System.out.println(hex);
        final BuiltinMsg0704V2013Alias msg = decode(hex, BuiltinMsg0704V2013Alias.class);
        System.out.println(msg);
        Assertions.assertEquals(3, msg.getCount());
        Assertions.assertEquals(1, msg.getType());
        Assertions.assertEquals(msg.getCount(), msg.getItemList().size());
        Assertions.assertEquals("211204082941", msg.getItemList().get(0).getLocationInfo().getTime());
        Assertions.assertEquals("211130171352", msg.getItemList().get(1).getLocationInfo().getTime());
        Assertions.assertEquals("211130171357", msg.getItemList().get(2).getLocationInfo().getTime());
    }
}