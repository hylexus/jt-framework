package io.github.hylexus.jt.jt808.spec.builtin.msg.req;

import io.github.hylexus.jt.jt808.spec.builtin.msg.BaseReqRespMsgTest;
import io.github.hylexus.jt.utils.BitOperator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuiltinMsg0200Test extends BaseReqRespMsgTest {
    final String hex = "7E0200402D01000000000139123443210000003000000040000101CD41C2072901B00929029A005A23042821314101040000029B0202004303020309300163897E";

    @Test
    void test2019Alias() {
        final BuiltinMsg0200V2019Alias msg = decode(hex, BuiltinMsg0200V2019Alias.class);
        assertMsg(msg);
    }

    // @Test
    // void test2019AliasByteBufContainer() {
    //     // 这里为什么要单独传递一个 Consumer 进去?
    //     // 因为 request.release(); 释放的时候 ByteBufContainer 的内容也会被一起释放了
    //     final BuiltinMsg0200V2019Alias msg = decodeWithConsumer(hex, BuiltinMsg0200V2019Alias.class, this::assertMsg);
    //     // 每个 ByteBufContainer 都应该已经随着 Request 的释放一起释放了
    //     msg.getExtraItemList().forEach(it -> assertEquals(0, it.getContent().value().refCnt()));
    // }

    private void assertMsg(BuiltinMsg0200V2019Alias msg) {
        final BitOperator alarmStatus = BitOperator.mutable(msg.getAlarmFlag());
        // final BitOperator alarmStatus = msg.getBitOperator();
        assertEquals(1, alarmStatus.get(20));
        assertEquals(1, alarmStatus.get(21));

        final BitOperator status = BitOperator.mutable(msg.getStatus());
        assertEquals(1, status.get(0));
        assertEquals(1, status.get(22));

        assertEquals(120127920, msg.getLng());
        assertEquals(30228930, msg.getLat());
        assertEquals(2345, msg.getHeight());
        assertEquals(666, msg.getSpeed());
        assertEquals(90, msg.getDirection());
        assertEquals("230428213141", msg.getTime());

        final List<BuiltinMsg0200V2019Alias.ExtraItem> itemList = msg.getExtraItemList();

        assertEquals(4, itemList.size());

        final BuiltinMsg0200V2019Alias.ExtraItem item1 = itemList.get(0);
        assertEquals(0x01, item1.getId());
        assertEquals(4, item1.getContentLength());
        assertEquals(667, item1.getContent().dwordValue());

        final BuiltinMsg0200V2019Alias.ExtraItem item2 = itemList.get(1);
        assertEquals(0x02, item2.getId());
        assertEquals(2, item2.getContentLength());
        assertEquals(67, item2.getContent().wordValue());

        final BuiltinMsg0200V2019Alias.ExtraItem item3 = itemList.get(2);
        assertEquals(0x03, item3.getId());
        assertEquals(2, item3.getContentLength());
        assertEquals(777, item3.getContent().wordValue());

        final BuiltinMsg0200V2019Alias.ExtraItem item4 = itemList.get(3);
        assertEquals(0x030, item4.getId());
        assertEquals(1, item4.getContentLength());
        assertEquals(99, item4.getContent().byteValue());
    }
}