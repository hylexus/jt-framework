package io.github.hylexus.jt.jt808.spec.session;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author hylexus
 */
public class DefaultJt808FlowIdGeneratorTest {

    @Test
    public void testFlowId() {
        final DefaultJt808FlowIdGenerator idGenerator = new DefaultJt808FlowIdGenerator();
        Assertions.assertEquals(0, idGenerator.currentFlowId());
        Assertions.assertEquals(1, idGenerator.nextFlowId());
        Assertions.assertEquals(1, idGenerator.currentFlowId());
        Assertions.assertEquals(2, idGenerator.nextFlowId());
        Assertions.assertEquals(5, idGenerator.flowId(3));
    }

    @Test
    public void testFlowIds() {
        final DefaultJt808FlowIdGenerator idGenerator = new DefaultJt808FlowIdGenerator();
        final int count = 20;
        final int[] flowIds = idGenerator.flowIds(count);

        Assertions.assertEquals(count, flowIds.length);
        Assertions.assertEquals(0, flowIds[0]);
        for (int i = 1; i < count; i++) {
            Assertions.assertEquals(1, flowIds[i] - flowIds[i - 1]);
        }
        Assertions.assertEquals(count - 1, flowIds[count - 1]);
    }


    @Test
    public void testFlowIdsOverflow() {

        final DefaultJt808FlowIdGenerator idGenerator = new DefaultJt808FlowIdGenerator();
        idGenerator.flowId(Jt808FlowIdGenerator.MAX_FLOW_ID - 100);
        Assertions.assertEquals(Jt808FlowIdGenerator.MAX_FLOW_ID - 100, idGenerator.currentFlowId());

        idGenerator.flowId(100);
        Assertions.assertEquals(100, idGenerator.currentFlowId());

        idGenerator.flowId(Jt808FlowIdGenerator.MAX_FLOW_ID - 1);
        Assertions.assertEquals(Jt808FlowIdGenerator.MAX_FLOW_ID - 1, idGenerator.currentFlowId());

        final int count = 20;
        final int[] flowIds = idGenerator.flowIds(count);

        Assertions.assertEquals(count, flowIds.length);
        Assertions.assertEquals(0, flowIds[0]);
        for (int i = 1; i < count; i++) {
            Assertions.assertEquals(1, flowIds[i] - flowIds[i - 1]);
        }
        Assertions.assertEquals(count - 1, flowIds[count - 1]);
    }

    //@Test(expected = JtIllegalArgumentException.class)
    //public void testOverflow() {
    //    final var idGenerator = new DefaultJt808FlowIdGenerator();
    //    idGenerator.flowId(Jt808FlowIdGenerator.MAX_FLOW_ID);
    //}
}
