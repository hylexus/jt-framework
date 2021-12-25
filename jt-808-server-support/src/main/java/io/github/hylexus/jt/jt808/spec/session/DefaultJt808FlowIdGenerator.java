package io.github.hylexus.jt.jt808.spec.session;

import io.github.hylexus.jt.exception.JtIllegalArgumentException;

/**
 * @author hylexus
 */
public class DefaultJt808FlowIdGenerator implements Jt808FlowIdGenerator {

    // private final AtomicInteger nextFlowId = new AtomicInteger(0);

    private int nextFlowId = 0;

    @Override
    public synchronized int flowId(int increment) {
        if (increment <= 0) {
            return nextFlowId;
        }
        if (increment >= MAX_FLOW_ID) {
            throw new JtIllegalArgumentException();
        }
        if (nextFlowId + increment >= MAX_FLOW_ID) {
            nextFlowId = increment;
            return nextFlowId;
        }

        nextFlowId += increment;
        return nextFlowId;
    }

    //    @Override
    //    public int flowId(int increment) {
    //        int current = this.nextFlowId.get();
    //        if (increment <= 0) {
    //            return current;
    //        }
    //        if (increment >= MAX_FLOW_ID) {
    //            throw new JtIllegalArgumentException();
    //        }
    //        if (current + increment >= MAX_FLOW_ID) {
    //            if (nextFlowId.compareAndSet(current, increment)) {
    //                return increment;
    //            }
    //        }
    //
    //        int target;
    //        do {
    //            current = nextFlowId.get();
    //            target = current + increment;
    //        } while (!nextFlowId.compareAndSet(current, target));
    //
    //        return target;
    //    }

}
