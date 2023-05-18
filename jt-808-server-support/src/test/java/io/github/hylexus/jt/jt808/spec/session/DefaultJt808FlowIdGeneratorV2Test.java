package io.github.hylexus.jt.jt808.spec.session;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DefaultJt808FlowIdGeneratorV2Test {
    @Test
    void test111() {
        final Jt808FlowIdGenerator generator = new DefaultJt808FlowIdGeneratorV2();
        Assertions.assertEquals(0, generator.currentFlowId());
        Assertions.assertEquals(1, generator.nextFlowId());
        Assertions.assertEquals(21, generator.flowId(20));
    }

    @Test
    void test112() {
        final Jt808FlowIdGenerator generator = new DefaultJt808FlowIdGeneratorV2(Jt808FlowIdGenerator.MAX_FLOW_ID - 1);
        Assertions.assertEquals(65534, generator.currentFlowId());
        Assertions.assertEquals(65535, generator.nextFlowId());
        Assertions.assertEquals(0, generator.nextFlowId());
        Assertions.assertEquals(1, generator.nextFlowId());
        Assertions.assertEquals(2, generator.nextFlowId());
        Assertions.assertEquals(3, generator.nextFlowId());
        Assertions.assertEquals(4, generator.nextFlowId());
        Assertions.assertEquals(7, generator.flowId(3));
    }

    @Test
    void test113() {
        // 131070 --> 131070 & 0xffff --> 65534
        final Jt808FlowIdGenerator generator = new DefaultJt808FlowIdGeneratorV2(Jt808FlowIdGenerator.MAX_FLOW_ID + Jt808FlowIdGenerator.MAX_FLOW_ID);
        Assertions.assertEquals(65534, generator.currentFlowId());
        Assertions.assertEquals(65535, generator.nextFlowId());
        Assertions.assertEquals(0, generator.nextFlowId());
    }

    @Test
    void test114() {
        final Jt808FlowIdGenerator generator = new DefaultJt808FlowIdGeneratorV2(0xffff - 2);
        Assertions.assertEquals(65533, generator.currentFlowId());
        Assertions.assertArrayEquals(new int[]{65533, 65534, 65535, 0, 1, 2, 3, 4}, generator.flowIds(8));

    }

    @Test
    void test115() {
        final Jt808FlowIdGenerator generator = new DefaultJt808FlowIdGeneratorV2(Integer.MAX_VALUE - 2);
        Assertions.assertEquals(65533, generator.currentFlowId());
        Assertions.assertArrayEquals(new int[]{65533, 65534, 65535, 0, 1, 2, 3, 4}, generator.flowIds(8));
    }

    @Test
    void test116() {
        final Jt808FlowIdGenerator generator = new DefaultJt808FlowIdGeneratorV2(Integer.MIN_VALUE);
        Assertions.assertEquals(0, generator.currentFlowId());
        Assertions.assertArrayEquals(new int[]{0, 1, 2, 3, 4}, generator.flowIds(5));
    }
}