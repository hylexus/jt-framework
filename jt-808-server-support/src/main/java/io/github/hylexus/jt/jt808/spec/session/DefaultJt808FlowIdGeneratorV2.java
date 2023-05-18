package io.github.hylexus.jt.jt808.spec.session;

import com.google.common.annotations.VisibleForTesting;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 该实现类的灵感来自于 <a href="https://github.com/spring-cloud/spring-cloud-commons/blob/8ec45e03b00f3ad96bbc03a86390ed029364bb89/spring-cloud-loadbalancer/src/main/java/org/springframework/cloud/loadbalancer/core/RoundRobinLoadBalancer.java#L111">RoundRobinLoadBalancer</a>
 * <ol>
 *     <li>去掉了 `synchronized`</li>
 *     <li>使用 {@link #mask(int)} 来处理溢出的情况</li>
 * </ol>
 *
 * <h3>备注:</h3>
 * 每个 {@link Jt808Session} 都有单独的 {@link Jt808FlowIdGenerator} 实例。实际上并发的场景并不会很多。
 *
 * @author hylexus
 * @see <a href="https://github.com/spring-cloud/spring-cloud-commons/blob/8ec45e03b00f3ad96bbc03a86390ed029364bb89/spring-cloud-loadbalancer/src/main/java/org/springframework/cloud/loadbalancer/core/RoundRobinLoadBalancer.java#L111">RoundRobinLoadBalancer</a>
 */
public class DefaultJt808FlowIdGeneratorV2 implements Jt808FlowIdGenerator {

    private final AtomicInteger currentValue;

    public DefaultJt808FlowIdGeneratorV2() {
        this.currentValue = new AtomicInteger(0);
    }

    @VisibleForTesting
    DefaultJt808FlowIdGeneratorV2(int init) {
        this.currentValue = new AtomicInteger(this.mask(init));
    }

    @Override
    public int flowId(int increment) {
        final int next = this.currentValue.addAndGet(increment);
        return this.mask(next);
    }

    @Override
    public int[] flowIds(int count) {
        int last = this.flowId(count) - 1;
        final int[] ids = new int[count];
        for (int i = count - 1; i >= 0; i--) {
            ids[i] = this.mask(last--);
        }
        return ids;
    }

    @Override
    public int currentFlowId() {
        return this.mask(this.currentValue.get());
    }

    private int mask(int input) {
        return input & MAX_FLOW_ID;
    }
}
