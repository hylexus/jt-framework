package io.github.hylexus.jt.jt808.spec.session;

import io.github.hylexus.jt.exception.JtIllegalArgumentException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author hylexus
 */
public class DefaultJt808FlowIdGeneratorTest {

    @Test
    public void testFlowId() {
        final var idGenerator = new DefaultJt808FlowIdGenerator();
        Assert.assertEquals(0, idGenerator.currentFlowId());
        Assert.assertEquals(1, idGenerator.nextFlowId());
        Assert.assertEquals(1, idGenerator.currentFlowId());
        Assert.assertEquals(2, idGenerator.nextFlowId());
        Assert.assertEquals(5, idGenerator.flowId(3));
    }

    @Test
    public void testFlowIds() {
        final var idGenerator = new DefaultJt808FlowIdGenerator();
        final int count = 20;
        final int[] flowIds = idGenerator.flowIds(count);

        Assert.assertEquals(count, flowIds.length);
        Assert.assertEquals(0, flowIds[0]);
        for (int i = 1; i < count; i++) {
            Assert.assertEquals(1, flowIds[i] - flowIds[i - 1]);
        }
        Assert.assertEquals(count - 1, flowIds[count - 1]);
    }


    @Test
    public void testFlowIdsOverflow() {

        final var idGenerator = new DefaultJt808FlowIdGenerator();
        idGenerator.flowId(Jt808FlowIdGenerator.MAX_FLOW_ID - 100);
        Assert.assertEquals(Jt808FlowIdGenerator.MAX_FLOW_ID - 100, idGenerator.currentFlowId());

        idGenerator.flowId(100);
        Assert.assertEquals(100, idGenerator.currentFlowId());

        idGenerator.flowId(Jt808FlowIdGenerator.MAX_FLOW_ID - 1);
        Assert.assertEquals(Jt808FlowIdGenerator.MAX_FLOW_ID - 1, idGenerator.currentFlowId());

        final int count = 20;
        final int[] flowIds = idGenerator.flowIds(count);

        Assert.assertEquals(count, flowIds.length);
        Assert.assertEquals(0, flowIds[0]);
        for (int i = 1; i < count; i++) {
            Assert.assertEquals(1, flowIds[i] - flowIds[i - 1]);
        }
        Assert.assertEquals(count - 1, flowIds[count - 1]);
    }

    @Test(expected = JtIllegalArgumentException.class)
    public void testOverflow() {
        final var idGenerator = new DefaultJt808FlowIdGenerator();
        idGenerator.flowId(Jt808FlowIdGenerator.MAX_FLOW_ID);
    }
}