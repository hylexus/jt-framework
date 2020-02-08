package io.github.hylexus.jt808.support;

/**
 * @author hylexus
 * Created At 2020-02-02 4:56 下午
 */
public interface OrderedComponent {

    int DEFAULT_ORDER = 0;

    int ANNOTATION_BASED_DEV_COMPONENT_ORDER = 100;

    int BUILTIN_COMPONENT_ORDER = 200;

    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;


    default int getOrder() {
        return DEFAULT_ORDER;
    }

    default boolean shouldBeReplacedBy(OrderedComponent another) {
        // 数字越小优先级越高
        // 数字小的覆盖数字大的
        return this.getOrder() > another.getOrder();
    }
}
